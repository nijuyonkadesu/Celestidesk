package one.njk.celestidesk.domain

import one.njk.celestidesk.network.NetworkNewRequest

data class NewBreakRequest(
    val subject: String,
    val message: String,
    var emergency: Boolean,
    val from: String,
    val to: String,
)

fun NewBreakRequest.asNetworkModel(): NetworkNewRequest {
    return NetworkNewRequest(
        this.subject,
        this.message,
        this.emergency,
        this.from,
        this.to
    )
}
