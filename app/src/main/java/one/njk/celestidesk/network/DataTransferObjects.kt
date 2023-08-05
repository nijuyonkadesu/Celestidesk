package one.njk.celestidesk.network

import com.squareup.moshi.Json
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.database.DatabasePendingRequest
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
    var time: String = "2023-07-31T13:12:01.129Z",
    var from: String = "2023-07-31T13:12:01.129Z",
    var to: String = "2023-07-31T13:12:01.129Z",
    @Json(name = "__v") val v: Int
)


enum class Stage {
    ACCEPTED, REJECTED, IN_PROCESS, IN_REVIEW
}
// TODO:  IN_PROCESS -> TEAM_LEAD, IN_REVIEW (Final) -> MANAGER
fun String.toStage(): Stage {
    val statusMap = mapOf(
        "Accepted" to Stage.ACCEPTED,
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
            time = it.time.toDate(),
            from = it.from.toDate(),
            to = it.to.toDate()
        )
    }
}

fun String.toDate(): LocalDateTime {
     return this.slice(0 until this.lastIndex).toLocalDateTime()
    // shouldn't have Z designator at the end
}

fun NetworkPendingRequestContainer.asDomainModel(): List<BreakRequest> {
    return requests.map {
        BreakRequest(
            id = it.id,
            subject = it.subject,
            message = it.message,
            date = it.time.slice(0 until it.time.lastIndex).toLocalDateTime(),
            status = it.status
        )
    }
}

enum class Decision {
    ACCEPTED, REJECTED
}

data class Message(
    val message: String
)

data class DecisionRequest(
    val reqID: String,
    val decision: Decision
)

// --------------------- Transaction Model [STARTS] -------------------------- //
data class NetworkTransactions (
    val history: List<ActionEmbed>
)
data class ActionEmbed (
    @Json(name = "_id") val id: String,
    val origin: CreatorEmbed,
    val responder: CreatorEmbed,
    val request: RequestEmbed?,
    val result: ActionResult,
    val time: String,
    @Json(name = "__v") val v: Int
)
data class CreatorEmbed (
    val name: String
)
data class RequestEmbed (
    val subject: String,
    val message: String,
    val status: Stage,
    val from: String,
    val to: String
)
enum class ActionResult {
    ACCEPTED, REJECTED, EXPIRED
} // In UI, Stage -> ActionStatus (eg: IN_REVIEW -> REJECTED)

// --------------------- Transaction Model [ENDS] -------------------------- //

// TODO: Add database, domain models, mappers and a fragment with a view to show transaction (only Manager & Teamlead)
