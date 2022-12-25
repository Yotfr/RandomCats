package com.yotfr.randomcats.presentation.screens.pagercatlist.mapper

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.presentation.screens.pagercatlist.model.PagerCatListModel
import java.text.SimpleDateFormat
import java.util.*

class PagerCatListMapper {

    private fun fromDomain(domainModel: Cat): PagerCatListModel {
        return PagerCatListModel(
            url = domainModel.url,
            created = domainModel.created,
            createdDateString = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(
                domainModel.created
            )
        )
    }

    fun toDomain(uiModel: PagerCatListModel): Cat {
        return Cat(
            url = uiModel.url,
            created = uiModel.created
        )
    }

    fun fromDomainList(initialList:List<Cat>):List<PagerCatListModel> {
        return initialList.map { fromDomain(it) }
    }

}