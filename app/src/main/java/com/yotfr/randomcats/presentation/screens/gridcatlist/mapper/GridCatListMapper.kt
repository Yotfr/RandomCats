package com.yotfr.randomcats.presentation.screens.gridcatlist.mapper

import com.yotfr.randomcats.presentation.screens.gridcatlist.model.GridCatListModel
import java.text.SimpleDateFormat
import java.util.*

class GridCatListMapper {

    fun fromDomain(domainModel: com.yotfr.randomcats.domain.model.Cat): GridCatListModel {
        return GridCatListModel(
            url = domainModel.url,
            created = domainModel.created,
            createdDateString = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(
                domainModel.created
            )
        )
    }

    fun toDomain(uiModel: GridCatListModel): com.yotfr.randomcats.domain.model.Cat {
        return com.yotfr.randomcats.domain.model.Cat(
            url = uiModel.url,
            created = uiModel.created
        )
    }

    fun fromDomainList(initialList:List<com.yotfr.randomcats.domain.model.Cat>):List<GridCatListModel> {
        return initialList.map { fromDomain(it) }
    }
}