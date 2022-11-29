package com.yotfr.randomcats.domain.use_case

data class UseCases(
    val getRandomCat:GetRandomCatUseCase,
    val uploadCatToRemoteDbUseCase: UploadCatToRemoteDbUseCase,
    val getCatsFromRemoteDb: GetCatsFromRemoteDb
)