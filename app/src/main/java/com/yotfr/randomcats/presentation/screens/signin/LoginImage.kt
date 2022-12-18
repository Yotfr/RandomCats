package com.yotfr.randomcats.presentation.screens.signin

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun LoginImage(
    modifier: Modifier,
    imageResource: Painter
) {
    Image(
        modifier = modifier,
        painter = imageResource,
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}