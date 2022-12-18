package com.yotfr.randomcats.presentation.screens.pagercatlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalPagerTopBar(
    isVisible: Boolean,
    date: String,
    time: String,
    onBackPressed: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(onClick = {
                    onBackPressed()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            title = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(text = date)
                    Text(text = time)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    1.dp
                )
            )
        )
    }
}