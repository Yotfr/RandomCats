package com.yotfr.randomcats.presentation.screens.cats_list_screen.model

data class CatListState(
    val groupedCats:List<CatListModel> = emptyList(),
    val selectedIndex:Int = 0
)