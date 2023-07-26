package one.njk.celestidesk.network

import com.squareup.moshi.Json
import one.njk.celestidesk.database.DatabasePendingRequest
import one.njk.celestidesk.database.Role
import one.njk.celestidesk.domain.BreakRequest

data class NetworkPendingRequestContainer(
    val requests: List<NetworkPendingRequest>
)

data class NetworkPendingRequest(
    @Json(name = "_id") val id: String,
    val origin: String,
    val subject: String,
    val message: String,
    @Json(name = "requestdate") val requestDate: String,
    val status: Stage,
    val time: String,
    @Json(name = "__v") val v: Int
)
// TODO: Use Date type for requested date

enum class Stage {
    APPROVED, REJECTED, IN_PROCESS, IN_REVIEW
}
fun String.toStage(): Stage {
    val statusMap = mapOf(
        "Approved" to Stage.APPROVED,
        "Rejected" to Stage.REJECTED,
        "Processing" to Stage.IN_PROCESS,
        "Reviewing" to Stage.IN_REVIEW
    )
    return statusMap[this] ?: statusMap["Approved"]!!
}

fun NetworkPendingRequestContainer.asDatabaseModel(): List<DatabasePendingRequest> {
    return requests.map {
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
    return requests.map {
        BreakRequest(
            id = it.id,
            subject = it.subject,
            message = it.message,
            date = it.time,
            status = it.status
        )
    }
}

enum class Decision {
    APPROVED, REJECTED
}

data class Message(
    val message: String
)

data class DecisionRequest(
    val reqID: String,
    val decision: Decision
)

// Transaction Classes
data class Transactions (
    val history: List<History>
)

data class History (
    val id: String,
    val origin: Origin,
    val responder: String,
    val request: HistoryStatus,
    val result: String,
    val time: String,
    val v: Long
)

data class Origin (
    val id: String,
    val name: String,
    val username: String,
    @Json(name = "orghandle") val orgHandle: String,
    val type: Role,
    val v: Long
)

enum class HistoryStatus {
    ACCEPTED, REJECTED, EXPIRED
}

// TODO: Add database, domain models, mappers and a fragment with a view to show transaction (only Manager & Teamlead)
