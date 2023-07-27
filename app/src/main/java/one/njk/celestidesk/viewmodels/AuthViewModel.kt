package one.njk.celestidesk.viewmodels

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import one.njk.celestidesk.network.auth.AuthRepository
import one.njk.celestidesk.network.auth.model.AuthLoginRequest
import one.njk.celestidesk.network.auth.model.AuthResult
import one.njk.celestidesk.network.auth.model.AuthSignUpRequest
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val api: AuthRepository
) : ViewModel() {

    private val uiState = MutableStateFlow(UiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: LiveData<UiState> = uiState.flatMapLatest { uiState ->
        flowOf(uiState).flowOn(Dispatchers.Default)
    }.asLiveData()

    private suspend fun signUp(req: AuthSignUpRequest) {

        uiState.update {
            it.copy(isLoading = true)
        }
        val result = api.signUp(req)
        uiState.update {
            it.copy(isLoading = false, authResult = result)
        }
    }

    suspend fun logIn(username: String, password: String){
        uiState.update {
            it.copy(isLoading = true)
        }
        val result = api.logIn(
            AuthLoginRequest(username, password)
        )
        uiState.update {
            it.copy(isLoading = false, authResult = result)
        }
    }
    // TODO: Validators for LogIn too & check if coroutines are used properly here

    suspend fun authenticate(){
        uiState.update {
            it.copy(isLoading = true)
        }
        val result = api.authenticate()
        uiState.update {
            it.copy(isLoading = false, authResult = result)
        }
    }

    suspend fun validateAndSignUp(raw: AuthSignUpRequest): Boolean {

        val req = AuthSignUpRequest(
            name = raw.name.trim(),
            username = raw.username.trim(),
            orgHandle = raw.orgHandle,
            type = raw.type,
            password = raw.password
        )

        Log.d("sign up", "status: Checking")

        val passwordRegex = "^(?=.*\\d)(?=.*[A-Z]).{8,}$".toRegex()

        if (passwordRegex.matches(req.password)
                && Patterns.EMAIL_ADDRESS.matcher("200ok09@svce.ac.in").matches()
                && req.name.isNotEmpty()
                && req.username.isNotEmpty()) {
            signUp(req)
            return true
        }
        return false
        // TODO: Add ROOM no, parent Number
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val authResult: AuthResult<Unit> = AuthResult.UnknownError(),
)