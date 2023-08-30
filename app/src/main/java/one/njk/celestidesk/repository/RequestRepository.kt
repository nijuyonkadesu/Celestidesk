package one.njk.celestidesk.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import one.njk.celestidesk.database.RequestsDao
import one.njk.celestidesk.database.RolesDataStore
import one.njk.celestidesk.database.TransactionDao
import one.njk.celestidesk.database.asDomainModel
import one.njk.celestidesk.database.asHistoryDomainModel
import one.njk.celestidesk.domain.History
import one.njk.celestidesk.network.ApiService
import one.njk.celestidesk.network.Decision
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.network.Stage
import one.njk.celestidesk.network.asDatabaseModel
import one.njk.celestidesk.utils.failsafe
import one.njk.celestidesk.utils.sendEmail
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val requestsDao: RequestsDao,
    private val transactionDao: TransactionDao,
    private val api: ApiService,
    private val pref: RolesDataStore
    ) {

    suspend fun refreshPendingRequests(){
        failsafe {
            val token = pref.getToken()
            val requests = api.getPendingRequests("Bearer ${token.token}").asDatabaseModel()
            requestsDao.invalidateCache()
            requestsDao.savePendingRequests(requests)
        }
    }

    // Requests in ACCEPTED or REJECTED
    fun getRequestsFlow(stage: Stage) = requestsDao.getRequestsFlow(stage).flowOn(Dispatchers.Default).map {
        it.asDomainModel()
    }

    // Requests in IN_REVIEW + IN_PROCESS
    fun getPendingRequestsFlow() = requestsDao.getPendingRequestsFlow().flowOn(Dispatchers.Default).map {
        it.asDomainModel()
    }

    fun getTransactionsFlow() = transactionDao.getTransactionsFlow().flowOn(Dispatchers.Default).map {
        it.asHistoryDomainModel()
    }

    fun allOrSearchTransactionsFlow(term: String): Flow<List<History>> {

        return if(term.isNotEmpty())
            transactionDao.searchTransactions("*$term*").flowOn(Dispatchers.Default).map {
                it.asHistoryDomainModel()
            }
        else
            getTransactionsFlow()
    }

    suspend fun makeDecision(decision: DecisionRequest) {
        failsafe {
            val token = pref.getToken()
            val message = api.makeDecision("Bearer ${token.token}", decision)
            Log.d("network", message.message)
            refreshPendingRequests()
        }
    }

    suspend fun updateTransactions(){
        failsafe {
            val token = pref.getToken()
            val transactions = api.getPastTransactions("Bearer ${token.token}")
            Log.d("network", transactions.toString())
            transactionDao.updateTransactions(transactions.asDatabaseModel())
        }
    }

    fun sendMailFromRequest(subject: String, body: String, decision: Decision) {
        sendEmail(subject, "Your request `$body` got $decision by MANAGER")
    }
    // TODO: Make it more dynamic and sensible - Replace with SMS
}