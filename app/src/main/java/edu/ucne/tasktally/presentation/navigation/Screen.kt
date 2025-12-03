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
    data object CreateTarea : Screen

    @Serializable
    data object ListTareas : Screen

    @Serializable
    data object CreateRecompensa : Screen


}
