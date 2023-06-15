package com.bbasoglu.core.di

import android.app.Application
import com.bbasoglu.core.data.SignUpCoreDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object CoreDataModule {

    @Singleton
    @Provides
    fun provideSignUpCoreDataStore(app:Application): SignUpCoreDataStore =
        SignUpCoreDataStore(app)
}