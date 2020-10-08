
package com.bytelogs.appyhigh.injection

import com.bytelogs.appyhigh.repository.RemoteConfigRepository
import javax.inject.Singleton
import dagger.Module
import dagger.Provides



@Module
 class ConfigModule(private val remoteConfigRepository: RemoteConfigRepository) {

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfigRepository(): RemoteConfigRepository {

        return remoteConfigRepository;
    }

}