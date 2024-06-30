package com.julia.imp.register

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.create_account_label
import imp.composeapp.generated.resources.create_account_title
import imp.composeapp.generated.resources.email_label
import imp.composeapp.generated.resources.ok_label
import imp.composeapp.generated.resources.password_confirmation_label
import imp.composeapp.generated.resources.password_label
import imp.composeapp.generated.resources.register_error_message
import imp.composeapp.generated.resources.register_error_title
import imp.composeapp.generated.resources.use_existing_account_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegistrationComplete: () -> Unit,
    viewModel: RegisterViewModel = viewModel { RegisterViewModel() }
) {
    Scaffold { paddingValues ->
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
                    .consumeWindowInsets(PaddingValues(horizontal = 24.dp))
                    .consumeWindowInsets(paddingValues)
                    .padding(horizontal = 24.dp)
                    .padding(paddingValues)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (compact) Spacer(Modifier.weight(1f))

                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = stringResource(Res.string.create_account_title),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )

                RegisterFormFields(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                    email = viewModel.uiState.email,
                    password = viewModel.uiState.password,
                    passwordConfirmation = viewModel.uiState.passwordConfirmation,
                    onEmailChange = { viewModel.setEmail(it) },
                    onPasswordChange = { viewModel.setPassword(it) },
                    onPasswordConfirmationChange = { viewModel.setPasswordConfirmation(it) },
                    passwordMismatch = viewModel.uiState.passwordMismatch,
                    enabled = !viewModel.uiState.isLoading
                )

                if (compact) Spacer(Modifier.weight(1f))

                RegisterFormButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    compact = compact,
                    loading = viewModel.uiState.isLoading,
                    onRegisterClick = { viewModel.register() },
                    onLoginClick = onLoginClick
                )
            }
        }
    }

    if (viewModel.uiState.showError) {
        RegisterErrorDialog(onDismissRequest = { viewModel.dismissError() })
    }
}

@Composable
fun RegisterErrorDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        icon = { Icon(Icons.Outlined.Warning, null) },
        title = { Text(text = stringResource(Res.string.register_error_title)) },
        text = { Text(text = stringResource(Res.string.register_error_message)) },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Res.string.ok_label))
            }
        }
    )
}

@Composable
fun RegisterFormFields(
    email: String,
    password: String,
    passwordConfirmation: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmationChange: (String) -> Unit,
    passwordMismatch: Boolean,
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

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = passwordConfirmation,
            onValueChange = onPasswordConfirmationChange,
            enabled = enabled,
            singleLine = true,
            isError = passwordMismatch,
            supportingText = {
                if (passwordMismatch) Text("Senhas não coincidem")
            },
            label = { Text(stringResource(Res.string.password_confirmation_label)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}

@Composable
fun RegisterFormButtons(
    compact: Boolean,
    loading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (compact) {
        Column(modifier) {
            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLoginClick,
                enabled = !loading
            ) {
                Text(stringResource(Res.string.use_existing_account_label))
            }

            Spacer(Modifier.height(4.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRegisterClick,
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(Res.string.create_account_label))
                }
            }
        }
    } else {
        Row(modifier) {
            FilledTonalButton(
                modifier = Modifier.weight(1f),
                onClick = onLoginClick,
                enabled = !loading
            ) {
                Text(stringResource(Res.string.use_existing_account_label))
            }

            Spacer(Modifier.width(16.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onRegisterClick,
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(stringResource(Res.string.create_account_label))
                }
            }
        }
    }
}