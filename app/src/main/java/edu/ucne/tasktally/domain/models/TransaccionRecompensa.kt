package edu.ucne.tasktally.domain.models

data class TransaccionRecompensa(
    val transaccionRecompensaId: Int = 0,
    val gemaId: Int,
    val recompensaId: Int,
    val costoPuntos: Double,
    val fechaTransaccion: String
)
