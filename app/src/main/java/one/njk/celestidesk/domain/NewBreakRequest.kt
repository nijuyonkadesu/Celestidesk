package one.njk.celestidesk.domain

import kotlinx.datetime.LocalDateTime
import one.njk.celestidesk.network.Stage

data class NewBreakRequest(
    val message: String,
    val status: Stage,
    val date: LocalDateTime
)
