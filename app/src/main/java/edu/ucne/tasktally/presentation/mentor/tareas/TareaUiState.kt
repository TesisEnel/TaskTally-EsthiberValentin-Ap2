package edu.ucne.tasktally.presentation.mentor.tareas

data class TareaUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,

    val tareaId: String? = null,
    val isEditing: Boolean = false,

    val titulo: String = "",
    val descripcion: String = "",
    val puntos: String = "",
    val estado: String = "Pendiente",
    val diaAsignada: String = "",
    val imgVector: String? = null,
    val showImagePicker: Boolean = false,

    val tituloError: String? = null,
    val descripcionError: String? = null,
    val puntosError: String? = null,
    val diaAsignadaError: String? = null,

    val navigateBack: Boolean = false
)