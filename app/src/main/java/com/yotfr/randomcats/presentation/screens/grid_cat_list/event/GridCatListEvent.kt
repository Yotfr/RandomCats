package com.yotfr.randomcats.presentation.screens.grid_cat_list.event

import com.yotfr.randomcats.presentation.screens.grid_cat_list.model.GridCatListModel

interface GridCatListEvent {
    data class DeleteCatFromFavorite(val cat: GridCatListModel): GridCatListEvent
    data class GridCatListItemClicked(val selectedIndex:Int): GridCatListEvent
}