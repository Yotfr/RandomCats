package com.yotfr.randomcats.di

import com.yotfr.randomcats.data.repository.CatsRepositoryImpl
import com.yotfr.randomcats.data.repository.UserPreferencesRepositoryImpl
import com.yotfr.randomcats.data.repository.UserRepositoryImpl
import com.yotfr.randomcats.domain.repository.CatsRepository
import com.yotfr.randomcats.domain.repository.UserPreferencesRepository
import com.yotfr.randomcats.domain.repository.UserRepository
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

    @Binds
    @Singleton
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl):UserRepository

    @Binds
    @Singleton
    fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ):UserPreferencesRepository

}