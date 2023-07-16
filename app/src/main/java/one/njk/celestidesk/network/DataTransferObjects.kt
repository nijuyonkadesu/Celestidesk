package one.njk.celestidesk.network

import com.squareup.moshi.Json
import one.njk.celestidesk.database.DatabasePendingRequest

data class NetworkPendingRequestContainer(
    val pendingRequests: List<NetworkPendingRequest>
)

data class NetworkPendingRequest(
    @Json(name = "_id") val id: String,
    val origin: String,
    val message: String,
    val requestDate: String,
    val status: BreakState,
    val time: String,
)
// TODO: Use Date type for requested date

enum class BreakState {
    APPROVED, REJECTED, IN_PROCESS, IN_REVIEW
}

fun NetworkPendingRequestContainer.asDatabaseModel(): List<DatabasePendingRequest> {
    return pendingRequests.map {
        DatabasePendingRequest(
            id = it.id,
            origin = it.origin,
            message = it.message,
            requestDate = it.requestDate,
            status = it.status,
            time = it.time
        )
    }
}
