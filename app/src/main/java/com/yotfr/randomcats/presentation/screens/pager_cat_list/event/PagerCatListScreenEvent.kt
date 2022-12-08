package com.yotfr.randomcats.presentation.screens.pager_cat_list.event

sealed interface PagerCatListScreenEvent{
    data class NavigateToGridCatList(val selectedIndex:Int):PagerCatListScreenEvent
}