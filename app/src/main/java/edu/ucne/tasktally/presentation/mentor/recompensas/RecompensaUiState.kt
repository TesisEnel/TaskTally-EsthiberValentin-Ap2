package edu.ucne.tasktally.presentation.mentor.recompensas

data class RecompensaUiState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null,

    val titulo: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val imgVector: String? = null,
    val showImagePicker: Boolean = false,

    val tituloError: String? = null,
    val descripcionError: String? = null,
    val precioError: String? = null,

    val navigateBack: Boolean = false
)
