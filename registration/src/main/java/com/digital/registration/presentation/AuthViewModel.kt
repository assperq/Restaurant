package com.digital.registration.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.digital.profile.domain.ProfileRepository
import com.digital.profile.presentation.ProfileViewModel
import com.digital.registration.domain.AuthRepository
import com.digital.registration.presentation.navigation.AuthRoutes
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val profileViewModel: ProfileViewModel
) : ViewModel() {

    private val _state : MutableStateFlow<State> = MutableStateFlow(State.DefaultState)
    val state = _state.asStateFlow()

    sealed class State {
        data object DefaultState : State()
        data class Error(val error : Throwable) : State()
        data class Success(val route : String) : State()
        data object Loading : State()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, error ->
        println(error)
        _state.value = State.Error(error)
    }
    private val authScope = CoroutineScope(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler)

    fun signIn(email : String, password : String) {
        authScope.launch {
            _state.value = State.Loading
            StringChecker.checkMailString(email)
            StringChecker.checkPassword(password)
            repository.signIn(email, password)
            _state.value = State.Success(AuthRoutes.SignInRoute.route)
            profileViewModel.fetchProfile()
        }
    }

    fun signUp(email: String, password: String, contactInfo : String) {
        authScope.launch {
            _state.value = State.Loading
            if (contactInfo.isEmpty()) {
                throw Exception("Поле с контактной информацией обязательно должно быть заполнено")
            }
            StringChecker.checkMailString(email)
            StringChecker.checkPassword(password)
            repository.signUp(email, password, contactInfo)
            _state.value = State.Success(AuthRoutes.SignUpRoute.route)
            profileViewModel.fetchProfile()
        }
    }

    fun signOut() {
        profileViewModel.clearUser()
        authScope.launch {
            repository.signOut()
        }
    }

    fun clearError() {
        _state.value = State.DefaultState
    }
}