package one.njk.celestidesk.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.domain.NewBreakRequest
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.network.Stage
import one.njk.celestidesk.repository.RequestRepository
import javax.inject.Inject

@HiltViewModel
class TeamLeadViewModel @Inject constructor(val repository: RequestRepository): ViewModel(), RoleAgreement {
    override val name = "Team Lead here"
    // "Processing" + "Reviewing" = "Pending"
    override val stages = listOf("Pending")
    override fun refreshRequests() {
        viewModelScope.launch {
            repository.refreshPendingRequests()
        }
    }

    override val uiState = MutableStateFlow(RoleUiState(Stage.IN_PROCESS))
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
            repository.makeDecision(decision)
        }
    }

    override fun newRequest(req: NewBreakRequest) {
        // Can't lol
    }
}