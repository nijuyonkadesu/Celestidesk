package one.njk.celestidesk.viewmodels

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.MutableStateFlow
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.domain.NewBreakRequest
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.network.Stage

interface RoleAgreement {
    val name: String
    val requestsFlow: LiveData<List<BreakRequest>>
    val uiState: MutableStateFlow<RoleUiState>
    val stages: List<String>
    fun updateStage(stage: Stage)
    fun refreshRequests()
    fun decide(decision: DecisionRequest, breakRequest: BreakRequest)
    fun newRequest(req: NewBreakRequest)
}

data class RoleUiState(
    val stage: Stage,
)