package com.yotfr.randomcats.presentation.screens.settings

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsEvent
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsScreenEvent

@Composable
fun ProfileScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToAuth:() -> Unit
) {

    val event = viewModel.event

    LaunchedEffect(key1 = event) {
        event.collect{
            when(it) {
                SettingsScreenEvent.NavigateToAuth -> {
                    navigateToAuth()
                }
            }
        }
    }

    Button(onClick = {viewModel.onEvent(SettingsEvent.SignOut)}) {

    }


}