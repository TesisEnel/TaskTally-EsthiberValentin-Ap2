package edu.ucne.tasktally.presentation.gema.tareas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.tasktally.presentation.componentes.CircularLoadingIndicator
import edu.ucne.tasktally.presentation.componentes.TareaCard.GemaTareaCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GemaTareasScreen(
    modifier: Modifier = Modifier,
    gemaId: Int = 1,
    viewModel: GemaTareasViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(gemaId) {
        viewModel.setGemaId(gemaId)
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Mis Tareas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(
                    onClick = { showDatePicker = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Filtrar por fecha"
                    )
                }

                IconButton(
                    onClick = { viewModel.onEvent(GemaTareasUiEvent.RefreshTareas) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refrescar"
                    )
                }
            }
        )

        if (uiState.selectedDay != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Fecha: ${uiState.selectedDay}",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    TextButton(
                        onClick = {
                            viewModel.onEvent(GemaTareasUiEvent.FilterByDay(null))
                        }
                    ) {
                        Text("Limpiar")
                    }
                }
            }
        }

        // Contenido principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    CircularLoadingIndicator()
                }

                uiState.errorMessage != null -> {
                    ErrorMessage(
                        message = uiState.errorMessage.toString(),
                        onRetry = { viewModel.onEvent(GemaTareasUiEvent.RefreshTareas) },
                        onDismiss = { viewModel.clearError() }
                    )
                }

                uiState.tareas.isEmpty() -> {
                    EmptyTasksMessage()
                }

                else -> {
                    TasksList(
                        tareas = uiState.tareas,
                        processingTaskId = uiState.processingTaskId,
                        onIniciarTarea = { tareaId ->
                            viewModel.onEvent(GemaTareasUiEvent.IniciarTarea(tareaId))
                        },
                        onCompletarTarea = { tareaId ->
                            viewModel.onEvent(GemaTareasUiEvent.CompletarTarea(tareaId))
                        }
                    )
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                viewModel.onEvent(GemaTareasUiEvent.FilterByDay(date))
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@Composable
private fun TasksList(
    tareas: List<edu.ucne.tasktally.domain.models.TareaGema>,
    processingTaskId: String?,
    onIniciarTarea: (String) -> Unit,
    onCompletarTarea: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(
            items = tareas,
            key = { it.tareaId }
        ) { tarea ->
            GemaTareaCard(
                tarea = tarea,
                isProcessing = processingTaskId == tarea.tareaId,
                onIniciarClick = onIniciarTarea,
                onCompletarClick = onCompletarTarea
            )
        }
    }
}

@Composable
private fun EmptyTasksMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No hay tareas disponibles",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Cuando tu mentor te asigne tareas, aparecerán aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Error",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onRetry) {
                    Text("Reintentar")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Composable
private fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000L))
                        val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        onDateSelected(formattedDate)
                    }
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
