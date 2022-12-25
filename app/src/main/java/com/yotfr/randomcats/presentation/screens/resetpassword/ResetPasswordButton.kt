package com.yotfr.randomcats.presentation.screens.resetpassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yotfr.randomcats.presentation.spacing

@Composable
fun ResetPasswordButton(
    onSendClicked: () -> Unit,
    sendButtonText: String,
    isLoading: Boolean
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.spacing.extraLarge
            ),
        onClick = {
            onSendClicked()
        }
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.small)
                .align(Alignment.CenterVertically)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = sendButtonText.uppercase(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}