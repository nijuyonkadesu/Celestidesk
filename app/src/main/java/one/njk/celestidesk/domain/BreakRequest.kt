package one.njk.celestidesk.domain

import one.njk.celestidesk.network.BreakState

data class BreakRequest(
    val id: String,
    val subject: String,
    val message: String,
    val date: String = "2023-01-03",
    val status: BreakState
) {
    val dateShort
        get() = date.slice(5..9)
}
// TODO: Use Date type for requested date