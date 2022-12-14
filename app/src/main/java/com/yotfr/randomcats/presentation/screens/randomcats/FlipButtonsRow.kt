package com.yotfr.randomcats.presentation.screens.randomcats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun FlipButtonRow(
    onFavouriteClicked: () -> Unit,
    onRefreshClicked: () -> Unit,
    onSaveGalleryClicked: () -> Unit,
    onShareClicked: () -> Unit,
    refreshIcon: ImageVector,
    favouriteIcon: ImageVector,
    saveGalleryIcon: ImageVector,
    shareIcon: ImageVector,
    flipButtonsIcon: ImageVector,
    refreshIconDescription: String,
    favouriteIconDescription: String,
    saveGalleryIconDescription: String,
    shareIconDescription: String,
    flipButtonsIconDescription: String,
    isButtonsEnabled: Boolean,
    modifier: Modifier
) {
    var flipState by remember {
        mutableStateOf(ButtonFace.Front)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FlipButton(
            buttonFace = flipState,
            onClick = { buttonFace ->
                when (buttonFace) {
                    ButtonFace.Front -> {
                        onRefreshClicked()
                    }
                    ButtonFace.Back -> {
                        onSaveGalleryClicked()
                    }
                }
            },
            backIcon = saveGalleryIcon,
            backIconDescription = saveGalleryIconDescription,
            frontIcon = refreshIcon,
            frontIconDescription = refreshIconDescription,
            isButtonEnabled = isButtonsEnabled
        )
        IconButton(
            onClick = { flipState = flipState.next }
        ) {
            Icon(
                flipButtonsIcon,
                contentDescription = flipButtonsIconDescription,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        FlipButton(
            buttonFace = flipState,
            onClick = { buttonFace ->
                when (buttonFace) {
                    ButtonFace.Front -> {
                        onFavouriteClicked()
                    }
                    ButtonFace.Back -> {
                        onShareClicked()
                    }
                }
            },
            backIcon = shareIcon,
            backIconDescription = shareIconDescription,
            frontIcon = favouriteIcon,
            frontIconDescription = favouriteIconDescription,
            isButtonEnabled = isButtonsEnabled
        )
    }
}