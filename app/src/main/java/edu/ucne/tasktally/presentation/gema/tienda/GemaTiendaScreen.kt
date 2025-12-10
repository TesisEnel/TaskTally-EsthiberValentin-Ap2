package edu.ucne.tasktally.presentation.gema.tienda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.tasktally.domain.models.RecompensaGema
import edu.ucne.tasktally.presentation.componentes.CircularLoadingIndicator
import edu.ucne.tasktally.presentation.componentes.RecompensaCard.GemaRecompensaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GemaTiendaScreen(
    modifier: Modifier = Modifier,
    viewModel: GemaTiendaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.onEvent(GemaTiendaUiEvent.ClearSuccessMessage)
        }
    }

    if (uiState.showConfirmDialog && uiState.recompensaToCanjear != null) {
        CanjearConfirmationDialog(
            recompensa = uiState.recompensaToCanjear!!,
            puntosDisponibles = uiState.puntosDisponibles,
            onConfirm = { viewModel.onEvent(GemaTiendaUiEvent.ConfirmCanjear) },
            onDismiss = { viewModel.onEvent(GemaTiendaUiEvent.DismissCanjearConfirmation) }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Tienda",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onEvent(GemaTiendaUiEvent.RefreshRecompensas) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refrescar"
                        )
                    }
                }
            )

            PuntosCard(
                gemaName = uiState.gemaName,
                puntosDisponibles = uiState.puntosDisponibles
            )

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
                            onRetry = { viewModel.onEvent(GemaTiendaUiEvent.RefreshRecompensas) },
                            onDismiss = { viewModel.onEvent(GemaTiendaUiEvent.ClearError) }
                        )
                    }

                    uiState.recompensas.isEmpty() -> {
                        EmptyRecompensasMessage()
                    }

                    else -> {
                        RecompensasGrid(
                            recompensas = uiState.recompensas,
                            puntosDisponibles = uiState.puntosDisponibles,
                            processingRecompensaId = uiState.processingRecompensaId,
                            onCanjearClick = { recompensa ->
                                viewModel.onEvent(GemaTiendaUiEvent.ShowCanjearConfirmation(recompensa))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PuntosCard(
    gemaName: String,
    puntosDisponibles: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hola, $gemaName!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Tus puntos disponibles",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "$puntosDisponibles",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun RecompensasGrid(
    recompensas: List<RecompensaGema>,
    puntosDisponibles: Int,
    processingRecompensaId: String?,
    onCanjearClick: (RecompensaGema) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(
            items = recompensas,
            key = { _, recompensa -> recompensa.recompensaId }
        ) { index, recompensa ->
            GemaRecompensaCard(
                numeroRecompensa = "Recompensa #${index + 1}",
                titulo = recompensa.titulo,
                descripcion = recompensa.descripcion,
                precio = recompensa.precio,
                imageName = recompensa.nombreImgVector,
                puedeComprar = puntosDisponibles >= recompensa.precio,
                isProcessing = processingRecompensaId == recompensa.recompensaId,
                onCanjearClick = { onCanjearClick(recompensa) }
            )
        }
    }
}

@Composable
private fun EmptyRecompensasMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No hay recompensas disponibles",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Cuando tu mentor agregue recompensas a la tienda, aparecerán aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
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
private fun CanjearConfirmationDialog(
    recompensa: RecompensaGema,
    puntosDisponibles: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                text = "Canjear recompensa",
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¿Deseas canjear \"${recompensa.titulo}\"?",
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Costo: ${recompensa.precio} puntos",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Puntos actuales: $puntosDisponibles",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Puntos después: ${puntosDisponibles - recompensa.precio}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Canjear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}