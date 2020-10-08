package com.bytelogs.appyhigh.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bytelogs.appyhigh.data.Resource
import com.bytelogs.appyhigh.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class MainViewModel @Inject constructor(val mainRepository: MainRepository): ViewModel() {


    fun onFetchNews(locale: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getNews(locale)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

 }