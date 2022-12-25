package com.yotfr.randomcats.presentation.screens.randomcats.event

sealed interface RandomCatEvent {
    object FavoriteIconClicked : RandomCatEvent
    object LoadNewButtonClicked : RandomCatEvent
    object PeekingCatClicked : RandomCatEvent
}
