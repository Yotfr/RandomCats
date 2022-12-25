package com.yotfr.randomcats.presentation.screens.pagercatlist.event

import android.graphics.Bitmap
import com.yotfr.randomcats.presentation.screens.pagercatlist.model.PagerCatListModel

sealed interface PagerCatListEvent {
    data class DeleteCatClicked(val cat: PagerCatListModel) : PagerCatListEvent
    data class BackArrowPressed(val selectedIndex: Int) : PagerCatListEvent
    data class PageChanged(val pageIndex: Int) : PagerCatListEvent
    data class ChangeBitmap(val bitmap: Bitmap) : PagerCatListEvent
    object OnScreenClicked : PagerCatListEvent
}
