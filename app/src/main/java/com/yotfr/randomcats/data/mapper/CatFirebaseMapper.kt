package com.yotfr.randomcats.data.mapper

import com.yotfr.randomcats.data.dto.CatFirebase
import com.yotfr.randomcats.domain.model.Cat

class CatFirebaseMapper {

    fun toDomain(firebaseModel: CatFirebase): Cat {
        return Cat(
            url = firebaseModel.url,
            created = firebaseModel.created
        )
    }

    fun fromDomain(domainModel: Cat): CatFirebase {
        return CatFirebase(
            url = domainModel.url,
            created = domainModel.created
        )
    }

    fun toDomainList(initial: List<CatFirebase>): List<Cat> {
        return initial.map { toDomain(it) }
    }
}
