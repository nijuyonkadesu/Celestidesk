package one.njk.celestidesk.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import one.njk.celestidesk.network.ActionResult
import one.njk.celestidesk.network.Stage

data class History (
    val origin: String,
    val subject: String,
    val message: String,
    val from: LocalDateTime,
    val to: LocalDateTime,
    val wasIn: Stage,
    val nowIn: ActionResult,
    val responder: String,
) {
    val fromShort
        get() = "${from.dayOfMonth} ${from.month.toString().slice(0..2)}"
    val toShort
        get() = "${to.dayOfMonth} ${to.month.toString().slice(0..2)}"

    private var totalDays = run {
        var days = (to.date - from.date).days
        if(days == 0) days = 1
        days
    }
    val dateRange
        get() = "$totalDays Day(s) off"

    val action
        get() = "$wasIn → $nowIn"
}
