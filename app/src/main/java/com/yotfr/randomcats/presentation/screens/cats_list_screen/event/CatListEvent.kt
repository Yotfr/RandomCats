package com.yotfr.randomcats.presentation.screens.cats_list_screen.event

import com.yotfr.randomcats.presentation.screens.cats_list_screen.model.CatListModel

interface CatListEvent {
    data class DeleteCatFromFavorite(val cat: CatListModel):CatListEvent
    data class GridListItemClicked(val selectedIndex:Int):CatListEvent
}