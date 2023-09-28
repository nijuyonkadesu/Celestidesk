package one.njk.celestidesk.domain

import one.njk.celestidesk.network.NetworkNewBreakRequest

data class NewBreakRequest(
    val subject: String,
    val message: String,
    var emergency: Boolean,
    val from: String,
    val to: String,
)

fun NewBreakRequest.asNetworkModel(): NetworkNewBreakRequest {
    return NetworkNewBreakRequest(
        this.subject,
        this.message,
        this.emergency,
        this.from,
        this.to
    )
}
