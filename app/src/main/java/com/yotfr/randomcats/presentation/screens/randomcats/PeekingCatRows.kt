package com.yotfr.randomcats.presentation.screens.randomcats

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.yotfr.randomcats.presentation.screens.randomcats.model.PeekingCatsLocations

@Composable
fun CatPeekTopRow(
    modifier: Modifier,
    peekingCatPainter: Painter,
    onPeekingCatClicked: () -> Unit,
    peekingCatsLocation: PeekingCatsLocations
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.TOP_LEFT,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.TOP_CENTER,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.TOP_RIGHT,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun CatPeekBottomRow(
    modifier: Modifier,
    peekingCatPainter: Painter,
    onPeekingCatClicked: () -> Unit,
    peekingCatsLocation: PeekingCatsLocations
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.BOTTOM_LEFT,
                enter = slideInVertically { -it } + fadeIn(),
                exit = shrinkVertically { -it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.BOTTOM_CENTER,
                enter = slideInVertically { -it } + fadeIn(),
                exit = shrinkVertically { -it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = peekingCatsLocation == PeekingCatsLocations.BOTTOM_RIGHT,
                enter = slideInVertically { -it } + fadeIn(),
                exit = shrinkVertically { -it } + fadeOut()
            ) {
                Image(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 180f
                        }
                        .clickable {
                            onPeekingCatClicked()
                        }
                        .height(50.dp),
                    painter = peekingCatPainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}