package edu.ucne.tasktally.data.remote.DTOs.mentor

data class MentorTareasRecompensasResponse(
    val tareas: List<TareaDto> = emptyList(),
    val recompensas: List<RecompensaDto> = emptyList()
)

data class TareaDto(
    val tareasGroupId: Int? = 0,
    val mentorId: Int? = 0,
    val titulo: String? = "",
    val descripcion: String? = "",
    val puntos: Int? = 0,
    val repetir: Int? = 0,
    val dias: String? = "",
    val nombreImgVector: String? = ""
)

data class RecompensaDto(
    val recompensaId: Int? = 0,
    val titulo: String? = "",
    val descripcion: String? = "",
    val precio: Int? = 0,
    val isDisponible: Boolean? = false,
    val nombreImgVector: String? = "",
    val gemas: List<GemaDto>? = emptyList()
)

data class GemaDto(
    val gemaId: Int = 0,
    val nombre: String = ""
)
