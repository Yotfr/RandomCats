package com.yotfr.randomcats.data.mapper

import com.yotfr.randomcats.data.dto.UserFirebase
import com.yotfr.randomcats.domain.model.User

class UserFirebaseMapper {
    fun toDomain(firebaseModel: UserFirebase): User {
        return User(
            email = firebaseModel.email,
            userName = firebaseModel.userName
        )
    }
}
