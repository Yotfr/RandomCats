package com.yotfr.randomcats.di

import com.yotfr.randomcats.domain.repository.CatsRepository
import com.yotfr.randomcats.domain.repository.UserRepository
import com.yotfr.randomcats.domain.use_case.*
import com.yotfr.randomcats.domain.use_case.cats.*
import com.yotfr.randomcats.domain.use_case.users.*
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
    ): UseCases {
        return UseCases(
            getRandomCat = GetRandomCatUseCase(
                catsRepository = catsRepository
            ),
            uploadCatToRemoteDbUseCase = UploadCatToRemoteDbUseCase(
                catsRepository = catsRepository
            ),
            getCatsFromRemoteDb = GetCatsFromRemoteDb(
                catsRepository = catsRepository
            ),
            deleteCatFromRemoteDb = DeleteCatFromRemoteDb(
                catsRepository = catsRepository
            )
        )
    }

    @Provides
    fun provideUserUseCase(
        userRepository: UserRepository
    ):UserUseCases {
        return UserUseCases(
            signUpUserUseCase = SignUpUserUseCase(
                userRepository = userRepository
            ),
            signInUserUseCase = SignInUserUseCase(
                userRepository = userRepository
            ),
            checkUserSignUseCase = CheckUserSignUseCase(
                userRepository = userRepository
            ),
            signOutUseCase = SignOutUseCase(
                userRepository = userRepository
            )
        )
    }

}