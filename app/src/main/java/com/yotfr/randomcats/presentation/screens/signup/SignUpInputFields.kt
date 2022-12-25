package com.yotfr.randomcats.presentation.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
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
fun SignUpInputFields(
    modifier: Modifier,
    emailLabelText: String,
    passwordLabelText: String,
    confirmPasswordLabelText: String,
    showPasswordText: String,
    hidePasswordText: String,
    emailText: String,
    passwordText: String,
    confirmPasswordText: String,
    onEmailTextChanged: (text: String) -> Unit,
    onPasswordTextChanged: (text: String) -> Unit,
    onConfirmPasswordTextChanged: (text: String) -> Unit,
    isEmailEmptyError: Boolean,
    isPasswordEmptyError: Boolean,
    isEmailInvalidError: Boolean,
    isUserAlreadyExistsError: Boolean,
    isPasswordsNotMatchError: Boolean,
    isPasswordTooShortError: Boolean,
    emptyEmailFieldErrorMessage: String,
    emptyPasswordFieldErrorMessage: String,
    invalidEmailErrorMessage: String,
    passwordTooShortErrorMessage: String,
    passwordsNotMatchErrorMessage: String,
    usernameText: String,
    usernameLabelText: String,
    isUserNameEmptyError: Boolean,
    userNameEmptyErrorMessage: String,
    onUserNameTextChanged: (text: String) -> Unit
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    var confirmPasswordHidden by rememberSaveable { mutableStateOf(true) }

    var emailTextFocused by remember { mutableStateOf(false) }
    var passwordTextFocused by remember { mutableStateOf(false) }
    var confirmPasswordTextFocused by remember { mutableStateOf(false) }
    var userNameTextFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    userNameTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isUserNameEmptyError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isUserNameEmptyError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error.copy(
                        alpha = 0.12f
                    )
                } else if (userNameTextFocused) {
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
            value = usernameText,
            onValueChange = { onUserNameTextChanged(it) },
            label = {
                Text(
                    text = usernameLabelText,
                    style = if (userNameTextFocused) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodyLarge
                )
            },
            isError = isUserNameEmptyError || isUserAlreadyExistsError,
            singleLine = true,
            supportingText = {
                Row(
                    modifier = Modifier.padding(
                        start = 52.dp
                    )
                ) {
                    if (isUserNameEmptyError) {
                        Text(text = userNameEmptyErrorMessage)
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
                    Icon(Icons.Outlined.Person, contentDescription = usernameLabelText)
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
                    emailTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isEmailEmptyError || isEmailInvalidError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isEmailEmptyError || isEmailInvalidError || isUserAlreadyExistsError) {
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
            isError = isEmailEmptyError || isEmailInvalidError || isUserAlreadyExistsError,
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
                textColor = if (isPasswordEmptyError || isPasswordTooShortError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isPasswordEmptyError || isPasswordTooShortError || isUserAlreadyExistsError) {
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
            isError = isPasswordEmptyError || isPasswordTooShortError || isUserAlreadyExistsError,
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
                        start = 52.dp
                    )
                ) {
                    if (isPasswordEmptyError) {
                        Text(text = emptyPasswordFieldErrorMessage)
                    }
                    if (isPasswordTooShortError) {
                        Text(text = passwordTooShortErrorMessage)
                    }
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
                    confirmPasswordTextFocused = it.isFocused
                },
            colors = TextFieldDefaults.textFieldColors(
                textColor = if (isPasswordsNotMatchError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error
                } else MaterialTheme.colorScheme.onBackground,
                containerColor = if (isPasswordsNotMatchError || isUserAlreadyExistsError) {
                    MaterialTheme.colorScheme.error.copy(
                        alpha = 0.12f
                    )
                } else if (confirmPasswordTextFocused) {
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
            value = confirmPasswordText,
            onValueChange = { onConfirmPasswordTextChanged(it) },
            label = {
                Text(
                    text = confirmPasswordLabelText,
                    style = if (confirmPasswordTextFocused) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.bodyLarge
                )
            },
            isError = isPasswordsNotMatchError || isUserAlreadyExistsError,
            visualTransformation =
            if (confirmPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = Modifier.padding(
                        start = MaterialTheme.spacing.large,
                        end = MaterialTheme.spacing.small
                    )
                ) {
                    Icon(Icons.Outlined.Lock, contentDescription = confirmPasswordLabelText)
                }
            },
            trailingIcon = {
                if (confirmPasswordText.isNotBlank()) {
                    Row(
                        modifier = Modifier.padding(
                            end = MaterialTheme.spacing.large,
                            start = MaterialTheme.spacing.small
                        )
                    ) {
                        IconButton(onClick = { confirmPasswordHidden = !confirmPasswordHidden }) {
                            val visibilityIcon =
                                if (confirmPasswordHidden) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                            val description =
                                if (confirmPasswordHidden) showPasswordText else hidePasswordText
                            Icon(imageVector = visibilityIcon, contentDescription = description)
                        }
                    }
                }
            },
            supportingText = {
                Row(
                    modifier = Modifier.padding(
                        start = 52.dp
                    )
                ) {
                    if (isPasswordsNotMatchError) {
                        Text(text = passwordsNotMatchErrorMessage)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
    }
}