package com.yotfr.randomcats.presentation.screens.random_cat_screen.model

import com.yotfr.randomcats.domain.model.Cat

data class RandomCatState(
    val isCatLoading: Boolean = false,
    val cat: Cat? = null,
    val error: String = "",
    val peekingCatsLocation: PeekingCatsLocations = PeekingCatsLocations.BOTTOM_RIGHT,
    val isCatUploading: Boolean = false
)