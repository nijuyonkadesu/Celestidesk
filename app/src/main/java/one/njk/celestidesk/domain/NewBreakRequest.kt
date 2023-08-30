package one.njk.celestidesk.domain

import one.njk.celestidesk.network.Stage

data class NewBreakRequest(
    val message: String,
    val status: Stage,
    val emergency: Boolean,
    val from: Long,
    val to: Long,
)

// TODO: Test sending date with long, then LocalDateTime, then finally String
