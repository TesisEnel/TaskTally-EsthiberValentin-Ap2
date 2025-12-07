package edu.ucne.tasktally.presentation.mentor.tareas.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.tasktally.domain.models.TareaMentor
import edu.ucne.tasktally.presentation.componentes.TareaCard.MentorTareaCard
import edu.ucne.tasktally.ui.theme.TaskTallyTheme

@Composable
fun ListTareaScreen(
    viewModel: ListTareaViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit = {},
    onNavigateToEdit: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            viewModel.onNavigationHandled()
            onNavigateToCreate()
        }
    }

    LaunchedEffect(state.navigateToEdit) {
        state.navigateToEdit?.let { tarea ->
            viewModel.onNavigationHandled()
            onNavigateToEdit(tarea.tareaId)
        }
    }

    state.message?.let { message ->
        LaunchedEffect(message) {
            viewModel.onMessageShown()
        }
    }

    state.tareaToDelete?.let { tarea ->
        DeleteConfirmationDialog(
            tareaTitle = tarea.titulo,
            isDeleting = state.isDeleting,
            onConfirm = { viewModel.onEvent(ListTareaUiEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(ListTareaUiEvent.DismissDeleteConfirmation) }
        )
    }

    ListTareaBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ListTareaBody(
    state: ListTareaUiState,
    onEvent: (ListTareaUiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Hola, ${state.mentorName}!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tareas",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    ErrorContent(
                        error = state.error,
                        onRetry = { onEvent(ListTareaUiEvent.Load) }
                    )
                }

                state.tareas.isEmpty() -> {
                    EmptyContent()
                }

                else -> {
                    TareasList(
                        tareas = state.tareas,
                        onEdit = { tarea -> onEvent(ListTareaUiEvent.Edit(tarea)) },
                        onDelete = { tarea -> onEvent(ListTareaUiEvent.ShowDeleteConfirmation(tarea)) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { onEvent(ListTareaUiEvent.CreateNew) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Crear tarea"
            )
        }
    }
}

@Composable
private fun TareasList(
    tareas: List<TareaMentor>,
    onEdit: (TareaMentor) -> Unit,
    onDelete: (TareaMentor) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = tareas,
            key = { _, tarea -> tarea.tareaId }
        ) { index, tarea ->
            MentorTareaCard(
                numeroTarea = "Tarea #${index + 1}",
                titulo = tarea.titulo,
                puntos = tarea.puntos,
                imageName = tarea.nombreImgVector,
                onEditClick = { onEdit(tarea) },
                onDeleteClick = { onDelete(tarea) }
            )
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No hay tareas",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toca + para crear una nueva",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = error,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    tareaTitle: String,
    isDeleting: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isDeleting) onDismiss() },
        title = { Text(text = "Eliminar tarea") },
        text = {
            Text(
                text = "¿Estás seguro de que deseas eliminar la tarea \"$tareaTitle\"? Esta acción no se puede deshacer."
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isDeleting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                if (isDeleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text("Eliminar")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isDeleting
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ListTareaScreenPreview() {
    TaskTallyTheme {
        ListTareaBody(
            state = ListTareaUiState(
                mentorName = "Esthiber",
                tareas = listOf(
                    TareaMentor(
                        tareaId = "1",
                        mentorId = 1,
                        titulo = "Arreglar la habitación",
                        descripcion = "Ordenar y limpiar",
                        puntos = 60,
                        recurrente = false,
                        dias = null,
                        nombreImgVector = "img0_yellow_tree"
                    ),
                    TareaMentor(
                        tareaId = "2",
                        mentorId = 1,
                        titulo = "Barrer la casa",
                        descripcion = "Barrer todas las habitaciones",
                        puntos = 100,
                        recurrente = true,
                        dias = "Mon,Wed,Fri",
                        nombreImgVector = "img5_purple_flower"
                    )
                )
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListTareaScreenEmptyPreview() {
    TaskTallyTheme {
        ListTareaBody(
            state = ListTareaUiState(mentorName = "Esthiber"),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListTareaScreenLoadingPreview() {
    TaskTallyTheme {
        ListTareaBody(
            state = ListTareaUiState(isLoading = true, mentorName = "Esthiber"),
            onEvent = {}
        )
    }
}
