package one.njk.celestidesk.domain

import android.util.Log
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.network.Stage

/**
 * Domain layer representation of Pending Requests [one.njk.celestidesk.network.NetworkPendingRequestContainer]
 * in any one of the stage [one.njk.celestidesk.network.Stage]
 * */
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
        var days = (to.date - from.date).days.toDouble()
        if(days == 0.0) days = 1.0
        days
    }

    val dateShort
        get() =
            "$totalDays Day(s): ${from.dayOfMonth} ${from.month} to ${to.dayOfMonth} ${to.month}"

    fun getProgress(): Int {

        val currentMoment = Clock.System.now()
        val datetimeInUtc = currentMoment.toLocalDateTime(TimeZone.UTC)

        var remaining = (to.date - datetimeInUtc.date).days.toDouble()

        if (remaining < 0) remaining = 0.0
        else if (remaining > totalDays) remaining = totalDays

        Log.d("bar", "total: $totalDays, remaining: $remaining")
        return ((remaining / totalDays) * 100).toInt()
    }

    // TODO: Replace these with calling string resources
}
/* Simulation area
* Behaviour: as day come closer to the 'to' date, the bar reduces. otherwise, it starts with full
* so,
* [another yday -- yday]               today                [ tmrw -- another tmrw]
*   x - x                               x                                                   = 0
*   x               -                   x               -        x                          = .69
*                                       x                        x - x                      = 1
*
* */