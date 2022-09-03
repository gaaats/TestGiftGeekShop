package com.geekshop.testgiftgeekshop.utils.hilt

import com.geekshop.testgiftgeekshop.data.PromAPI
import com.geekshop.testgiftgeekshop.data.RetrofotIstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PromApiModule {


    @Provides
    @Singleton
    fun providesPromApiService(): PromAPI {
        return RetrofotIstance.api
    }
}