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
import edu.ucne.tasktally.presentation.gema.tareas.GemaTareasScreen
import edu.ucne.tasktally.presentation.mentor.tareas.CreateTareaScreen
import edu.ucne.tasktally.presentation.mentor.tareas.list.ListTareaScreen
import edu.ucne.tasktally.presentation.zona.ZoneAccessScreen

@Composable
fun TaskTallyNavHost(
    navHostController: NavHostController
) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    val currentUser by loginViewModel.uiState.collectAsState()

    LaunchedEffect(isLoggedIn, currentUser.currentUser?.role, currentUser.hasZoneAccess) {
        if (isLoggedIn && currentUser.currentUser != null) {

            val user = currentUser.currentUser

            val targetDestination = when (user?.role) {
                "mentor" -> Screen.MentorTareas
                "gema" -> if (currentUser.hasZoneAccess) Screen.Tareas else Screen.ZoneAccess
                else -> {
                    loginViewModel.onLogoutClick()
                    Screen.Login
                }
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
        startDestination = Screen.Login
    ) {

        composable<Screen.Login> {
            LoginScreen(
                onNavigateToRegister = {
                    navHostController.navigate(Screen.Register)
                },
                onLoginSuccess = {}
            )
        }

        composable<Screen.Register> {
            RegisterScreen(
                onNavigateBack = { navHostController.popBackStack() },
                onRegisterSuccess = { navHostController.popBackStack() }
            )
        }

        composable<Screen.ZoneAccess> {
            ZoneAccessScreen(
                onZoneAccessGranted = { loginViewModel.refreshZoneAccess() }
            )
        }

        composable<Screen.Tareas> {
            GemaTareasScreen()
        }

        composable<Screen.Tienda> {
            NavigationGuard(
                navController = navHostController,
                requiredRole = "gema"
            ) {
                TiendaScreen()
            }
        }

        composable<Screen.Perfil> {
            NavigationGuard(
                navController = navHostController,
                requiredRole = "gema"
            ) {
                PerfilScreen(
                    onLogout = { loginViewModel.onLogoutClick() },
                    onNavigateToZona = {
                        navHostController.navigate(Screen.Zona)
                    }
                )
            }
        }

        composable<Screen.Zona> {
            val user = currentUser.currentUser
            if (user != null) {
                edu.ucne.tasktally.presentation.zona.ZonaScreen(
                    userId = (user.gemaId ?: user.mentorId)?.toString() ?: "",
                    isMentor = user.role == "mentor"
                )
            }
        }

        composable<Screen.MentorTareas> {
            ListTareaScreen(
                onNavigateToCreate = {
                    navHostController.navigate(Screen.CreateTarea)
                },
                onNavigateToEdit = { tareaId ->
                    navHostController.navigate(Screen.EditTarea(tareaId))
                }
            )
        }

        composable<Screen.ListTareas> {
            ListTareaScreen(
                onNavigateToCreate = {
                    navHostController.navigate(Screen.CreateTarea)
                },
                onNavigateToEdit = { tareaId ->
                    navHostController.navigate(Screen.EditTarea(tareaId))
                }
            )
        }

        composable<Screen.CreateTarea> {
            CreateTareaScreen(
                tareaId = null,
                onNavigateBack = { navHostController.popBackStack() }
            )
        }

        composable<Screen.EditTarea> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditTarea>()
            CreateTareaScreen(
                tareaId = args.tareaId,
                onNavigateBack = { navHostController.popBackStack() }
            )
        }

        composable<Screen.MentorTienda> {}

        composable<Screen.MentorPerfil> {
            PerfilScreen(
                onLogout = { loginViewModel.onLogoutClick() },
                onNavigateToZona = {
                    navHostController.navigate(Screen.Zona)
                }
            )
        }
    }
}