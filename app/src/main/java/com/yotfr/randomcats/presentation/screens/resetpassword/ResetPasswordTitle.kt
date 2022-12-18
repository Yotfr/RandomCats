package com.yotfr.randomcats.presentation.screens.resetpassword

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun ResetPasswordTitle(
    modifier: Modifier,
    titleText: String,
    subTitleText: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = titleText,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        Text(
            modifier = Modifier.alpha(0.5f),
            text = subTitleText,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium
        )
    }
}