package com.yotfr.randomcats.presentation.screens.gridcatlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder

@Composable
fun GridCell(
    onGridClicked: () -> Unit,
    catContentDescription: String,
    request: ImageRequest
) {
    var isLoading by remember {
        mutableStateOf(true)
    }

    SubcomposeAsyncImage(
        modifier = Modifier.fillMaxWidth()
            .placeholder(
                visible = isLoading,
                color = MaterialTheme.colorScheme.surfaceVariant,
                highlight = PlaceholderHighlight.fade(
                    highlightColor = MaterialTheme.colorScheme.surface
                )
            ),
        model = request,
        contentDescription = catContentDescription,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.Center
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                isLoading = true
            }
            is AsyncImagePainter.State.Success -> {
                isLoading = false
                SubcomposeAsyncImageContent(
                    modifier = Modifier.clickable {
                        onGridClicked()
                    }
                )
            }
            else -> {}
        }
    }
}
