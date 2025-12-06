package edu.ucne.tasktally.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.tasktally.presentation.Perfil.PerfilScreen
import edu.ucne.tasktally.presentation.Tienda.TiendaScreen
import edu.ucne.tasktally.presentation.auth.LoginScreen
import edu.ucne.tasktally.presentation.auth.LoginViewModel
import edu.ucne.tasktally.presentation.auth.RegisterScreen
import edu.ucne.tasktally.presentation.mentor.tareas.CreateTareaScreen
import edu.ucne.tasktally.presentation.mentor.recompensas.CreateRecompensaScreen
import edu.ucne.tasktally.presentation.mentor.recompensas.lista.ListRecompensaScreen
import edu.ucne.tasktally.presentation.mentor.tareas.list.ListTareaScreen

@Composable
fun TaskTallyNavHost(
    navHostController: NavHostController
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    val currentUser by loginViewModel.uiState.collectAsState()

    val startDestination = if (isLoggedIn) {
        when (currentUser.currentUser?.role) {
            "mentor" -> Screen.MentorTareas
            "gema" -> Screen.Tareas
            else -> Screen.Tareas
        }
    } else {
        Screen.Login
    }

    LaunchedEffect(isLoggedIn, currentUser.currentUser?.role) {
        if (isLoggedIn && currentUser.currentUser != null) {
            val user = currentUser.currentUser
            val targetDestination = when (user?.role) {
                "mentor" -> Screen.MentorTareas
                "gema" -> Screen.Tareas
                else -> Screen.Tareas
            }
            navHostController.navigate(targetDestination) {
                popUpTo(Screen.Login) { inclusive = true }
            }
        } else if (!isLoggedIn) {
            navHostController.navigate(Screen.Login) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navHostController,
        startDestination = Screen.Login // TODO Cambiar cuado lo demas funcione.
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navHostController.navigate(Screen.Register)
                },
                onLoginSuccess = {

                }
            )
        }

        composable<Screen.Register> {
            RegisterScreen(
                onNavigateBack = {
                    navHostController.popBackStack()
                },
                onRegisterSuccess = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<Screen.Tienda> {
            TiendaScreen()
        }

        composable<Screen.Perfil> {
            PerfilScreen(
                onLogout = {
                    loginViewModel.onLogoutClick()
                }
            )
        }

        composable<Screen.CreateTarea> {
            CreateTareaScreen(
                tareaId = null,
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<Screen.EditTarea> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditTarea>()
            CreateTareaScreen(
                tareaId = args.tareaId,
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<Screen.CreateRecompensa> {
            CreateRecompensaScreen(
                recompensaId = null,
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<Screen.EditRecompensa> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditRecompensa>()
            CreateRecompensaScreen(
                recompensaId = args.recompensaId,
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<Screen.ListTareas> {
            ListTareaScreen(
                onNavigateToCreate = {
                    navHostController.navigate(Screen.CreateTarea)
                },
                onNavigateToEdit = { tareaId ->
                    navHostController.navigate(Screen.EditTarea(tareaId = tareaId))
                },
                mentorName = "Mentor"
            )
        }

        composable<Screen.ListRecompensas> {
            ListRecompensaScreen(
                onNavigateToCreate = {
                    navHostController.navigate(Screen.CreateRecompensa)
                },
                onNavigateToEdit = { recompensaId ->
                    navHostController.navigate(Screen.EditRecompensa(recompensaId = recompensaId))
                },
                mentorName = "Mentor"
            )
        }
    }
}