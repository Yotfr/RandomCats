package com.yotfr.randomcats.presentation.screens.cats_list_screen.mapper

import com.yotfr.randomcats.presentation.screens.cats_list_screen.model.CatListModel
import java.text.SimpleDateFormat
import java.util.*

class CatListMapper {

    fun fromDomain(domainModel: com.yotfr.randomcats.domain.model.Cat): CatListModel {
        return CatListModel(
            id = domainModel.id,
            url = domainModel.url,
            created = domainModel.created,
            createdDateString = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(
                domainModel.created
            )
        )
    }

    fun toDomain(uiModel:CatListModel): com.yotfr.randomcats.domain.model.Cat {
        return com.yotfr.randomcats.domain.model.Cat(
            id = uiModel.id,
            url = uiModel.url,
            created = uiModel.created
        )
    }

    fun fromDomainList(initialList:List<com.yotfr.randomcats.domain.model.Cat>):List<CatListModel> {
        return initialList.map { fromDomain(it) }
    }
}