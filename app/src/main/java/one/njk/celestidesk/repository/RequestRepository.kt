package one.njk.celestidesk.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import one.njk.celestidesk.database.RequestsDao
import one.njk.celestidesk.database.RolesDataStore
import one.njk.celestidesk.database.asDomainModel
import one.njk.celestidesk.network.ApiService
import one.njk.celestidesk.network.asDatabaseModel
import javax.inject.Inject

class RequestRepository @Inject constructor(
    private val requestsDao: RequestsDao,
    private val api: ApiService,
    private val pref: RolesDataStore
    ) {

    suspend fun refreshPendingRequests(){
        withContext(Dispatchers.IO) {
            val token = pref.getToken()
            val requests = api.getPendingRequests("Bearer ${token.token}").asDatabaseModel()
            requestsDao.savePendingRequests(requests)
        }
    }

    val requestsFlow = requestsDao.getPendingRequests().flowOn(Dispatchers.Default).map {
        it.asDomainModel()
    }
}