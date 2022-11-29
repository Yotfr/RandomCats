package com.yotfr.randomcats.di

import com.yotfr.randomcats.data.repository.CatsRepositoryImpl
import com.yotfr.randomcats.domain.repository.CatsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindCatsRepository(catsRepositoryImpl: CatsRepositoryImpl):CatsRepository

}