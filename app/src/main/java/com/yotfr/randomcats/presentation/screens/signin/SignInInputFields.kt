package com.yotfr.randomcats.presentation.screens.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.yotfr.randomcats.presentation.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInInputFields(
    modifier: Modifier,
    emailLabelText: String,
    passwordLabelText: String,
    showPasswordText: String,
    hidePasswordText: String,
    emailText: String,
    passwordText: String,
    onEmailTextChanged: (text: String) -> Unit,
    onPasswordTextChanged: (text: String) -> Unit,
    isEmailEmptyError: Boolean,
    isPasswordEmptyError: Boolean,
    isEmailInvalidError: Boolean,
    isInvalidCredentialsError: Boolean,
    emptyEmailFieldErrorMessage: String,
    emptyPasswordFieldErrorMessage: String,
    invalidEmailErrorMessage: String
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        var emailTextFocused by remember { mutableStateOf(false) }
        var passwordTextFocused by remember { mutableStateOf(false) }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    emailTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isEmailEmptyError || isEmailInvalidError || isInvalidCredentialsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isEmailEmptyError || isEmailInvalidError || isInvalidCredentialsError) {
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
            isError = isEmailEmptyError || isEmailInvalidError || isInvalidCredentialsError,
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
                        start = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.small
                    )
                ) {
                    Icon(Icons.Outlined.Email, contentDescription = emailLabelText)
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    passwordTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isPasswordEmptyError || isInvalidCredentialsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isPasswordEmptyError || isInvalidCredentialsError) {
                    MaterialTheme.colorScheme.error.copy(
                        alpha = 0.12f
                    )
                } else if (passwordTextFocused) {
                    MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = 0.3f
                    )
                } else Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
                focusedTrailingIconColor = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = 0.7f
                ),
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
            value = passwordText,
            onValueChange = { onPasswordTextChanged(it) },
            label = {
                Text(
                    text = passwordLabelText,
                    style = if (passwordTextFocused) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodyLarge
                )
            },
            isError = isPasswordEmptyError || isInvalidCredentialsError,
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(
                        start = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.small
                    )
                ) {
                    Icon(Icons.Outlined.Lock, contentDescription = passwordLabelText)
                }
            },
            trailingIcon = {
                if (passwordText.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(
                            end = MaterialTheme.spacing.large,
                            start = MaterialTheme.spacing.small
                        )
                    ) {
                        IconButton(onClick = { passwordHidden = !passwordHidden }) {
                            val visibilityIcon =
                                if (passwordHidden) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                            val description =
                                if (passwordHidden) showPasswordText else hidePasswordText
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    }
                }
            },
            supportingText = {
                Row(
                    modifier = Modifier.padding(
                        start = MaterialTheme.spacing.large
                    )
                ) {
                    if (isPasswordEmptyError) {
                        Text(text = emptyPasswordFieldErrorMessage)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}