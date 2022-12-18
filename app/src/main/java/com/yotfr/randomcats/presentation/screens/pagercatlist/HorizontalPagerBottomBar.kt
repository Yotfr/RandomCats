package com.yotfr.randomcats.presentation.screens.pagercatlist

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yotfr.randomcats.R

@Composable
fun HorizontalPagerBottomBar(
    isVisible: Boolean,
    onShareClicked: () -> Unit,
    onDownloadClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        BottomAppBar(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = {
                        Log.d("TEST", "clicked")
                        onDownloadClicked()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = stringResource(id = R.string.save_to_gallery)
                    )
                }
                IconButton(
                    onClick = { onShareClicked() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = stringResource(id = R.string.share)
                    )
                }
                IconButton(
                    onClick = {
                        Log.d("ERRTEST", "clicked")
                        onDeleteClicked()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_favorite_minus),
                        contentDescription = stringResource(id = R.string.delete_from_favorite)
                    )
                }
            }
        }
    }
}