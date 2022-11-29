package com.yotfr.randomcats.presentation.screens.random_cat_screen.event

sealed interface RandomCatEvent{
    object FavCat:RandomCatEvent
    object GetNewCat:RandomCatEvent
}