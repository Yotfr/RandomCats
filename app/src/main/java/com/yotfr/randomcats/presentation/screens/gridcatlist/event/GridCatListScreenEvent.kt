package com.yotfr.randomcats.presentation.screens.gridcatlist.event

sealed interface GridCatListScreenEvent {
    data class NavigateToPagerScreen(val selectedIndex: Int) : GridCatListScreenEvent
    object NavigateToAuth : GridCatListScreenEvent
}
