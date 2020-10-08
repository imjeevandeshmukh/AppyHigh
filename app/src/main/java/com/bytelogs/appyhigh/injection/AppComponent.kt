package com.bytelogs.appyhigh.injection

import android.app.Application
import com.bytelogs.appyhigh.view.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class,NetworkModule::class,ConfigModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(application: Application)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        fun appModule(appModule: AppModule):Builder

        fun networkModule(networkModule: NetworkModule): Builder

        fun configModule(configModule: ConfigModule): Builder

    }
}