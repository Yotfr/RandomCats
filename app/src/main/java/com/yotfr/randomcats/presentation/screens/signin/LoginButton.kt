package com.yotfr.randomcats.presentation.screens.signin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yotfr.randomcats.presentation.spacing

@Composable
fun LoginButton(
    modifier: Modifier,
    forgotPasswordText: String,
    loginButtonText: String,
    onLoginClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.extraLarge
                ),
            onClick = {
                onLoginClicked()
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
                        text = loginButtonText.uppercase(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        TextButton(
            onClick = {
                onForgotPasswordClicked()
            }
        ) {
            Text(
                text = forgotPasswordText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
