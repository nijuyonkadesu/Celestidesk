package one.njk.celestidesk.domain

import kotlinx.datetime.LocalDateTime
import one.njk.celestidesk.network.Stage

data class BreakRequest(
    val id: String,
    val subject: String,
    val message: String,
    val date: LocalDateTime,
    val status: Stage
) {
    val dateShort
        get() = "${date.dayOfMonth} / ${date.month}"
}
