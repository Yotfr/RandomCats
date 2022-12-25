package com.yotfr.randomcats.presentation

import com.yotfr.randomcats.domain.model.Theme
import com.yotfr.randomcats.presentation.navigation.root.RootGraph

data class MainActivityState(
    val isLoading: Boolean = true,
    val startDestinationRoute: String = RootGraph.AUTH,
    val theme: Theme = Theme.SYSTEM_DEFAULT
)
