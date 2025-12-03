package edu.ucne.tasktally.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.tasktally.presentation.Perfil.PerfilScreen
import edu.ucne.tasktally.presentation.Tienda.TiendaScreen
import edu.ucne.tasktally.presentation.auth.LoginScreen
import edu.ucne.tasktally.presentation.auth.LoginViewModel
import edu.ucne.tasktally.presentation.auth.RegisterScreen
import edu.ucne.tasktally.presentation.mentor.tareas.CreateTareaScreen
import edu.ucne.tasktally.presentation.mentor.recompensas.CreateRecompensaScreen
import edu.ucne.tasktally.presentation.mentor.tareas.list.ListTareaScreen
import edu.ucne.tasktally.presentation.mentor.recompensas.lista.ListaRecompensaScreen

@Composable
fun TaskTallyNavHost(
    navHostController: NavHostController
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    // TODO: descomentar
    /*
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navHostController.navigate(Screen.Tareas) {
                popUpTo(Screen.Login) { inclusive = true }
            }
        } else {
            navHostController.navigate(Screen.Login) {
                popUpTo(0) { inclusive = true }
            }
        }
    }
    */

    NavHost(
        navController = navHostController,
        // TODO: descomentar el logged
        // startDestination = if (isLoggedIn) Screen.Tareas else Screen.Login
        startDestination = Screen.ListaRecompensas
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navHostController.navigate(Screen.Register)
                },
                onLoginSuccess = {
                    navHostController.navigate(Screen.Tareas) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
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
                onNavigateBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable<Screen.CreateRecompensa> {
            CreateRecompensaScreen(
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
                    // TODO: implementar edit
                    println("Editar tarea: $tareaId")
                },
                mentorName = "Mentor"
            )
        }

        composable<Screen.ListaRecompensas> {
            ListaRecompensaScreen(
                onNavigateToCreate = {
                    navHostController.navigate(Screen.CreateRecompensa)
                },
                onNavigateToEdit = { recompensaId ->
                    // TODO: implementar edit
                    println("Editar recompensa: $recompensaId")
                },
                mentorName = "Mentor"
            )
        }
    }
}