package com.yotfr.randomcats.presentation.screens.gridcatlist.mapper

import com.yotfr.randomcats.domain.model.Cat
import com.yotfr.randomcats.presentation.screens.gridcatlist.model.GridCatListModel
import java.text.SimpleDateFormat
import java.util.*

class GridCatListMapper {

    private fun fromDomain(domainModel: Cat): GridCatListModel {
        return GridCatListModel(
            url = domainModel.url,
            created = domainModel.created,
            createdDateString = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(
                domainModel.created
            )
        )
    }

    fun fromDomainList(initialList: List<Cat>): List<GridCatListModel> {
        return initialList.map { fromDomain(it) }
    }
}
