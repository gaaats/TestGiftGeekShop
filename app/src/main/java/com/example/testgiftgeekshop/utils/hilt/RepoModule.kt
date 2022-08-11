package com.example.testgiftgeekshop.utils.hilt

import com.example.testgiftgeekshop.data.MainRepoImpl
import com.example.testgiftgeekshop.domain.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @Binds
    @Singleton
    fun bindsRepository(impl: MainRepoImpl): MainRepository
}