package com.yotfr.randomcats.presentation.screens.randomcats.model

import com.yotfr.randomcats.domain.model.Cat

data class RandomCatState(
    val isCatLoading: Boolean = false,
    val cat: Cat? = null,
    val error: String = "",
    val peekingCatsLocation: PeekingCatsLocations = PeekingCatsLocations.BOTTOM_RIGHT,
    val isCatUploading: Boolean = false,
    val isButtonsEnabled: Boolean = false
)
