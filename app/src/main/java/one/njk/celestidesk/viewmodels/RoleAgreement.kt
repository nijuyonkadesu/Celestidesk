package one.njk.celestidesk.viewmodels

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.MutableStateFlow
import one.njk.celestidesk.database.Role
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.network.NetworkResult
import one.njk.celestidesk.network.Stage

interface RoleAgreement {
    val name: Role
    val requestsFlow: LiveData<List<BreakRequest>>
    val uiState: MutableStateFlow<RoleUiState>
    val state: LiveData<RoleUiState>
    val stages: List<String>
    fun updateStage(stage: Stage)
    fun refreshRequests()
    fun decide(decision: DecisionRequest, breakRequest: BreakRequest)
}

data class RoleUiState(
    val stage: Stage,
    val isLoading: Boolean = false,
    val status: NetworkResult<Unit> = NetworkResult.ItsOk()
)