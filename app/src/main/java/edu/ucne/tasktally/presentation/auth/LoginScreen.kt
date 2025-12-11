package edu.ucne.tasktally.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.tasktally.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val primaryColor = MaterialTheme.colorScheme.primary

    HandleLoginEffects(isLoggedIn, uiState.error, onLoginSuccess)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryColor)
    ) {
        LoginHeader()

        LoginCard(
            uiState = uiState,
            primaryColor = primaryColor,
            onUsernameChanged = viewModel::onUsernameChanged,
            onPasswordChanged = viewModel::onPasswordChanged,
            onLoginClick = viewModel::onLoginClick,
            onNavigateToRegister = onNavigateToRegister
        )
    }
}

@Composable
private fun HandleLoginEffects(
    isLoggedIn: Boolean,
    error: String?,
    onLoginSuccess: () -> Unit
) {
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            // TODO Manejar ERRORESSSS
        }
    }
}

@Composable
private fun LoginHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.35f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Hola!",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Bienvenido a TaskTally",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.img_inicio),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun BoxScope.LoginCard(
    uiState: LoginUiState,
    primaryColor: Color,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.68f)
            .align(Alignment.BottomCenter),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                color = primaryColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoginForm(
                uiState = uiState,
                primaryColor = primaryColor,
                onUsernameChanged = onUsernameChanged,
                onPasswordChanged = onPasswordChanged,
                onLoginClick = onLoginClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegisterPrompt(
                primaryColor = primaryColor,
                isLoading = uiState.isLoading,
                onNavigateToRegister = onNavigateToRegister
            )
        }
    }
}

@Composable
private fun LoginForm(
    uiState: LoginUiState,
    primaryColor: Color,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by remember { mutableStateOf(false) }

    UsernameField(
        value = uiState.username,
        onValueChange = onUsernameChanged,
        primaryColor = primaryColor,
        isEnabled = !uiState.isLoading,
        focusManager = focusManager
    )

    Spacer(modifier = Modifier.height(16.dp))

    PasswordField(
        value = uiState.password,
        onValueChange = onPasswordChanged,
        primaryColor = primaryColor,
        isEnabled = !uiState.isLoading,
        isPasswordVisible = isPasswordVisible,
        onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
        onDone = {
            focusManager.clearFocus()
            onLoginClick()
        }
    )

    ErrorMessage(error = uiState.error)

    Spacer(modifier = Modifier.height(32.dp))

    LoginButton(
        isLoading = uiState.isLoading,
        isEnabled = !uiState.isLoading && uiState.username.isNotBlank() && uiState.password.isNotBlank(),
        primaryColor = primaryColor,
        onClick = onLoginClick
    )
}

@Composable
private fun UsernameField(
    value: String,
    onValueChange: (String) -> Unit,
    primaryColor: Color,
    isEnabled: Boolean,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Usuario") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryColor,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = primaryColor
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        enabled = isEnabled
    )
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    primaryColor: Color,
    isEnabled: Boolean,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Contraseña") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryColor,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = primaryColor
        ),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        trailingIcon = {
            PasswordVisibilityToggle(
                isPasswordVisible = isPasswordVisible,
                onToggle = onTogglePasswordVisibility
            )
        },
        enabled = isEnabled
    )
}

@Composable
private fun PasswordVisibilityToggle(
    isPasswordVisible: Boolean,
    onToggle: () -> Unit
) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
            tint = Color.Gray
        )
    }
}

@Composable
private fun ErrorMessage(error: String?) {
    error?.let {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun LoginButton(
    isLoading: Boolean,
    isEnabled: Boolean,
    primaryColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        enabled = isEnabled
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White
            )
        } else {
            Text(
                text = "Iniciar Sesión",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RegisterPrompt(
    primaryColor: Color,
    isLoading: Boolean,
    onNavigateToRegister: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "¿No tienes cuenta? ",
            color = Color.Gray,
            fontSize = 14.sp
        )
        TextButton(
            onClick = onNavigateToRegister,
            enabled = !isLoading,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Regístrate",
                color = primaryColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        onNavigateToRegister = {},
        onLoginSuccess = {}
    )
}