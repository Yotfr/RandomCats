package com.yotfr.randomcats.presentation.screens.randomcats.event

sealed interface RandomCatEvent {
    object FavCat : RandomCatEvent
    object GetNewCat : RandomCatEvent
    object ChangePeekingCatLocation : RandomCatEvent
}
