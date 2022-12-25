package com.yotfr.randomcats.presentation.screens.settingstheme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.yotfr.randomcats.domain.model.Theme
import com.yotfr.randomcats.presentation.spacing

@Composable
fun ThemePickerCard(
    modifier: Modifier,
    currentTheme: Theme,
    onThemeSelected: (theme: Theme) -> Unit,
    lightThemeText: String,
    darkThemeText: String,
    systemDefaultThemeText: String,
    selectedThemeIcon: ImageVector
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onThemeSelected(Theme.LIGHT)
                }
                .padding(MaterialTheme.spacing.default),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = lightThemeText)
            AnimatedVisibility(
                visible = currentTheme == Theme.LIGHT,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = selectedThemeIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onThemeSelected(Theme.DARK)
                }
                .padding(MaterialTheme.spacing.default),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = darkThemeText)
            AnimatedVisibility(
                visible = currentTheme == Theme.DARK,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = selectedThemeIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onThemeSelected(Theme.SYSTEM_DEFAULT)
                }
                .padding(MaterialTheme.spacing.default),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = systemDefaultThemeText)
            AnimatedVisibility(
                visible = currentTheme == Theme.SYSTEM_DEFAULT,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = selectedThemeIcon,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}