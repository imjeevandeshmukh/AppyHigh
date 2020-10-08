package com.bytelogs.appyhigh.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bytelogs.appyhigh.repository.MainRepository
import com.bytelogs.appyhigh.repository.RemoteConfigRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(val mainRepository: MainRepository,val remoteConfigRepository: RemoteConfigRepository): ViewModelProvider.Factory{


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return  MainViewModel(mainRepository,remoteConfigRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}