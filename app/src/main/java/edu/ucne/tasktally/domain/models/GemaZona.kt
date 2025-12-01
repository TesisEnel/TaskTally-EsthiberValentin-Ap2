package edu.ucne.tasktally.domain.models

data class GemaZona(
    val gemaZonaId: Int = 0,
    val gemaId: Int,
    val zonaId: Int,
    val fechaIngreso: String
)
