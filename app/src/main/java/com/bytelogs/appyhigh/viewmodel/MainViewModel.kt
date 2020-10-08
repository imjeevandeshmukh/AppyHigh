package com.bytelogs.appyhigh.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bytelogs.appyhigh.data.Resource
import com.bytelogs.appyhigh.repository.MainRepository
import com.bytelogs.appyhigh.repository.RemoteConfigRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class MainViewModel  constructor(val mainRepository: MainRepository,val remoteConfigRepository: RemoteConfigRepository): ViewModel() {


    fun onFetchNews(locale: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getNews(locale)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun  getOnCompleteSyncLiveData(): MutableLiveData<Boolean> {
        return  remoteConfigRepository.getOnCompleteSyncLiveData()
    }
    fun  onRemoteInit(){
        remoteConfigRepository.init()
    }

    fun getDoLoadAds():Boolean{
        return  remoteConfigRepository.getDoLoadAds()
    }


}