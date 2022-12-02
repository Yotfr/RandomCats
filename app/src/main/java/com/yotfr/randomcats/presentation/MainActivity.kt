package com.yotfr.randomcats.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.yotfr.randomcats.presentation.navigation.root.RootNavigationGraph
import com.yotfr.randomcats.presentation.theme.RandomCatsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomCatsTheme() {
                RootNavigationGraph(navController = rememberNavController())
            }
        }
    }
}




