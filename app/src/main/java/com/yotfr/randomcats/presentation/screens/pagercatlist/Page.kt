package com.yotfr.randomcats.presentation.screens.pagercatlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.yotfr.randomcats.presentation.spacing

@Composable
fun Page(
    modifier: Modifier,
    catContentDescription: String,
    loadingPlaceholderPainter: Painter,
    request: ImageRequest,
    updateBitmap: (bitmap: Bitmap) -> Unit
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = request,
        contentDescription = catContentDescription,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.Center
    ) {
        val painterState = painter.state
        if (painterState is AsyncImagePainter.State.Loading ||
            painterState is AsyncImagePainter.State.Error
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.extraLarge)
                    .alpha(0.5f),
                painter = loadingPlaceholderPainter,
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(
                    color = Color.Gray
                )
            )
        } else {
            SubcomposeAsyncImageContent()
        }
        SideEffect {
            if (painterState is AsyncImagePainter.State.Success) {
                val bitmap = (painterState.result.drawable as BitmapDrawable).bitmap
                updateBitmap(bitmap)
            }
        }
    }
}