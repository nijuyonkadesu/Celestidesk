package one.njk.celestidesk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val transactions = repository.getTransactionsFlow().asLiveData()
}