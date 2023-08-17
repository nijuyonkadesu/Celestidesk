package one.njk.celestidesk.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.network.Stage

data class BreakRequest(
    val name: String,
    val id: String,
    val subject: String,
    val message: String,
    val date: LocalDateTime,
    val status: Stage,
    val from: LocalDateTime,
    val to: LocalDateTime,
) {
    // Target Format: "9 Day(s): 8 July to 17 July."

    private var totalDays = run {
        var days = (to.date - from.date).days
        if(days == 0) days = 1
        days
    }

    val dateShort
        get() =
            "$totalDays Day(s): ${from.dayOfMonth} ${from.month} to ${to.dayOfMonth} ${to.month}"

    fun getProgress(): Int {

        val currentMoment = Clock.System.now()
        val datetimeInUtc = currentMoment.toLocalDateTime(TimeZone.UTC)

        var elapsed = (to.date - datetimeInUtc.date).days
        if (elapsed <= 0)
            elapsed = totalDays

        return (elapsed / totalDays) * 100
    }

    // TODO: Replace these with calling string resources
}
