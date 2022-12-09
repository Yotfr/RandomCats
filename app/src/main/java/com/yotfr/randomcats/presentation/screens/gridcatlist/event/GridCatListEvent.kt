package com.yotfr.randomcats.presentation.screens.gridcatlist.event

import com.yotfr.randomcats.presentation.screens.gridcatlist.model.GridCatListModel

interface GridCatListEvent {
    data class DeleteCatFromFavorite(val cat: GridCatListModel): GridCatListEvent
    data class GridCatListItemClicked(val selectedIndex:Int): GridCatListEvent
}