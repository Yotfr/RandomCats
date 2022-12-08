package com.yotfr.randomcats.presentation.screens.pager_cat_list.mapper

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.presentation.screens.pager_cat_list.model.PagerCatListModel
import java.text.SimpleDateFormat
import java.util.*

class PagerCatListMapper {

    fun fromDomain(domainModel: Cat): PagerCatListModel {
        return PagerCatListModel(
            id = domainModel.id,
            url = domainModel.url,
            created = domainModel.created,
            createdDateString = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(
                domainModel.created
            )
        )
    }

    fun toDomain(uiModel: PagerCatListModel): Cat {
        return Cat(
            id = uiModel.id,
            url = uiModel.url,
            created = uiModel.created
        )
    }

    fun fromDomainList(initialList:List<Cat>):List<PagerCatListModel> {
        return initialList.map { fromDomain(it) }
    }

}