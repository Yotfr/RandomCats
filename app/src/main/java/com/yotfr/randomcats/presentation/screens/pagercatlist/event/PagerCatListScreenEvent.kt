package com.yotfr.randomcats.presentation.screens.pagercatlist.event

sealed interface PagerCatListScreenEvent{
    data class NavigateToGridCatList(val selectedIndex:Int):PagerCatListScreenEvent
}