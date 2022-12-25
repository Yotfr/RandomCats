package com.yotfr.randomcats.presentation.screens.settingstheme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yotfr.randomcats.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsThemeTopBar(
    onArrowBackPressed: () -> Unit,
    topBarTitle: String
) {
    TopAppBar(
        title = {
            Text(text = topBarTitle)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onArrowBackPressed()
                }
            ) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(id = R.string.to_sign_in)
                )
            }
        },
        windowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
    )
}