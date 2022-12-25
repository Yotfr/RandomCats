package com.yotfr.randomcats.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.yotfr.randomcats.presentation.spacing

@Composable
fun AccountSection(
    modifier: Modifier,
    userNameText: String,
    emailText: String,
    defaultProfileImagePainter: Painter
) {
    Column(
        modifier = modifier.padding(MaterialTheme.spacing.default),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = defaultProfileImagePainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
        )
        Text(
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.extraSmall),
            text = userNameText,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            modifier = Modifier
                .alpha(0.5f),
            text = emailText,
            style = MaterialTheme.typography.titleSmall
        )
    }
}