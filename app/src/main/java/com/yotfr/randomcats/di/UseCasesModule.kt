package com.yotfr.randomcats.di

import com.yotfr.randomcats.domain.repository.CatsRepository
import com.yotfr.randomcats.domain.use_case.GetCatsFromRemoteDb
import com.yotfr.randomcats.domain.use_case.GetRandomCatUseCase
import com.yotfr.randomcats.domain.use_case.UploadCatToRemoteDbUseCase
import com.yotfr.randomcats.domain.use_case.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {

    @Provides
    fun provideUseCases(
       catsRepository: CatsRepository
    ):UseCases{
        return UseCases(
            getRandomCat = GetRandomCatUseCase(
                catsRepository = catsRepository
            ),
            uploadCatToRemoteDbUseCase = UploadCatToRemoteDbUseCase(
                catsRepository = catsRepository
            ),
            getCatsFromRemoteDb = GetCatsFromRemoteDb(
                catsRepository = catsRepository
            )
        )
    }

}