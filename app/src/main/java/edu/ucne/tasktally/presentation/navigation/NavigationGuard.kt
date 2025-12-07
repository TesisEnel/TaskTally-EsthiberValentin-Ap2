package edu.ucne.tasktally.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import edu.ucne.tasktally.presentation.auth.LoginViewModel

@Composable
fun NavigationGuard(
    navController: NavController,
    requiredRole: String,
    content: @Composable () -> Unit
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val uiState by loginViewModel.uiState.collectAsState()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn, uiState.currentUser?.role) {
        if (!isLoggedIn) {
            navController.navigate(Screen.Login) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        val userRole = uiState.currentUser?.role
        if (userRole != null && userRole != requiredRole) {
            val correctDestination = when (userRole) {
                "mentor" -> Screen.MentorTareas
                "gema" -> if (uiState.hasZoneAccess) Screen.Tareas else Screen.ZoneAccess
                else -> Screen.Login
            }

            navController.navigate(correctDestination) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (isLoggedIn && uiState.currentUser?.role == requiredRole) {
        content()
    }
}
