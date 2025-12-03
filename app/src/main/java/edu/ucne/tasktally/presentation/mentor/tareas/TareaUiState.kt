package edu.ucne.tasktally.presentation.mentor.tareas

data class TareaUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,

    val titulo: String = "",
    val descripcion: String = "",
    val puntos: String = "",
    val fechaCreacion: String = "",
    val zonaId: Int? = null,
    val imgVector: String? = null,
    val showImagePicker: Boolean = false,

    val tituloError: String? = null,
    val descripcionError: String? = null,
    val puntosError: String? = null,
    val fechaCreacionError: String? = null,

    val navigateBack: Boolean = false
)