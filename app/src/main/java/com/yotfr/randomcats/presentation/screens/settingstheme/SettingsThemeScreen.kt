package com.yotfr.randomcats.presentation.screens.settingstheme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.R
import com.yotfr.randomcats.domain.model.Theme
import com.yotfr.randomcats.presentation.screens.settingstheme.event.SettingsThemeEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsThemeScreen(
    goBack: () -> Unit,
    viewModel: SettingsThemeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                onArrowBackPressed = { goBack() },
                topBarTitle = stringResource(id = R.string.theme)
            )
        }
    ) {
        ThemePickerCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            currentTheme = state.currentTheme,
            onThemeSelected = { theme ->
                viewModel.onEvent(
                    SettingsThemeEvent.ThemeChanged(
                        theme = theme
                    )
                )
            },
            lightThemeText = stringResource(id = R.string.light),
            darkThemeText = stringResource(id = R.string.dark),
            systemDefaultThemeText = stringResource(id = R.string.system_default),
            selectedThemeIcon = Icons.Outlined.CheckCircle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
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
        }
    )
}

@Composable
fun ThemePickerCard(
    modifier: Modifier,
    currentTheme: Theme,
    onThemeSelected: (theme: Theme) -> Unit,
    lightThemeText: String,
    darkThemeText: String,
    systemDefaultThemeText: String,
    selectedThemeIcon: ImageVector
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onThemeSelected(Theme.LIGHT)
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = lightThemeText)
            AnimatedVisibility(
                visible = currentTheme == Theme.LIGHT,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = selectedThemeIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onThemeSelected(Theme.DARK)
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = darkThemeText)
            AnimatedVisibility(
                visible = currentTheme == Theme.DARK,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = selectedThemeIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onThemeSelected(Theme.SYSTEM_DEFAULT)
                }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = systemDefaultThemeText)
            AnimatedVisibility(
                visible = currentTheme == Theme.SYSTEM_DEFAULT,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = selectedThemeIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
