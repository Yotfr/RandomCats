package com.yotfr.randomcats.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import com.yotfr.randomcats.presentation.elevation
import com.yotfr.randomcats.presentation.spacing

@Composable
fun AppearanceSection(
    modifier: Modifier,
    appearanceTitleText: String,
    onThemeClicked: () -> Unit,
    themeText: String,
    themeIcon: ImageVector,
    chevronForwardIcon: ImageVector
) {
    Column(modifier = modifier) {
        ElevatedCard(
            shape = RectangleShape,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    MaterialTheme.elevation.small
                )
            )
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(
                        horizontal = MaterialTheme.spacing.small,
                        vertical = MaterialTheme.spacing.small
                    ),
                text = appearanceTitleText,
                style = MaterialTheme.typography.titleSmall
            )
        }
        ElevatedCard(
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onThemeClicked()
                    }
                    .padding(
                        horizontal = MaterialTheme.spacing.small,
                        vertical = MaterialTheme.spacing.medium
                    )
            ) {
                Icon(
                    imageVector = themeIcon,
                    contentDescription = themeText
                )
                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                Text(
                    modifier = Modifier.weight(1f),
                    text = themeText,
                    style = MaterialTheme.typography.titleSmall
                )
                Icon(
                    imageVector = chevronForwardIcon,
                    contentDescription = themeText
                )
            }
        }
    }
}
