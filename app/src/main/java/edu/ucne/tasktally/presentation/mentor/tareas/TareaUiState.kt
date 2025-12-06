package edu.ucne.tasktally.presentation.mentor.tareas

data class TareaUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,

    val tareaId: Int? = null,
    val isEditing: Boolean = false,

    val titulo: String = "",
    val descripcion: String = "",
    val puntos: String = "",
    val recurrente: Boolean = false,
    val diasSeleccionados: List<String> = emptyList(),
    val asignada: String = "0",
    val imgVector: String? = null,

    val showImagePicker: Boolean = false,
    val showDiasPicker: Boolean = false,

    val tituloError: String? = null,
    val descripcionError: String? = null,
    val puntosError: String? = null,

    val navigateBack: Boolean = false
)