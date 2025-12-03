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

    //tareas
    @Serializable
    data object CreateTarea : Screen
    @Serializable
    data class EditTarea(val tareaId: String) : Screen

    @Serializable
    data object ListTareas : Screen

    //recompensa
    @Serializable
    data object CreateRecompensa : Screen
    @Serializable
    data class EditRecompensa(val recompensaId: String) : Screen
    @Serializable
    data object ListRecompensas : Screen

    //mentor
    @Serializable
    data object MentorTareas : Screen

    @Serializable
    data object MentorTienda : Screen

    @Serializable
    data object MentorPerfil : Screen

}
