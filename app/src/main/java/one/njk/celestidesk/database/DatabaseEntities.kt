package one.njk.celestidesk.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.domain.History
import one.njk.celestidesk.network.ActionResult
import one.njk.celestidesk.network.Stage

@Entity
data class DatabasePendingRequest(
    @PrimaryKey
    @ColumnInfo(name = "_id") val id: String,
    val origin: String,
    val subject: String,
    val message: String,
    val status: Stage,
    val time: LocalDateTime,
    val from: LocalDateTime,
    val to: LocalDateTime
)

fun List<DatabasePendingRequest>.asDomainModel(): List<BreakRequest> {
    return map {
        BreakRequest(
            name = it.origin,
            id = it.id,
            subject = it.subject,
            message = it.message,
            date = it.time,
            status = it.status,
            from = it.from,
            to = it.to
        )
    }
}

// --------------------- Transaction Model [STARTS] -------------------------- //

@Entity
data class DatabaseTransaction(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    val origin: String,
    val subject: String,
    val message: String,
    val from: LocalDateTime,
    val to: LocalDateTime,
    val wasIn: Stage,
    val nowIn: ActionResult,
    val responder: String,
)

fun List<DatabaseTransaction>.asHistoryDomainModel(): List<History> {
    return map {
        History(
            origin = it.origin,
            subject = it.subject,
            message = it.message,
            from = it.from,
            to = it.to,
            wasIn = it.wasIn,
            nowIn = it.nowIn,
            responder = it.responder
        )
    }
}

@Fts4(contentEntity = DatabaseTransaction::class, notIndexed = ["from", "to", "id"])
@Entity
data class FtsTransaction (
    @PrimaryKey
    @ColumnInfo(name = "rowid")
    val autoId: Int,
    val id: String,
    val origin: String,
    val subject: String,
    val message: String,
    val from: LocalDateTime,
    val to: LocalDateTime,
    val wasIn: Stage,
    val nowIn: ActionResult,
    val responder: String,
)
// --------------------- Transaction Model [ENDS] -------------------------- //

@ProvidedTypeConverter
class Converters {
    @TypeConverter
    fun dateToTimeStamp(date: LocalDateTime): Long {
        return date.toInstant(TimeZone.UTC).toEpochMilliseconds()
    }

    @TypeConverter
    fun timeStampToDate(value: Long): LocalDateTime {
        return Instant
            .fromEpochMilliseconds(value)
            .toLocalDateTime(TimeZone.UTC)
    }
}