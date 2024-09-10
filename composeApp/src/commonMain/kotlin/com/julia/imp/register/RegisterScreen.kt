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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.julia.imp.common.auth.UserCredentials
import com.julia.imp.common.ui.button.PrimaryButton
import com.julia.imp.common.ui.button.SecondaryButton
import com.julia.imp.common.ui.dialog.ErrorDialog
import imp.composeapp.generated.resources.Res
import imp.composeapp.generated.resources.create_account_label
import imp.composeapp.generated.resources.create_account_title
import imp.composeapp.generated.resources.email_label
import imp.composeapp.generated.resources.first_name_label
import imp.composeapp.generated.resources.last_name_label
import imp.composeapp.generated.resources.password_confirmation_label
import imp.composeapp.generated.resources.password_label
import imp.composeapp.generated.resources.register_error_message
import imp.composeapp.generated.resources.register_error_title
import imp.composeapp.generated.resources.use_existing_account_label
import org.jetbrains.compose.resources.stringResource

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegistrationComplete: (UserCredentials) -> Unit,
    viewModel: RegisterViewModel = viewModel { RegisterViewModel() }
) {
    LaunchedEffect(viewModel.uiState.registeredCredentials) {
        viewModel.uiState.registeredCredentials?.let {
            onRegistrationComplete(it)
        }
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

                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = stringResource(Res.string.create_account_title),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )

                RegisterFormFields(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp),
                    firstName = viewModel.uiState.firstName,
                    lastName = viewModel.uiState.lastName,
                    email = viewModel.uiState.email,
                    password = viewModel.uiState.password,
                    passwordConfirmation = viewModel.uiState.passwordConfirmation,
                    onFirstNameChange = { viewModel.setFirstName(it) },
                    onLastNameChange = { viewModel.setLastName(it) },
                    onEmailChange = { viewModel.setEmail(it) },
                    onPasswordChange = { viewModel.setPassword(it) },
                    onPasswordConfirmationChange = { viewModel.setPasswordConfirmation(it) },
                    passwordMismatch = viewModel.uiState.passwordMismatch,
                    enabled = !viewModel.uiState.loading
                )

                if (compact) Spacer(Modifier.weight(1f))

                RegisterFormButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    compact = compact,
                    loading = viewModel.uiState.loading,
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
    ErrorDialog(
        title = stringResource(Res.string.register_error_title),
        message = stringResource(Res.string.register_error_message),
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun RegisterFormFields(
    firstName: String,
    lastName: String,
    email: String,
    password: String,
    passwordConfirmation: String,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
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
            value = firstName,
            onValueChange = onFirstNameChange,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(Res.string.first_name_label)) }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = lastName,
            onValueChange = onLastNameChange,
            enabled = enabled,
            singleLine = true,
            label = { Text(stringResource(Res.string.last_name_label)) }
        )

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
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.use_existing_account_label),
                onClick = onLoginClick,
                enabled = !loading
            )

            Spacer(Modifier.height(4.dp))

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(Res.string.create_account_label),
                onClick = onRegisterClick,
                enabled = !loading,
                loading = loading
            )
        }
    } else {
        Row(modifier) {
            SecondaryButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.use_existing_account_label),
                onClick = onLoginClick,
                enabled = !loading
            )

            Spacer(Modifier.width(16.dp))

            PrimaryButton(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.create_account_label),
                onClick = onRegisterClick,
                enabled = !loading,
                loading = loading
            )
        }
    }
}