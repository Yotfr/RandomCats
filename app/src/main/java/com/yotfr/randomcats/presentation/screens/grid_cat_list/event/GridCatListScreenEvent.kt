package com.yotfr.randomcats.presentation.screens.grid_cat_list.event

sealed interface GridCatListScreenEvent {
    data class NavigateToPagerScreenCat(val selectedIndex:Int): GridCatListScreenEvent
}