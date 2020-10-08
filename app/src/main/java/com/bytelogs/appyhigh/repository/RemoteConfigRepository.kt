package com.bytelogs.appyhigh.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

/**
 * Created by Jeevan Deshmukh on 08,October,2020
 */

class RemoteConfigRepository {

    private val DEFAULTS: HashMap<String, Any> =
        hashMapOf(
            DO_LOAD_ADS to true
        )

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var doLoadLiveData: MutableLiveData<Boolean>

    fun init() {
        remoteConfig = getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig(): FirebaseRemoteConfig {
        doLoadLiveData = MutableLiveData();

        val remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            if (BuildConfig.DEBUG) {
                minimumFetchIntervalInSeconds = 0
            } else {
                minimumFetchIntervalInSeconds = 60 * 60
            }
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(DEFAULTS)

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            doLoadLiveData.postValue(it.result)
        }

        return remoteConfig
    }

    fun getDoLoadAds(): MutableLiveData<Boolean> = doLoadLiveData

    companion object {
        private const val TAG = "RemoteConfigUtils"
        private const val DO_LOAD_ADS = "do_load_ads"
    }


}