package com.yotfr.randomcats.presentation.navigation.home.cat_list

const val DETAILS_SELECTED_INDEX_KEY = "SELECTED_INDEX"

sealed class CatListScreenRoute(val screen_route: String) {
    object Details : CatListScreenRoute("details/{$DETAILS_SELECTED_INDEX_KEY}") {
        fun passSelectedIndex(selectedIndex:Int):String {
            return this.screen_route.replace(oldValue = "{$DETAILS_SELECTED_INDEX_KEY}",
            newValue = selectedIndex.toString())
        }
    }
}