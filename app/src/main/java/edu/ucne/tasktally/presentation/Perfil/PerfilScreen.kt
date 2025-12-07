package edu.ucne.tasktally.presentation.Perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.tasktally.presentation.componentes.ActionButtons.ActionButtonsRow
import edu.ucne.tasktally.presentation.componentes.CircularLoadingIndicator
import edu.ucne.tasktally.presentation.componentes.ProfileHeader.ProfileHeader
import edu.ucne.tasktally.presentation.componentes.StatsCard.StatsCard
import edu.ucne.tasktally.ui.theme.TaskTallyTheme
import kotlinx.coroutines.delay

@Composable
fun PerfilScreen(
    onLogout: () -> Unit = {},
    onNavigateToZona: () -> Unit = {}
) { // TODO Completar PerfilScreen
    var isLoading by remember { mutableStateOf(false) }

    // TODO: Estados que deben venir del ViewModel
    val userName by remember { mutableStateOf("Alma") }
    val motivationalMessage by remember { mutableStateOf("¡Sigue así!") }
    val completedTasks by remember { mutableStateOf(12) }
    val currentStreak by remember { mutableStateOf(5) }
    val totalPoints by remember { mutableStateOf(250) }

    LaunchedEffect(Unit) { // TODO: Implementar logica de carga desde ViewModel
        delay(2000)
        isLoading = false
    }

    if (isLoading) {
        CircularLoadingIndicator()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ProfileHeader(
                userName = userName,
                motivationalMessage = motivationalMessage
            )

            Text(
                text = "Estadísticas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            StatsCard(
                title = "Tareas Completadas",
                value = completedTasks.toString(),
                icon = Icons.Default.EmojiEvents,
                animationDelay = 100
            )

            StatsCard(
                title = "Racha Actual",
                value = "$currentStreak días",
                icon = Icons.Default.CalendarToday,
                animationDelay = 200
            )

            StatsCard(
                title = "Puntos Totales",
                value = totalPoints.toString(),
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                animationDelay = 300
            )

            Spacer(modifier = Modifier.height(16.dp))

            ActionButtonsRow(
                onNotificationsClick = {
                    // TODO: Implementar navegacion a notificaciones
                    println("Navegando a notificaciones")
                },
                onProfileClick = {
                    // TODO: Implementar navegacion a edicion de perfil
                    println("Navegando a edicion de perfil")
                },
                onSettingsClick = {
                    // TODO: Implementar navegacion a configuracion
                    println("Navegando a configuracion")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateToZona,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "Zona",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Mi Zona",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = "Cerrar Sesión",
                    color = MaterialTheme.colorScheme.onError
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilScreenPreview() {
    TaskTallyTheme {
        PerfilScreen(onLogout = {})
    }
}