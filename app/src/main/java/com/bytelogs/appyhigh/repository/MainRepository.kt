package com.bytelogs.appyhigh.repository


import com.bytelogs.appyhigh.BuildConfig
import com.bytelogs.appyhigh.data.ApiInterface
import javax.inject.Inject

class MainRepository @Inject constructor(var  apiInterface: ApiInterface) {


    suspend fun getNews(locale: String) = apiInterface.getTopNews(locale,BuildConfig.API_KEY)


}