package com.yotfr.randomcats.presentation.screens.randomcats

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.yotfr.randomcats.R

@Composable
fun GifCatPlaceholder(
    context: Context,
    modifier: Modifier
) {
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(data = R.drawable.gif_loading_cat)
                .apply(
                    block = {
                        size(Size.ORIGINAL)
                    }
                )
                .build(),
            imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillWidth,
        colorFilter = ColorFilter.tint(
            color = MaterialTheme.colorScheme.primary
        )
    )
}
