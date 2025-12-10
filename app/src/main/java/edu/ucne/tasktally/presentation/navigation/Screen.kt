package edu.ucne.tasktally.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data object Register : Screen

    @Serializable
    data object Tareas : Screen

    @Serializable
    data object Tienda : Screen

    @Serializable
    data object Perfil : Screen

    @Serializable
    data object Zona : Screen

    @Serializable
    data object ZoneAccess : Screen

    @Serializable
    data object CreateTarea : Screen

    @Serializable
    data class EditTarea(val tareaId: String) : Screen

    @Serializable
    data object ListTareas : Screen

    @Serializable
    data object CreateRecompensa : Screen

    @Serializable
    data class EditRecompensa(val recompensaId: String) : Screen

    @Serializable
    data object ListRecompensas : Screen

    @Serializable
    data object MentorTareas : Screen

    @Serializable
    data object MentorTienda : Screen

    @Serializable
    data object MentorPerfil : Screen
}