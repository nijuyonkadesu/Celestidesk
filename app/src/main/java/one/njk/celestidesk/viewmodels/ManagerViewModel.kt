package one.njk.celestidesk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.network.Decision
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.network.Stage
import one.njk.celestidesk.repository.RequestRepository
import javax.inject.Inject

@HiltViewModel
class ManagerViewModel @Inject constructor(val repository: RequestRepository): ViewModel(), RoleAgreement {
    override val name = "Manager Here"
    // "Processing" + "Reviewing" = "Pending"
    override val stages = listOf("Pending")
    override fun refreshRequests() {
        viewModelScope.launch {
            repository.refreshPendingRequests()
        }
    }

    override val uiState = MutableStateFlow(RoleUiState(Stage.IN_PROCESS))
    override fun updateStage(stage: Stage){
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.makeDecision(decision)
            if(decision.decision == Decision.ACCEPTED) {
                fireMail(decision, breakRequest)
            }
        }
    }

    private fun fireMail(decision: DecisionRequest, breakRequest: BreakRequest) {
        // TODO: Send SMS instead, it's fine if it's paid
        viewModelScope.launch(Dispatchers.IO) {
            repository.sendMailFromRequest(breakRequest.subject, breakRequest.message, decision.decision)
        }
    }
}