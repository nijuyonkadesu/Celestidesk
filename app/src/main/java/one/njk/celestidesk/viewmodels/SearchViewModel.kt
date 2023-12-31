package one.njk.celestidesk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import one.njk.celestidesk.repository.RequestRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: RequestRepository
): ViewModel() {

    init {
        viewModelScope.launch { repository.updateTransactions() }
    }

    private val searchTerm = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResultsFlow = searchTerm.flatMapLatest {
        repository.allOrSearchTransactionsFlow(it)
    }.debounce(300).distinctUntilChanged().asLiveData()

    fun search(term: String) {
        searchTerm.update { term }
    }

}