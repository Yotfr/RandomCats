package com.yotfr.randomcats.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsEvent
import com.yotfr.randomcats.presentation.screens.settings.event.SettingsScreenEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToAuth: () -> Unit,
    navigateToThemeScreen: () -> Unit,
    goBack: () -> Unit
) {
    val uiEvent = viewModel.uiEvent
    val state by viewModel.state.collectAsState()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    Scaffold(
        topBar = {
            TopBar(
                onArrowBackPressed = {
                    goBack()
                },
                topBarTitle = stringResource(id = R.string.settings)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
        ) {
            AccountSection(
                modifier = Modifier.fillMaxWidth(),
                userNameText = state.userName,
                emailText = state.email,
                defaultProfileImagePainter = painterResource(id = R.drawable.cat_log)
            )
            AppearanceSection(
                modifier = Modifier.fillMaxWidth(),
                appearanceTitleText = stringResource(id = R.string.appearance),
                onThemeClicked = { viewModel.onEvent(SettingsEvent.ThemePressed) },
                themeText = stringResource(id = R.string.theme),
                themeIcon = Icons.Outlined.Palette,
                chevronForwardIcon = Icons.Outlined.ChevronRight
            )
            LogOutButton(
                onLogoutClicked = {
                    viewModel.onEvent(SettingsEvent.SignOut)
                },
                logoutText = stringResource(id = R.string.sign_out)
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEvent.collect {
                when (it) {
                    SettingsScreenEvent.NavigateToAuth -> {
                        navigateToAuth()
                    }
                    SettingsScreenEvent.NavigateToThemeScreen -> {
                        navigateToThemeScreen()
                    }
                }
            }
        }
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
        },
        windowInsets = WindowInsets.systemBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
    )
}

@Composable
fun AccountSection(
    modifier: Modifier,
    userNameText: String,
    emailText: String,
    defaultProfileImagePainter: Painter
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = defaultProfileImagePainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )
        Text(
            modifier = Modifier
                .padding(vertical = 4.dp),
            text = userNameText,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier
                .alpha(0.5f),
            text = emailText,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun AppearanceSection(
    modifier: Modifier,
    appearanceTitleText: String,
    onThemeClicked: () -> Unit,
    themeText: String,
    themeIcon: ImageVector,
    chevronForwardIcon: ImageVector
) {
    Column(modifier = modifier) {
        ElevatedCard(
            shape = RectangleShape,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    2.dp
                )
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                text = appearanceTitleText,
                style = MaterialTheme.typography.titleSmall
            )
        }
        ElevatedCard(
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onThemeClicked()
                    }
                    .padding(horizontal = 8.dp, vertical = 16.dp)
            ) {
                Icon(
                    imageVector = themeIcon,
                    contentDescription = themeText
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = themeText,
                    style = MaterialTheme.typography.titleSmall
                )
                Icon(
                    imageVector = chevronForwardIcon,
                    contentDescription = themeText
                )
            }
        }
    }
}

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
