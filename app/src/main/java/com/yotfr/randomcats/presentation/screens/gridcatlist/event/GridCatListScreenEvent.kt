package com.yotfr.randomcats.presentation.screens.gridcatlist.event

sealed interface GridCatListScreenEvent {
    data class NavigateToPagerScreenCat(val selectedIndex:Int): GridCatListScreenEvent
}