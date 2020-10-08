package com.bytelogs.appyhigh.injection

import android.content.Context
import javax.inject.Singleton
import dagger.Module
import dagger.Provides



@Module
 class AppModule(private val mApplication: Context) {

    @Provides
    @Singleton
    fun provideApplication(): Context {
        return mApplication
    }

}