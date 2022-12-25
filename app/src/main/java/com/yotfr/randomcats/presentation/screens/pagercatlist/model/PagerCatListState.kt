package com.yotfr.randomcats.presentation.screens.pagercatlist.model

import android.graphics.Bitmap

data class PagerCatListState(
    val cats: List<PagerCatListModel> = emptyList(),
    val displayDate: String = "",
    val displayTime: String = "",
    val isSystemBarsVisible: Boolean = false,
    val currentlyDisplayedImageBitmap: Bitmap? = null,
    val currentPage: Int = 0
)
