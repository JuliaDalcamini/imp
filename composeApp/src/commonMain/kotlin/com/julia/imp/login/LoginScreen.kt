package com.julia.imp.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.julia.imp.common.session.UserSession
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.button.SecondaryButton
import com.julia.imp.common.ui.dialog.ErrorDialog
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.app_name
import imp.composeapp.generated.resources.create_account_label
import imp.composeapp.generated.resources.email_label
import imp.composeapp.generated.resources.login_error_message
import imp.composeapp.generated.resources.login_error_title
import imp.composeapp.generated.resources.login_subtitle
import imp.composeapp.generated.resources.password_label
import imp.composeapp.generated.resources.sign_in_label
import imp.composeapp.generated.resources.welcome_to_format
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    presetEmail: String? = null,
    presetPassword: String? = null,
    onSignIn: (UserSession) -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = viewModel { LoginViewModel() }
) {
    LaunchedEffect(Unit) {
        presetEmail?.let { viewModel.setEmail(it) }
        presetPassword?.let { viewModel.setPassword(it) }
    }

    LaunchedEffect(viewModel.uiState.newSession) {
        viewModel.uiState.newSession?.let { onSignIn(it) }
    }

    Scaffold(Modifier.imePadding()) { paddingValues ->
        val scrollState = rememberScrollState()

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val compact = maxWidth < 480.dp
            val maxFormWidth = if (compact) Dp.Unspecified else 480.dp

            Column(
                modifier = Modifier
                    .widthIn(max = maxFormWidth)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .consumeWindowInsets(PaddingValues(horizontal = 24.dp))
                    .consumeWindowInsets(paddingValues)
                    .padding(horizontal = 24.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (compact) Spacer(Modifier.weight(1f))

                LoginFormTitle(Modifier.padding(top = 24.dp))

                LoginFormFields(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                    email = viewModel.uiState.email,
                    password = viewModel.uiState.password,
                    onEmailChange = { viewModel.setEmail(it) },
                    onPasswordChange = { viewModel.setPassword(it) },
                    enabled = !viewModel.uiState.loading
                )

                if (compact) Spacer(Modifier.weight(1f))

                LoginFormButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    compact = compact,
                    loading = viewModel.uiState.loading,
                    onLoginClick = { viewModel.login() },
                    onRegisterClick = onRegisterClick
                )
            }
        }
    }

    if (viewModel.uiState.showError) {
        LoginErrorDialog(onDismissRequest = { viewModel.dismissError() })
    }
}

@Composable
fun LoginErrorDialog(onDismissRequest: () -> Unit) {
    ErrorDialog(
        title = stringResource(Res.string.login_error_title),
        message = stringResource(Res.string.login_error_message),
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun LoginFormTitle(modifier: Modifier = Modifier) {
    val appName = stringResource(Res.string.app_name)
    val title = stringResource(Res.string.welcome_to_format, appName)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
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

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.login_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
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
    loading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (compact) {
        Column(modifier) {
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.create_account_label),
                onClick = onRegisterClick,
                enabled = !loading
            )

            Spacer(Modifier.height(4.dp))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.sign_in_label),
                onClick = onLoginClick,
                enabled = !loading,
                loading = loading
            )
        }
    } else {
        Row(modifier) {
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.create_account_label),
                onClick = onRegisterClick,
                enabled = !loading
            )

            Spacer(Modifier.width(16.dp))

            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.sign_in_label),
                onClick = onLoginClick,
                enabled = !loading,
                loading = loading
            )
        }
    }
}