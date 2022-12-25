package com.yotfr.randomcats.presentation.screens.settings

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LogOutButton(
    onLogoutClicked: () -> Unit,
    logoutText: String
) {
    TextButton(onClick = {
        onLogoutClicked()
    }) {
        Text(text = logoutText)
    }
}