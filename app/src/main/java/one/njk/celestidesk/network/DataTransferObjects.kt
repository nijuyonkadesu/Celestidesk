package one.njk.celestidesk.network

import com.squareup.moshi.Json

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
    APPROVED, DENIED, IN_PROCESS, IN_REVIEW
}