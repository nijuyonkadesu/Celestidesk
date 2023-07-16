package one.njk.celestidesk.network

import com.squareup.moshi.Json
import one.njk.celestidesk.database.DatabasePendingRequest
import one.njk.celestidesk.domain.BreakRequest

data class NetworkPendingRequestContainer(
    val pendingRequests: List<NetworkPendingRequest>
)

data class NetworkPendingRequest(
    @Json(name = "_id") val id: String,
    val origin: String,
    val subject: String,
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
            subject = it.subject,
            message = it.message,
            requestDate = it.requestDate,
            status = it.status,
            time = it.time
        )
    }
}

fun NetworkPendingRequestContainer.asDomainModel(): List<BreakRequest> {
    return pendingRequests.map {
        BreakRequest(
            id = it.id,
            subject = it.subject,
            message = it.message,
            date = it.requestDate,
            status = it.status
        )
    }
}
