package one.njk.celestidesk.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.network.BreakState

@Entity
data class DatabasePendingRequest(
    @PrimaryKey
    @ColumnInfo(name = "_id") val id: String,
    val origin: String,
    val subject: String,
    val message: String,
    @ColumnInfo(name = "request_date") val requestDate: String,
    val status: BreakState,
    val time: String,
)

fun List<DatabasePendingRequest>.asDomainModel(): List<BreakRequest> {
    return map {
        BreakRequest(
            id = it.id,
            subject = it.subject,
            message = it.message,
            date = it.requestDate,
            status = it.status
        )
    }
}

