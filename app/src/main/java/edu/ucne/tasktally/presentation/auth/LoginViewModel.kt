package edu.ucne.tasktally.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.usecases.auth.GetAuthStatusUseCase
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.auth.LoginUseCase
import edu.ucne.tasktally.domain.usecases.auth.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            getAuthStatusUseCase().collectLatest { loggedIn ->
                _isLoggedIn.value = loggedIn
                if (loggedIn) {

                    getCurrentUserUseCase().collectLatest { userData ->
                        _uiState.value = _uiState.value.copy(
                            currentUser = UserUiState(
                                userId = userData.userId ?: 0,
                                username = userData.username ?: "",
                                email = userData.email ?: "",
                                role = userData.role ?: "",
                                mentorId = userData.mentorId,
                                gemaId = userData.gemaId
                            )
                        )
                    }
                }
            }
        }
    }

    fun onUsernameChanged(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onLoginClick() {
        val currentState = _uiState.value
        if (currentState.isLoading) return

        _uiState.value = currentState.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            when (val result = loginUseCase(currentState.username, currentState.password)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.value = LoginUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUser: UserUiState? = null
)

data class UserUiState(
    val userId: Int,
    val username: String,
    val email: String,
    val role: String = "",
    val mentorId: Int? = null,
    val gemaId: Int? = null
)
