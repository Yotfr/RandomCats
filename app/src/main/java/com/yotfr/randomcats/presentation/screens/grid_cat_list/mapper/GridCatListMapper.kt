package com.yotfr.randomcats.presentation.screens.grid_cat_list.mapper

import com.yotfr.randomcats.presentation.screens.grid_cat_list.model.GridCatListModel
import java.text.SimpleDateFormat
import java.util.*

class GridCatListMapper {

    fun fromDomain(domainModel: com.yotfr.randomcats.domain.model.Cat): GridCatListModel {
        return GridCatListModel(
            id = domainModel.id,
            url = domainModel.url,
            created = domainModel.created,
            createdDateString = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(
                domainModel.created
            )
        )
    }

    fun toDomain(uiModel: GridCatListModel): com.yotfr.randomcats.domain.model.Cat {
        return com.yotfr.randomcats.domain.model.Cat(
            id = uiModel.id,
            url = uiModel.url,
            created = uiModel.created
        )
    }

    fun fromDomainList(initialList:List<com.yotfr.randomcats.domain.model.Cat>):List<GridCatListModel> {
        return initialList.map { fromDomain(it) }
    }
}