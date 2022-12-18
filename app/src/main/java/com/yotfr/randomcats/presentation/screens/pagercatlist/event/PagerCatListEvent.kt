package com.yotfr.randomcats.presentation.screens.pagercatlist.event

import com.yotfr.randomcats.presentation.screens.pagercatlist.model.PagerCatListModel

sealed interface PagerCatListEvent {
    data class DeleteCatClicked(val cat: PagerCatListModel) : PagerCatListEvent
    data class BackArrowPressed(val selectedIndex: Int) : PagerCatListEvent
}
