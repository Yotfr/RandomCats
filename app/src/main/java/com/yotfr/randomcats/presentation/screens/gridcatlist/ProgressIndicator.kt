package com.yotfr.randomcats.presentation.screens.gridcatlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yotfr.randomcats.presentation.elevation
import com.yotfr.randomcats.presentation.spacing

@Composable
fun ProgressIndicator(
    modifier: Modifier
) {
    Box(
        modifier = modifier.wrapContentSize()
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    elevation = MaterialTheme.elevation.large
                )
            )
            .padding(MaterialTheme.spacing.small)
    ) {
        CircularProgressIndicator()
    }
}
