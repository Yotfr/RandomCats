package com.yotfr.randomcats.presentation.screens.cats_list_screen.model

import com.yotfr.randomcats.domain.model.Cat

data class CatListState(
    val groupedCats:Map<String,List<Cat>> = emptyMap()
)