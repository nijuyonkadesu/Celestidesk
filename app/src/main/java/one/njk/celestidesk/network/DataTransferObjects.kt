package one.njk.celestidesk.network

import com.squareup.moshi.Json
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.database.DatabasePendingRequest
import one.njk.celestidesk.database.DatabaseTransaction

data class NetworkPendingRequestContainer(
    val requests: List<NetworkPendingRequest>
)

data class NetworkPendingRequest(
    @Json(name = "_id") val id: String,
    val origin: String,
    val subject: String,
    val message: String,
    @Json(name = "requestdate") val requestDate: String,
    var emergency: Boolean = false,
    val status: Stage,
    var time: String = "2023-07-31T13:12:01.129Z",
    var from: String = "2023-07-31T13:12:01.129Z",
    var to: String = "2023-07-31T13:12:01.129Z",
    @Json(name = "__v") val v: Int
)


enum class Stage {
    ACCEPTED, REJECTED, IN_PROCESS, IN_REVIEW, PENDING
}
// PENDING Doesn't exist!, here, using 'pending' to show results from both IN_REVIEW & IN_PROCESS
fun String.toStage(): Stage {
    val statusMap = mapOf(
        "Pending" to Stage.PENDING,
        "Accepted" to Stage.ACCEPTED,
        "Rejected" to Stage.REJECTED,
        "Processing" to Stage.IN_PROCESS,
        "Reviewing" to Stage.IN_REVIEW
    )
    return statusMap[this] ?: statusMap["Accepted"]!!
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
data class NetworkTransactionsContainer (
    val history: List<ActionEmbed>
)
data class ActionEmbed (
    @Json(name = "_id") val id: String,
    val origin: CreatorEmbed,
    val responder: CreatorEmbed,
    val request: NetworkRequestEmbed?,
    val result: ActionResult,
    val time: String,
    @Json(name = "__v") val v: Int
)
data class CreatorEmbed (
    val name: String
)
data class NetworkRequestEmbed (
    val subject: String,
    val message: String,
    val status: Stage,
    val from: String,
    val to: String
)
enum class ActionResult {
    ACCEPTED, REJECTED, EXPIRED
} // In UI, Stage -> ActionStatus (eg: IN_REVIEW -> REJECTED)

fun NetworkTransactionsContainer.asDatabaseModel(): List<DatabaseTransaction> {
    return history.map {
        DatabaseTransaction(
            id = it.id,
            origin = it.origin.name,
            subject = it.request?.subject ?: "Not Found",
            message = it.request?.message ?: "Not Found",
            from = it.request?.from?.toDate() ?: LocalDateTime(2023, 8, 1, 0, 0, 0, 0),
            to = it.request?.to?.toDate() ?: LocalDateTime(2023, 8, 2, 0, 0, 0, 0),
            wasIn = it.request?.status ?: Stage.REJECTED,
            nowIn = it.result,
            responder = it.responder.name
        )
    }
}

// --------------------- Transaction Model [ENDS] -------------------------- //

data class NetworkNewRequest(
    val message: String,
    val status: Stage,
    val date: String,
)
