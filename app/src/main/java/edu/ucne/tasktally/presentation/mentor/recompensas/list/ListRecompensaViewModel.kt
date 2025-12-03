package edu.ucne.tasktally.presentation.mentor.recompensas.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.domain.usecases.mentor.DeleteRecompensaUseCase
import edu.ucne.tasktally.domain.usecases.mentor.ObserveRecompensaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListRecompensaViewModel @Inject constructor(
    private val observeRecompensaUseCase: ObserveRecompensaUseCase,
    private val deleteRecompensaUseCase: DeleteRecompensaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListRecompensaUiState(isLoading = true))
    val state: StateFlow<ListRecompensaUiState> = _state.asStateFlow()

    init {
        onEvent(ListRecompensaUiEvent.Load)
    }

    fun onEvent(event: ListRecompensaUiEvent) {
        when (event) {
            ListRecompensaUiEvent.Load -> observe()
            is ListRecompensaUiEvent.Delete -> onDelete(event.id)
            ListRecompensaUiEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is ListRecompensaUiEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is ListRecompensaUiEvent.ShowMessage -> _state.update { it.copy(message = event.message) }
        }
    }

    private fun observe() {
        viewModelScope.launch {
            observeRecompensaUseCase().collectLatest { list ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        recompensas = list,
                        message = null
                    )
                }
            }
        }
    }

    private fun onDelete(id: String) {
        viewModelScope.launch {
            deleteRecompensaUseCase(id)
            onEvent(ListRecompensaUiEvent.ShowMessage("Recompensa eliminada"))
        }
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(
                navigateToCreate = false,
                navigateToEditId = null
            )
        }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }
}