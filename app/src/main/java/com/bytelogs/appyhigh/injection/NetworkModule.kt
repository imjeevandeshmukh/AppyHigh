package com.bytelogs.appyhigh.injection

import android.content.Context
import android.net.ConnectivityManager
import com.bytelogs.appyhigh.data.ApiInterface
import com.bytelogs.appyhigh.repository.MainRepository
import com.bytelogs.appyhigh.viewmodel.ViewModelFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("unused")
@Module
class NetworkModule{



    @Provides
    @Reusable
    internal fun  provideCache(context:Context):Cache{
        val cacheSize = (5*1024*1024).toLong()
        return Cache(context.cacheDir, cacheSize)
    }


    @Provides
    @Reusable
    internal fun provideGson():Gson{
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Reusable
    internal fun provideOkhttp(cache:Cache,context: Context):OkHttpClient{
        return  OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (verifyAvailableNetwork(context)!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Reusable
    internal fun provideRetrofit(okHttpClient:OkHttpClient,gson :Gson): Retrofit {
        val BASE_URL = "https://newsapi.org"
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }


    @Provides
    @Reusable
    internal fun provideApodsApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Reusable
    internal fun provideMainRepository(apiInterface: ApiInterface): MainRepository {
        return MainRepository(apiInterface)
    }

    @Provides
    @Reusable
    internal fun provideViewModelFactory(mainRepository: MainRepository): ViewModelFactory {
        return ViewModelFactory(mainRepository)
    }

    @Suppress("DEPRECATION")
    internal fun verifyAvailableNetwork(context: Context):Boolean{
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }



}