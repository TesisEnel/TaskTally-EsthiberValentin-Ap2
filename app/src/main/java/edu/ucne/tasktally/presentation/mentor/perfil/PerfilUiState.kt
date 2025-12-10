package edu.ucne.tasktally.presentation.mentor.perfil

data class PerfilUiState (
    val isLoading: Boolean = true,
    val userName: String = "",
    val completedTasks: Int = 0,
    val currentStreak: Int = 0,
    val totalPoints: Int = 0
)

