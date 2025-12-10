package edu.ucne.tasktally.presentation.mentor.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.local.preferences.AuthPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val authPreferencesManager: AuthPreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(PerfilUiState())
    val state: StateFlow<PerfilUiState> = _state.asStateFlow()

    init {
        loadPerfilData()
    }

    private fun loadPerfilData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val username = authPreferencesManager.username.first() ?: "Usuario"

            _state.update {
                it.copy(
                    isLoading = false,
                    userName = username,
                )
            }
        }
    }
}