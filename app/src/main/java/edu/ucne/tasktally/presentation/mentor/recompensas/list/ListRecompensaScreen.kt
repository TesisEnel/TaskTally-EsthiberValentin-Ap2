package edu.ucne.tasktally.presentation.mentor.recompensas.list

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
import edu.ucne.tasktally.domain.models.RecompensaMentor
import edu.ucne.tasktally.presentation.componentes.RecompensaCard.MentorRecompensaCard
import edu.ucne.tasktally.ui.theme.TaskTallyTheme

@Composable
fun ListRecompensaScreen(
    viewModel: ListRecompensaViewModel = hiltViewModel(),
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
        state.navigateToEdit?.let { recompensa ->
            viewModel.onNavigationHandled()
            onNavigateToEdit(recompensa.recompensaId)
        }
    }

    state.message?.let { message ->
        LaunchedEffect(message) {
            viewModel.onMessageShown()
        }
    }

    state.recompensaToDelete?.let { recompensa ->
        DeleteConfirmationDialog(
            recompensaTitle = recompensa.titulo,
            isDeleting = state.isDeleting,
            onConfirm = { viewModel.onEvent(ListRecompensaUiEvent.ConfirmDelete) },
            onDismiss = { viewModel.onEvent(ListRecompensaUiEvent.DismissDeleteConfirmation) }
        )
    }

    ListRecompensaBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ListRecompensaBody(
    state: ListRecompensaUiState,
    onEvent: (ListRecompensaUiEvent) -> Unit
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

                state.error != null -> {
                    ErrorContent(
                        error = state.error,
                        onRetry = { onEvent(ListRecompensaUiEvent.Load) }
                    )
                }

                state.recompensas.isEmpty() -> {
                    EmptyContent()
                }

                else -> {
                    RecompensasGrid(
                        recompensas = state.recompensas,
                        onEdit = { recompensa -> onEvent(ListRecompensaUiEvent.Edit(recompensa)) },
                        onDelete = { recompensa -> onEvent(ListRecompensaUiEvent.ShowDeleteConfirmation(recompensa)) }
                    )
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

@Composable
private fun RecompensasGrid(
    recompensas: List<RecompensaMentor>,
    onEdit: (RecompensaMentor) -> Unit,
    onDelete: (RecompensaMentor) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 80.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(
            items = recompensas,
            key = { _, recompensa -> recompensa.recompensaId }
        ) { index, recompensa ->
            MentorRecompensaCard(
                numeroRecompensa = "Recompensa #${index + 1}",
                titulo = recompensa.titulo,
                precio = recompensa.precio,
                imageName = recompensa.nombreImgVector,
                onEditClick = { onEdit(recompensa) },
                onDeleteClick = { onDelete(recompensa) }
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
    recompensaTitle: String,
    isDeleting: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isDeleting) onDismiss() },
        title = { Text(text = "Eliminar recompensa") },
        text = {
            Text(
                text = "¿Estás seguro de que deseas eliminar la recompensa \"$recompensaTitle\"? Esta acción no se puede deshacer."
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
fun ListRecompensaScreenPreview() {
    TaskTallyTheme {
        ListRecompensaBody(
            state = ListRecompensaUiState(
                mentorName = "Esthiber",
                recompensas = listOf(
                    RecompensaMentor(
                        recompensaId = "1",
                        createdBy = 1,
                        titulo = "Cenar pizza",
                        descripcion = "Una deliciosa pizza familiar",
                        precio = 750,
                        nombreImgVector = "img27_pizza"
                    ),
                    RecompensaMentor(
                        recompensaId = "2",
                        createdBy = 1,
                        titulo = "Salir el fin de semana",
                        descripcion = "Paseo familiar",
                        precio = 1500,
                        nombreImgVector = null
                    ),
                    RecompensaMentor(
                        recompensaId = "3",
                        createdBy = 1,
                        titulo = "Ir al cine",
                        descripcion = "Ver una película",
                        precio = 1300,
                        nombreImgVector = "img23_ice_cream"
                    ),
                    RecompensaMentor(
                        recompensaId = "4",
                        createdBy = 1,
                        titulo = "Nuevo celular",
                        descripcion = "Un smartphone nuevo",
                        precio = 100000,
                        nombreImgVector = "img24_mobile_phone"
                    )
                )
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListRecompensaScreenEmptyPreview() {
    TaskTallyTheme {
        ListRecompensaBody(
            state = ListRecompensaUiState(mentorName = "Esthiber"),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListRecompensaScreenLoadingPreview() {
    TaskTallyTheme {
        ListRecompensaBody(
            state = ListRecompensaUiState(isLoading = true, mentorName = "Esthiber"),
            onEvent = {}
        )
    }
}