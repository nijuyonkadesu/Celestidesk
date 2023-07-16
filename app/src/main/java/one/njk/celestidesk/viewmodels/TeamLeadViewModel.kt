package one.njk.celestidesk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.repository.RequestRepository
import javax.inject.Inject

@HiltViewModel
class TeamLeadViewModel @Inject constructor(val repository: RequestRepository): ViewModel(), RoleAgreement {
    override val name = "Team Lead here"
    override fun refreshRequests() {
        viewModelScope.launch {
            repository.refreshPendingRequests()
        }
    }

    override val requestsFlow = repository.requestsFlow.asLiveData()

    override fun decide(decision: DecisionRequest) {
        viewModelScope.launch {
            repository.makeDecision(decision)
        }
    }
}