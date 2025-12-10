package edu.ucne.tasktally.presentation.mentor.tareas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.tasktally.presentation.componentes.ImagePickerBottomSheet
import edu.ucne.tasktally.ui.theme.TaskTallyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTareaScreen(
    viewModel: TareaViewModel = hiltViewModel(),
    tareaId: String? = null,
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(tareaId) {
        if (tareaId != null) {
            viewModel.loadTareaParaEdicion(tareaId)
        }
    }

    LaunchedEffect(state.navigateBack) {
        if (state.navigateBack) {
            viewModel.onNavigationHandled()
            onNavigateBack()
        }
    }

    state.message?.let { message ->
        LaunchedEffect(message) {
            viewModel.onMessageShown()
        }
    }

    state.error?.let { error ->
        LaunchedEffect(error) {
            viewModel.onMessageShown()
        }
    }

    if (state.showImagePicker) {
        ImagePickerBottomSheet(
            onDismiss = { viewModel.onEvent(TareaUiEvent.OnDismissImagePicker) },
            onImageSelected = { imageName ->
                viewModel.onEvent(TareaUiEvent.OnImageSelected(imageName))
            },
            selectedImageName = state.imgVector
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEditing) "Editar tarea" else "Crear tarea",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        CreateTareaBody(
            state = state,
            onEvent = viewModel::onEvent,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun CreateTareaBody(
    state: TareaUiState,
    onEvent: (TareaUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading && state.isEditing) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hola, ${state.mentorName}!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = state.titulo,
                    onValueChange = { onEvent(TareaUiEvent.OnTituloChange(it)) },
                    label = { Text(text = "Título") },
                    isError = state.tituloError != null,
                    supportingText = state.tituloError?.let {
                        { Text(text = it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.descripcion,
                    onValueChange = { onEvent(TareaUiEvent.OnDescripcionChange(it)) },
                    label = { Text(text = "Descripción") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.puntos,
                    onValueChange = { onEvent(TareaUiEvent.OnPuntosChange(it)) },
                    label = { Text(text = "Puntos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = state.puntosError != null,
                    supportingText = state.puntosError?.let {
                        { Text(text = it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ImageSelectorBox(
                    selectedImage = state.imgVector,
                    onClick = { onEvent(TareaUiEvent.OnShowImagePicker) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onEvent(TareaUiEvent.Save) },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = if (state.isEditing) "Actualizar" else "Guardar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ImageSelectorBox(
    selectedImage: String?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImage != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(selectedImage)
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("Selecciona una imagen")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTareaScreenPreview() {
    TaskTallyTheme {
        CreateTareaBody(
            state = TareaUiState(mentorName = "Esthiber"),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTareaScreenEditingPreview() {
    TaskTallyTheme {
        CreateTareaBody(
            state = TareaUiState(
                isEditing = true,
                mentorName = "Esthiber",
                titulo = "Arreglar la habitación",
                descripcion = "Ordenar y limpiar todo",
                puntos = "60",
                imgVector = "img0_yellow_tree"
            ),
            onEvent = {}
        )
    }
}