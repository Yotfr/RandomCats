package com.yotfr.randomcats.data.mapper

import com.yotfr.randomcats.data.dto.UserFirebase
import com.yotfr.randomcats.domain.model.SignUpModel
import com.yotfr.randomcats.domain.model.User

class UserFirebaseMapper {
    fun fromDomain(domainModel: SignUpModel): UserFirebase {
        return UserFirebase(
            email = domainModel.email,
            userName = domainModel.userName
        )
    }
    fun toDomain(firebaseModel: UserFirebase): User {
        return User(
            email = firebaseModel.email,
            userName = firebaseModel.userName
        )
    }
}
