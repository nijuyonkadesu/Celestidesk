package one.njk.celestidesk.viewmodels

import android.util.Log
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.network.NetworkResult
import one.njk.celestidesk.network.Stage
import one.njk.celestidesk.repository.RequestRepository
import javax.inject.Inject

@HiltViewModel
class CrisisManagerViewModel @Inject constructor(val repository: RequestRepository): ViewModel(), RoleAgreement {
    override val name = "Crisis Manager here"
    // "Processing" + "Reviewing" = "Pending"
    override val stages = listOf("Pending")
    override fun refreshRequests() {
        viewModelScope.launch {
            repository.refreshPendingRequests()
        }
    }

    override val uiState = MutableStateFlow(RoleUiState(Stage.IN_PROCESS))

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state = uiState.flatMapLatest {
        flowOf(it).flowOn(Dispatchers.IO)
    }.asLiveData()

    override fun updateStage(stage: Stage){
        Log.d("stage", "$stage")
        uiState.value = uiState.value.copy(
            stage = stage
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val requestsFlow = uiState.flatMapLatest {
        if(it.stage == Stage.PENDING) {
            repository.getPendingRequestsFlow()
        }
        else {
            repository.getRequestsFlow(it.stage)
        }
    }.asLiveData()

    override fun decide(decision: DecisionRequest, breakRequest: BreakRequest) {
        viewModelScope.launch {
            uiState.update {
                it.copy(isLoading = true, status = NetworkResult.ItsOk())
            }
            val status = repository.makeDecision(decision)
            uiState.update {
                it.copy(isLoading = false, status = status)
            }
        }
    }
}