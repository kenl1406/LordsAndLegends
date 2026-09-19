package com.lordsandlegends.crew.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lordsandlegends.crew.CrewApp
import com.lordsandlegends.crew.data.model.Staff
import com.lordsandlegends.crew.data.repository.StaffRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Sign-in state for the whole screen in one object, so the UI can never show a
 * spinner and an error at the same time.
 */
data class LoginState(
    val loading: Boolean = false,
    val staff: Staff? = null,
    val error: String? = null,
)

class LoginViewModel(
    private val client: SupabaseClient,
    private val staffRepo: StaffRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun signIn(emailInput: String, passwordInput: String) = viewModelScope.launch {
        _state.update { it.copy(loading = true, error = null) }
        runCatching {
            client.auth.signInWith(Email) {
                this.email = emailInput
                this.password = passwordInput
            }
            val uid = client.auth.currentUserOrNull()?.id
                ?: error("No session after sign-in")
            staffRepo.byAuthUserId(uid)
        }.onSuccess { staff ->
            _state.update { it.copy(loading = false, staff = staff) }
        }.onFailure {
            // Deliberately vague: naming the reason tells an attacker which
            // addresses are registered.
            _state.update {
                it.copy(loading = false, error = "Email or password is incorrect")
            }
        }
    }

    fun clearError() = _state.update { it.copy(error = null) }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CrewApp
                LoginViewModel(app.supabase, StaffRepository(app.supabase))
            }
        }
    }
}
