package one.njk.celestidesk.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import one.njk.celestidesk.database.RequestsDao
import one.njk.celestidesk.database.RolesDataStore
import one.njk.celestidesk.database.asDomainModel
import one.njk.celestidesk.network.ApiService
import one.njk.celestidesk.network.DecisionRequest
import one.njk.celestidesk.network.Stage
import one.njk.celestidesk.network.asDatabaseModel
import retrofit2.HttpException
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val requestsDao: RequestsDao,
    private val api: ApiService,
    private val pref: RolesDataStore
    ) {

    suspend fun refreshPendingRequests(){
        withContext(Dispatchers.IO) {
            val token = pref.getToken()
            try {
                val requests = api.getPendingRequests("Bearer ${token.token}").asDatabaseModel()
                requestsDao.savePendingRequests(requests)

            } catch (e: HttpException) {
                Log.d("network", "${e.message}")

            } catch (e: Exception){
                Log.d("network", "${e.message}")
            }
        }
    }

    fun getRequestsFlow(stage: Stage) = requestsDao.getRequestsFlow(stage).flowOn(Dispatchers.Default).map {
        it.asDomainModel()
    }

    suspend fun makeDecision(decision: DecisionRequest) {
        try {
            requestsDao.updateRequest(decision.reqID, decision.decision)
            val message = api.makeDecision(decision)
            Log.d("network", message.message)
            refreshPendingRequests()

        } catch (e: HttpException){
            Log.d("network", "${e.message}")

        } catch (e: Exception){
            Log.d("network", "fatal: ${e.message}")
        }
    }
}