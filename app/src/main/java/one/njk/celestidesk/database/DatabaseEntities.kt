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
import one.njk.celestidesk.network.ActionResult
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

// TODO: Create a Class for Transactions
// TODO: Make it FTS4 compliant
// TODO: Perform Search in SearchFragment
/***
 * 1. Set Content Entity (Transaction class)
 * 2. Create Entity: TransactionFts (with what fields we shd consider for finding search results)
 * 3. ✅ @Fts4(contentEntity) ❌ Map Transaction -> TransactionFts https://gist.github.com/joaocruz04/4667d9ae9fa884cd6c70f93f66bb6fd4
 * 4. Include Fts entity to room: Return List<>
 * 5. Search!! @Query("SELECT * FROM note_fts WHERE note_fts MATCH :searchText")
 */

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

// --------------------- Transaction Model [STARTS] -------------------------- //

@Entity
data class DatabaseTransaction(
    @PrimaryKey
    @ColumnInfo(name = "_id") val id: String,
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