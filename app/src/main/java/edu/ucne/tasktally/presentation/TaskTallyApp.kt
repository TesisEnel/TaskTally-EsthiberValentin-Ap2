package edu.ucne.tasktally.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.ucne.tasktally.presentation.auth.LoginViewModel
import edu.ucne.tasktally.presentation.componentes.BottomNavBar.BottomNavBar
import edu.ucne.tasktally.presentation.componentes.TopAppBar
import edu.ucne.tasktally.presentation.navigation.Screen
import edu.ucne.tasktally.presentation.navigation.TaskTallyNavHost
import edu.ucne.tasktally.ui.theme.TaskTallyTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskTallyApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val loginViewModel: LoginViewModel = hiltViewModel()
    val currentUser by loginViewModel.uiState.collectAsState()
    val isMentor = currentUser.currentUser?.role == "mentor"

    val title = when (currentRoute) {
        Screen.Tareas::class.qualifiedName -> "Mis Tareas"
        Screen.Tienda::class.qualifiedName -> "Tienda"
        Screen.Perfil::class.qualifiedName -> "Perfil"
        Screen.MentorTareas::class.qualifiedName -> "Gestionar Tareas"
        Screen.MentorTienda::class.qualifiedName -> "Gestionar Recompensas"
        Screen.MentorPerfil::class.qualifiedName -> "Perfil Mentor"
        Screen.Login::class.qualifiedName -> "Iniciar Sesión"
        Screen.Register::class.qualifiedName -> "Registrarse"
        Screen.CreateTarea::class.qualifiedName -> "Crear Tareas"
        Screen.CreateRecompensa::class.qualifiedName -> "Crear Recompensa"
        Screen.ListTareas::class.qualifiedName -> "Lista de Tareas"
        Screen.ListRecompensas::class.qualifiedName -> "Lista de Recompensas"
        Screen.ZoneAccess::class.qualifiedName -> "Acceso a Zona"
        else -> "TaskTally"
    }

    val isAuthScreen = currentRoute == Screen.Login::class.qualifiedName ||
            currentRoute == Screen.Register::class.qualifiedName ||
            currentRoute == Screen.ZoneAccess::class.qualifiedName

    val showBottomBar = !isAuthScreen &&
            currentRoute != Screen.CreateTarea::class.qualifiedName &&
            currentRoute != Screen.CreateRecompensa::class.qualifiedName &&
            currentRoute != Screen.ListTareas::class.qualifiedName &&
            currentRoute != Screen.ListRecompensas::class.qualifiedName

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (!isAuthScreen) {
                TopAppBar(title = title)
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    navController = navController,
                    isMentor = isMentor
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            TaskTallyNavHost(navHostController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskTallyAppPreview() {
    TaskTallyTheme {
        TaskTallyApp()
    }
}