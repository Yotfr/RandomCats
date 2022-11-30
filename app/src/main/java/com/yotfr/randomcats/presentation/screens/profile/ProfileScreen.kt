package com.yotfr.randomcats.presentation.screens.profile

import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.presentation.screens.profile.event.ProfileEvent
import com.yotfr.randomcats.presentation.screens.profile.event.ProfileScreenEvent

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToAuth:() -> Unit
) {

    val event = viewModel.event

    LaunchedEffect(key1 = event) {
        event.collect{
            when(it) {
                ProfileScreenEvent.NavigateToAuth -> {
                    navigateToAuth()
                }
            }
        }
    }

    Button(onClick = {viewModel.onEvent(ProfileEvent.SignOut)}) {

    }


}