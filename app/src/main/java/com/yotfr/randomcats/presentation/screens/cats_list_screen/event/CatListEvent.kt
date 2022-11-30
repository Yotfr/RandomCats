package com.yotfr.randomcats.presentation.screens.cats_list_screen.event

import com.yotfr.randomcats.domain.model.Cat

interface CatListEvent {
    data class DeleteCatFromFavorite(val cat: Cat):CatListEvent
}