package com.yotfr.randomcats.domain.use_case.cats

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.domain.repository.CatsRepository
import com.yotfr.randomcats.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteCatFromRemoteDb(
    private val catsRepository: CatsRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(cat: Cat) {
        withContext(Dispatchers.IO) {
            val userId = userRepository.getCurrentUserUid()
            catsRepository.deleteFromRemoteDb(cat, userId)
        }
    }
}