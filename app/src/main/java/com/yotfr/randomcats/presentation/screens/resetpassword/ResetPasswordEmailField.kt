package com.yotfr.randomcats.presentation.screens.resetpassword

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordEmailField(
    isEmailEmptyError: Boolean,
    emailLabelText: String,
    modifier: Modifier,
    onEmailTextChanged: (text: String) -> Unit,
    emailText: String,
    emptyEmailFieldErrorMessage: String,
    isEmailInvalidError: Boolean,
    invalidEmailErrorMessage: String,
    isNoUserWithThisEmailError:Boolean
) {
    var emailTextFocused by remember { mutableStateOf(false) }

    TextField(
        modifier = modifier
            .onFocusChanged {
                emailTextFocused = it.isFocused
            },
        colors = TextFieldDefaults.textFieldColors(
            textColor = if (isEmailEmptyError || isEmailInvalidError || isNoUserWithThisEmailError) {
                MaterialTheme.colorScheme.error
            } else MaterialTheme.colorScheme.onBackground,
            containerColor = if (isEmailEmptyError || isEmailInvalidError || isNoUserWithThisEmailError) {
                MaterialTheme.colorScheme.error.copy(
                    alpha = 0.12f
                )
            } else if (emailTextFocused) {
                MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = 0.3f
                )
            } else Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.7f
            ),
            focusedLeadingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.7f
            ),
            unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.7f
            ),
            focusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(
                alpha = 0.7f
            ),
            errorLeadingIconColor = MaterialTheme.colorScheme.error,
            errorIndicatorColor = Color.Transparent
        ),
        value = emailText,
        onValueChange = { onEmailTextChanged(it) },
        label = {
            Text(
                text = emailLabelText,
                style = if (emailTextFocused) MaterialTheme.typography.bodyMedium
                else MaterialTheme.typography.bodyLarge
            )
        },
        isError = isEmailEmptyError || isEmailInvalidError || isNoUserWithThisEmailError,
        singleLine = true,
        supportingText = {
            Row(
                modifier = Modifier.padding(
                    start = 52.dp
                )
            ) {
                if (isEmailEmptyError) {
                    Text(text = emptyEmailFieldErrorMessage)
                }
                if (isEmailInvalidError) {
                    Text(text = invalidEmailErrorMessage)
                }
            }
        },
        leadingIcon = {
            Row(
                modifier = Modifier.padding(
                    start = 32.dp,
                    end = 8.dp
                )
            ) {
                Icon(Icons.Outlined.Email, contentDescription = emailLabelText)
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}