package edu.ucne.tasktally.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.tasktally.data.remote.Resource
import edu.ucne.tasktally.domain.usecase.gema.CheckGemaZoneAccessUseCase
import edu.ucne.tasktally.domain.usecases.auth.GetAuthStatusUseCase
import edu.ucne.tasktally.domain.usecases.auth.GetCurrentUserUseCase
import edu.ucne.tasktally.domain.usecases.auth.LoginUseCase
import edu.ucne.tasktally.domain.usecases.auth.LogoutUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAuthStatusUseCase: GetAuthStatusUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkGemaZoneAccessUseCase: CheckGemaZoneAccessUseCase
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
                        val userUiState = UserUiState(
                            userId = userData.userId ?: 0,
                            username = userData.username ?: "",
                            email = userData.email ?: "",
                            role = userData.role ?: "",
                            mentorId = userData.mentorId,
                            gemaId = userData.gemaId
                        )

                        _uiState.value = _uiState.value.copy(currentUser = userUiState)

                        if (userUiState.role == "gema" && userUiState.gemaId != null) {
                            checkZoneAccess(userUiState.gemaId,userData.zoneId ?: 0)
                        } else {
                            _uiState.value = _uiState.value.copy(hasZoneAccess = true)
                        }
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

    private suspend fun checkZoneAccess(gemaId: Int, zoneId: Int) {
        when (val result = checkGemaZoneAccessUseCase(gemaId, zoneId)) {
            is Resource.Success -> {
                _uiState.value = _uiState.value.copy(hasZoneAccess = result.data ?: false)
            }
            is Resource.Error -> {
                _uiState.value = _uiState.value.copy(
                    hasZoneAccess = false,
                    error = result.message
                )
            }
            is Resource.Loading -> {}
        }
    }

    fun refreshZoneAccess() {
        viewModelScope.launch {
            val currentUser = _uiState.value.currentUser
            if (currentUser?.role == "gema" && currentUser.gemaId != null) {
                val userData = getCurrentUserUseCase().first()
                val zoneId = userData.zoneId
                if (zoneId != null && zoneId > 0) {
                    checkZoneAccess(currentUser.gemaId, zoneId)
                }
            }
        }
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentUser: UserUiState? = null,
    val hasZoneAccess: Boolean = false
)

data class UserUiState(
    val userId: Int,
    val username: String,
    val email: String,
    val role: String = "",
    val mentorId: Int? = null,
    val gemaId: Int? = null
)
