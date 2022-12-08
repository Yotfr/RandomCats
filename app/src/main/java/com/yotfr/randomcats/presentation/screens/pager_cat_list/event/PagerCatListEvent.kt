package com.yotfr.randomcats.presentation.screens.pager_cat_list.event

import com.yotfr.randomcats.presentation.screens.pager_cat_list.model.PagerCatListModel

sealed interface PagerCatListEvent{
    data class DeleteCatFromFavorite(val cat: PagerCatListModel): PagerCatListEvent
    data class BackArrowPressed(val selectedIndex:Int):PagerCatListEvent
}