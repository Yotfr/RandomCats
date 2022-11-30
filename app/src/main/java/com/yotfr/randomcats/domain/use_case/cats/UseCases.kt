package com.yotfr.randomcats.domain.use_case.cats

data class UseCases(
    val getRandomCat: GetRandomCatUseCase,
    val uploadCatToRemoteDbUseCase: UploadCatToRemoteDbUseCase,
    val getCatsFromRemoteDb: GetCatsFromRemoteDb,
    val deleteCatFromRemoteDb: DeleteCatFromRemoteDb
)