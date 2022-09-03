package com.geekshop.testgiftgeekshop.utils.hilt

import com.geekshop.testgiftgeekshop.data.MainRepoImpl
import com.geekshop.testgiftgeekshop.domain.MainRepository
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