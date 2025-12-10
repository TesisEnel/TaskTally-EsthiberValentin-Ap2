package edu.ucne.tasktally.presentation.zona

data class ZoneAccessUiState(
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val zoneCode: String = "",
    val hasZoneAccess: Boolean = false
)
