package one.njk.celestidesk.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import one.njk.celestidesk.domain.BreakRequest
import one.njk.celestidesk.network.Stage

@Entity
data class DatabasePendingRequest(
    @PrimaryKey
    @ColumnInfo(name = "_id") val id: String,
    val origin: String,
    val subject: String,
    val message: String,
    @ColumnInfo(name = "request_date") val requestDate: String,
    val status: Stage,
    val time: LocalDateTime,
    val from: LocalDateTime,
    val to: LocalDateTime
)

fun List<DatabasePendingRequest>.asDomainModel(): List<BreakRequest> {
    return map {
        BreakRequest(
            id = it.id,
            subject = it.subject,
            message = it.message,
            date = it.time,
            status = it.status
        )
    }
}

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