package edu.ucne.tasktally.presentation.mentor.recompensas.lista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import edu.ucne.tasktally.presentation.componentes.RecompensaCard.MentorRecompensaCard
import edu.ucne.tasktally.presentation.mentor.recompensas.list.ListRecompensaUiEvent
import edu.ucne.tasktally.presentation.mentor.recompensas.list.ListRecompensaUiState
import edu.ucne.tasktally.presentation.mentor.recompensas.list.ListRecompensaViewModel
import edu.ucne.tasktally.ui.theme.TaskTallyTheme

@Composable
fun ListRecompensaScreen(
    viewModel: ListRecompensaViewModel = hiltViewModel(),
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

    ListRecompensaBody(
        state = state,
        onEvent = viewModel::onEvent,
        mentorName = mentorName
    )
}

@Composable
fun ListRecompensaBody(
    state: ListRecompensaUiState,
    onEvent: (ListRecompensaUiEvent) -> Unit,
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
                    text = "Tienda",
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
                state.recompensas.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No hay recompensas",
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
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(state.recompensas) { index, recompensa ->
                            MentorRecompensaCard(
                                numeroRecompensa = "Recompensa #${index + 1}",
                                titulo = recompensa.titulo,
                                precio = recompensa.precio.toInt(),
                                imageName = recompensa.imgVector,
                                onEditClick = {
                                    onEvent(ListRecompensaUiEvent.Edit(recompensa.id))
                                },
                                onDeleteClick = {
                                    onEvent(ListRecompensaUiEvent.Delete(recompensa.id))
                                }
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { onEvent(ListRecompensaUiEvent.CreateNew) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Crear recompensa"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListRecompensaScreenPreview() {
    TaskTallyTheme {
        ListRecompensaBody(
            state = ListRecompensaUiState(
                recompensas = listOf(
                    Recompensa(
                        id = "1",
                        remoteId = 1,
                        titulo = "Cenar pizza",
                        descripcion = "Una deliciosa pizza familiar",
                        precio = 750.0,
                        imgVector = "img27_pizza",
                        isPendingPost = false,
                        isPendingUpdate = false
                    ),
                    Recompensa(
                        id = "2",
                        remoteId = 2,
                        titulo = "Salir el fin de semana",
                        descripcion = "Paseo familiar",
                        precio = 1500.0,
                        imgVector = null,
                        isPendingPost = false,
                        isPendingUpdate = false
                    ),
                    Recompensa(
                        id = "3",
                        remoteId = 3,
                        titulo = "Ir al cine",
                        descripcion = "Ver una película",
                        precio = 1300.0,
                        imgVector = "img23_ice_cream",
                        isPendingPost = false,
                        isPendingUpdate = false
                    ),
                    Recompensa(
                        id = "4",
                        remoteId = 4,
                        titulo = "Nuevo celular",
                        descripcion = "Un smartphone nuevo",
                        precio = 1000000.0,
                        imgVector = "img24_mobile_phone",
                        isPendingPost = false,
                        isPendingUpdate = false
                    )
                )
            ),
            onEvent = {},
            mentorName = "Alma"
        )
    }
}