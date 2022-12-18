package com.yotfr.randomcats.presentation.screens.gridcatlist.model

data class GridCatListState(
    val isLoading: Boolean = false,
    val cats: List<GridCatListModel> = emptyList()
)
