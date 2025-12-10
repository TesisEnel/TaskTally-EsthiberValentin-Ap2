package edu.ucne.tasktally.presentation.componentes.BottomNavBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FormatListNumberedRtl
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import edu.ucne.tasktally.presentation.navigation.Screen

@Composable
fun BottomNavBar(
    navController: NavController,
    isMentor: Boolean = false
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigateTo: (Screen) -> Unit = { screen ->
        navController.navigate(screen) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    XrNavigationBar(
        currentRoute = currentRoute,
        navigateTo = navigateTo,
        isMentor = isMentor
    )
}

@Composable
fun XrNavigationBar(
    currentRoute: String?,
    navigateTo: (Screen) -> Unit,
    isMentor: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isMentor) {
                    XrNavigationItem(
                        icon = Icons.Default.FormatListNumberedRtl,
                        label = "Tareas",
                        isSelected = currentRoute == Screen.MentorTareas::class.qualifiedName,
                        onClick = { navigateTo(Screen.MentorTareas) }
                    )

                    XrNavigationItem(
                        icon = Icons.Default.ShoppingCart,
                        label = "Tienda",
                        isSelected = currentRoute == Screen.MentorTienda::class.qualifiedName,
                        onClick = { navigateTo(Screen.MentorTienda) }
                    )

                    XrNavigationItem(
                        icon = Icons.Default.AccountCircle,
                        label = "Perfil",
                        isSelected = currentRoute == Screen.MentorPerfil::class.qualifiedName,
                        onClick = { navigateTo(Screen.MentorPerfil) }
                    )
                } else {
                    XrNavigationItem(
                        icon = Icons.Default.FormatListNumberedRtl,
                        label = "Tareas",
                        isSelected = currentRoute == Screen.Tareas::class.qualifiedName,
                        onClick = { navigateTo(Screen.Tareas) }
                    )

                    XrNavigationItem(
                        icon = Icons.Default.ShoppingCart,
                        label = "Tienda",
                        isSelected = currentRoute == Screen.Tienda::class.qualifiedName,
                        onClick = { navigateTo(Screen.Tienda) }
                    )

                    XrNavigationItem(
                        icon = Icons.Default.AccountCircle,
                        label = "Perfil",
                        isSelected = currentRoute == Screen.Perfil::class.qualifiedName,
                        onClick = { navigateTo(Screen.Perfil) }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(navController = NavController(LocalContext.current))
}


