package com.julia.imp.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.team.Team
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.app_name
import imp.composeapp.generated.resources.create_account_label
import imp.composeapp.generated.resources.email_label
import imp.composeapp.generated.resources.login_error_message
import imp.composeapp.generated.resources.login_error_title
import imp.composeapp.generated.resources.ok_label
import imp.composeapp.generated.resources.password_label
import imp.composeapp.generated.resources.sign_in_label
import imp.composeapp.generated.resources.welcome_to_format
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    onLoggedIn: (Team) -> Unit,
    viewModel: LoginViewModel = viewModel { LoginViewModel() }
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold {
        val scrollState = rememberScrollState()

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            val compact = maxWidth < 600.dp
            val maxFormWidth = if (compact) Dp.Unspecified else 480.dp

            Column(
                modifier = Modifier
                    .widthIn(max = maxFormWidth)
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (compact) Spacer(Modifier.weight(1f))

                LoginFormTitle(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                )

                LoginFormFields(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                    email = uiState.email,
                    password = uiState.password,
                    onEmailChange = { viewModel.setEmail(it) },
                    onPasswordChange = { viewModel.setPassword(it) },
                    enabled = !uiState.isLoggingIn
                )

                if (compact) Spacer(Modifier.weight(1f))

                LoginFormButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
                    compact = compact,
                    signingIn = uiState.isLoggingIn,
                    onLoginClick = { viewModel.login() },
                    onRegisterClick = {}
                )
            }
        }
    }

    if (uiState.showError) {
        LoginErrorDialog(onDismissRequest = { viewModel.dismissError() })
    }

    LaunchedEffect(uiState.loggedTeam) {
        uiState.loggedTeam?.let { onLoggedIn(it) }
    }
}

@Composable
fun LoginErrorDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        icon = { Icon(Icons.Outlined.Warning, null) },
        title = { Text(text = stringResource(Res.string.login_error_title)) },
        text = { Text(text = stringResource(Res.string.login_error_message)) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.ok_label))
            }
        }
    )
}

@Composable
fun LoginFormTitle(modifier: Modifier = Modifier) {
    val appName = stringResource(Res.string.app_name)
    val title = stringResource(Res.string.welcome_to_format, appName)

    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append(title)

            addStyle(
                style = SpanStyle(color = MaterialTheme.colorScheme.primary),
                start = title.indexOf(appName),
                end = title.indexOf(appName) + appName.length
            )
        },
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LoginFormFields(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean
) {
    Column(modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailChange,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(Res.string.email_label)) }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = password,
            onValueChange = onPasswordChange,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(Res.string.password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}

@Composable
fun LoginFormButtons(
    compact: Boolean,
    signingIn: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (compact) {
        Column(modifier) {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRegisterClick,
                enabled = !signingIn
            ) {
                Text(stringResource(Res.string.create_account_label))
            }

            Spacer(Modifier.height(4.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLoginClick,
                enabled = !signingIn
            ) {
                if (signingIn) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(Res.string.sign_in_label))
                }
            }
        }
    } else {
        Row(modifier) {
            FilledTonalButton(
                modifier = Modifier.weight(1f),
                onClick = onRegisterClick,
                enabled = !signingIn
            ) {
                Text(stringResource(Res.string.create_account_label))
            }

            Spacer(Modifier.width(16.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onLoginClick,
                enabled = !signingIn
            ) {
                if (signingIn) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(Res.string.sign_in_label))
                }
            }
        }
    }
}