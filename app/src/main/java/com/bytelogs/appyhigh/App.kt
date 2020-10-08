package com.bytelogs.appyhigh

import android.app.Application
import com.bytelogs.appyhigh.injection.*

import com.bytelogs.appyhigh.repository.RemoteConfigRepository
import javax.inject.Inject


class App : Application() {

    lateinit var injector: AppComponent

    @Inject
    lateinit var repository: RemoteConfigRepository
    override fun onCreate() {
        super.onCreate()
        injector = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .configModule(ConfigModule(RemoteConfigRepository()))
            .build()





    }

    fun getApplicationComponent(): AppComponent {
        return injector
    }


}