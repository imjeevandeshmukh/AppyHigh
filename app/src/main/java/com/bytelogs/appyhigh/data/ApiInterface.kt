package com.bytelogs.appyhigh.data


import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    @GET("/v2/top-headlines")
    suspend fun getTopNews(@Query("country") country: String,@Query("apiKey") api_key: String) : NewsResponse

}