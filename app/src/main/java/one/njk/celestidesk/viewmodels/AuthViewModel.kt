package one.njk.celestidesk.viewmodels

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
import one.njk.celestidesk.data.auth.AuthRepository
import one.njk.celestidesk.data.auth.model.AuthLoginRequest
import one.njk.celestidesk.data.auth.model.AuthResult
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

    suspend fun authenticate(){
        uiState.update {
            it.copy(isLoading = true)
        }
        val result = api.authenticate()
        uiState.update {
            it.copy(isLoading = false, authResult = result)
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val authResult: AuthResult<Unit> = AuthResult.UnknownError(),
)