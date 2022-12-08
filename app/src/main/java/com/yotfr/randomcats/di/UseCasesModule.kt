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
       catsRepository: CatsRepository,
       userRepository: UserRepository
    ): UseCases {
        return UseCases(
            getRandomCat = GetRandomCatUseCase(
                catsRepository = catsRepository
            ),
            uploadCatToRemoteDbUseCase = UploadCatToRemoteDbUseCase(
                catsRepository = catsRepository,
                userRepository = userRepository
            ),
            getCatsFromRemoteDb = GetCatsFromRemoteDb(
                catsRepository = catsRepository,
                userRepository = userRepository
            ),
            deleteCatFromRemoteDb = DeleteCatFromRemoteDb(
                catsRepository = catsRepository,
                userRepository = userRepository
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