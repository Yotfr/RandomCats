package com.yotfr.randomcats.data.mapper

import com.yotfr.randomcats.base.Constants
import com.yotfr.randomcats.data.dto.CatResponse
import com.yotfr.randomcats.domain.model.Cat

class CatMapper {

    fun toDomain(response: CatResponse): Cat {
        return Cat(
            url = Constants.BASE_URL + response.url,
            /**
             * [created] will be replaced when the user will add cat to favorites
             */
            created = 0L
        )
    }
}
