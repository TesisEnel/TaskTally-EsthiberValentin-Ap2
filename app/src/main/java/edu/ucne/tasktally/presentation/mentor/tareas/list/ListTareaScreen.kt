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
import edu.ucne.tasktally.presentation.componentes.TareaCard.MentorTareaCard
import edu.ucne.tasktally.ui.theme.TaskTallyTheme

@Composable
fun ListTareaScreen(
    viewModel: ListTareaViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit = {},
    onNavigateToEdit: (String) -> Unit = {},
    mentorName: String = "Mentor"
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            viewModel.onNavigationHandled()
            onNavigateToCreate()
        }
    }

    LaunchedEffect(state.navigateToEditId) {
        state.navigateToEditId?.let { id ->
            viewModel.onNavigationHandled()
            onNavigateToEdit(id)
        }
    }

    state.message?.let { message ->
        LaunchedEffect(message) {
            viewModel.onMessageShown()
        }
    }

    ListTareaBody(
        state = state,
        onEvent = viewModel::onEvent,
        mentorName = mentorName
    )
}

@Composable
fun ListTareaBody(
    state: ListTareaUiState,
    onEvent: (ListTareaUiEvent) -> Unit,
    mentorName: String = "Mentor"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Hola, $mentorName!",
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

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.tareas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(state.tareas) { index, tarea ->
                        MentorTareaCard(
                            numeroTarea = "Tarea #${index + 1}",
                            titulo = tarea.titulo,
                            puntos = tarea.puntos.toInt(),
                            imageName = tarea.imgVector,
                            onEditClick = {
                                onEvent(ListTareaUiEvent.Edit(tarea.id))
                            },
                            onDeleteClick = {
                                onEvent(ListTareaUiEvent.Delete(tarea.id))
                            }
                        )
                    }
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

@Preview(showBackground = true)
@Composable
fun ListTareaScreenPreview() {
    TaskTallyTheme {
        ListTareaBody(
            state = ListTareaUiState(
                tareas = listOf(
                    Tarea(
                        id = "1",
                        remoteId = 1,
                        estado = "Pendiente",
                        titulo = "Arreglar la habitación",
                        descripcion = "Ordenar y limpiar",
                        puntos = 60.0,
                        diaAsignada = "2025-12-02",
                        imgVector = null,
                        isPendingPost = false,
                        isPendingUpdate = false
                    ),
                    Tarea(
                        id = "2",
                        remoteId = 2,
                        estado = "Pendiente",
                        titulo = "Barrer la casa",
                        descripcion = "Barrer todas las habitaciones",
                        puntos = 100.0,
//                        diaAsignada = null,
                        diaAsignada = "2025-12-02",
                        imgVector = null,
                        isPendingPost = false,
                        isPendingUpdate = false
                    )
                )
            ),
            onEvent = {},
            mentorName = "Esthiber"
        )
    }
}