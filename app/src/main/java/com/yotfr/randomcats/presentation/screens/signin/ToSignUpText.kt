package com.yotfr.randomcats.presentation.screens.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun ToSignUpText(
    modifier: Modifier,
    toSignUpText: String,
    toSignUpButtonText: String,
    onSignUpClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.alpha(0.5f),
            color = MaterialTheme.colorScheme.onBackground,
            text = toSignUpText,
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(
            onClick = { onSignUpClicked() }
        ) {
            Text(
                text = toSignUpButtonText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}