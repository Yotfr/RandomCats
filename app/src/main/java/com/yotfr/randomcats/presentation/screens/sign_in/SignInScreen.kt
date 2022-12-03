package com.yotfr.randomcats.presentation.screens.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yotfr.randomcats.R
import com.yotfr.randomcats.presentation.screens.sign_in.event.SignInScreenEvent

@Composable
fun SignInScreen(
    onSignUp: () -> Unit,
    onSignIn: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.event.collect {
            when (it) {
                SignInScreenEvent.NavigateHome -> {
                    onSignIn()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LoginImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            imageResource = painterResource(id = R.drawable.cat_login_screen)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )
        LoginTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            titleText = stringResource(id = R.string.login),
            subTitleText = stringResource(id = R.string.fill_input_below)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )
        InputFields(
            modifier = Modifier.fillMaxWidth(),
            loginLabelText = stringResource(id = R.string.email),
            passwordLabelText = stringResource(id = R.string.password),
            showPasswordText = stringResource(id = R.string.show_password),
            hidePasswordText = stringResource(id = R.string.hide_password)
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
        )
        LoginButton(
            modifier = Modifier.wrapContentSize(),
            forgotPasswordText = stringResource(id = R.string.forgot_password),
            loginButtonText = stringResource(id = R.string.login),
            onLoginClicked = {
                //TODO
            },
            onForgotPasswordClicked = {
                //TODO
            }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        SignUpText(
            modifier = Modifier.fillMaxWidth(),
            signUpText = stringResource(id = R.string.do_not_have_acc),
            signUpButtonText = stringResource(id = R.string.sign_up),
            onSignUpClicked = { onSignUp() }
        )
    }

}

@Composable
fun LoginImage(
    modifier: Modifier,
    imageResource: Painter
) {
    Image(
        modifier = modifier,
        painter = imageResource,
        contentDescription = "",
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun LoginTitle(
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
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        Text(
            modifier = Modifier.alpha(0.7f),
            text = subTitleText,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFields(
    modifier: Modifier,
    loginLabelText: String,
    passwordLabelText: String,
    showPasswordText: String,
    hidePasswordText: String
) {

    var loginText by rememberSaveable {
        mutableStateOf("")
    }
    var passwordText by rememberSaveable {
        mutableStateOf("")
    }

    var passwordHidden by rememberSaveable { mutableStateOf(true) }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = loginText,
            onValueChange = { loginText = it },
            label = { Text(text = loginLabelText) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = loginLabelText) }
        )
        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text(text = passwordLabelText) },
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = passwordLabelText) },
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Outlined.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordHidden) showPasswordText else hidePasswordText
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        )
    }
}

@Composable
fun LoginButton(
    modifier: Modifier,
    forgotPasswordText: String,
    loginButtonText: String,
    onLoginClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 52.dp),
            onClick = {
                onLoginClicked()
            }
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = loginButtonText.uppercase(),
                style = MaterialTheme.typography.titleMedium
            )
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


@Composable
fun SignUpText(
    modifier: Modifier,
    signUpText: String,
    signUpButtonText: String,
    onSignUpClicked: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = signUpText,
            style = MaterialTheme.typography.titleMedium
        )
        TextButton(
            onClick = { onSignUpClicked() }
        ) {
            Text(
                text = signUpButtonText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}