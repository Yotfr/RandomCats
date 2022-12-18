package com.yotfr.randomcats.presentation.screens.randomcats

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest

@Composable
fun CatCard(
    modifier: Modifier,
    catContentDescription: String,
    isLoading: Boolean,
    request: ImageRequest,
    updateBitmap: (bitmap: Bitmap) -> Unit,
    context: Context
) {
    ElevatedCard(
        modifier = modifier
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = request,
            contentDescription = catContentDescription,
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center
        ) {
            val painterState = painter.state
            when {
                painterState is AsyncImagePainter.State.Loading || isLoading -> {
                    GifCatPlaceholder(
                        context = context,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                painterState is AsyncImagePainter.State.Success -> {
                    SubcomposeAsyncImageContent()
                }
                painterState is AsyncImagePainter.State.Error -> {
                }
            }
            SideEffect {
                if (painterState is AsyncImagePainter.State.Success) {
                    val bitmap = (painterState.result.drawable as BitmapDrawable).bitmap
                    updateBitmap(bitmap)
                }
            }
        }
    }
}
