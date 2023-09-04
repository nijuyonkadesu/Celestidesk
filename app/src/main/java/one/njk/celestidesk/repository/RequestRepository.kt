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
import one.njk.celestidesk.network.NetworkNewRequest
import one.njk.celestidesk.network.Stage
import one.njk.celestidesk.network.asDatabaseModel
import one.njk.celestidesk.utils.failsafe
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

    /**
     * Requests in ACCEPTED or REJECTED stages
     * */
    fun getRequestsFlow(stage: Stage) = requestsDao.getRequestsFlow(stage).flowOn(Dispatchers.Default).map {
        Log.d("network", "For Employee to see $it")
        it.asDomainModel()
    }

    /**
     * Requests in IN_REVIEW and IN_PROCESS stages
     * */
    fun getPendingRequestsFlow() = requestsDao.getPendingRequestsFlow().flowOn(Dispatchers.Default).map {
        Log.d("network", "Waiting for Approval $it")
        it.asDomainModel()
    }

    private fun getTransactionsFlow() = transactionDao.getTransactionsFlow().flowOn(Dispatchers.Default).map {
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

    suspend fun createNewRequest(req: NetworkNewRequest) {
        failsafe {
            Log.d("new", "$req")
            val token = pref.getToken()
            api.createNewRequest("Bearer ${token.token}", req)
        }
    }

    fun sendMailFromRequest(subject: String, body: String, decision: Decision) {
        sendEmail(subject, "Your request `$body` got $decision by MANAGER")
    }
    // TODO: Make it more dynamic and sensible - Replace with SMS
}