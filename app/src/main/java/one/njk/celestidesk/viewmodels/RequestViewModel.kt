package one.njk.celestidesk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import one.njk.celestidesk.domain.NewBreakRequest
import one.njk.celestidesk.domain.asNetworkModel
import one.njk.celestidesk.repository.RequestRepository
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    val repository: RequestRepository
): ViewModel() {

    private val uiState = MutableStateFlow(UiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = uiState.flatMapLatest {
        flowOf(it).flowOn(Dispatchers.IO)
    }.asLiveData()
    fun newRequest(req: NewBreakRequest) {
        viewModelScope.launch {
            repository.createNewRequest(req.asNetworkModel())
        }
    }
    data class UiState(
        val isLoading: Boolean = false
    )
}