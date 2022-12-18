package com.yotfr.randomcats.presentation.screens.gridcatlist.event

interface GridCatListEvent {
    data class GridCatListItemClicked(val selectedIndex: Int) : GridCatListEvent
}
