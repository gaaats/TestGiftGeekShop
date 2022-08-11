package com.example.testgiftgeekshop.utils.hilt

import android.app.Application
import com.example.testgiftgeekshop.data.PromAPI
import com.example.testgiftgeekshop.data.RetrofotIstance
import com.example.testgiftgeekshop.utils.Constances
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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