package one.njk.celestidesk.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import one.njk.celestidesk.network.Decision
import one.njk.celestidesk.network.Stage

@Dao
interface RequestsDao {

    @Query("select * from DatabasePendingRequest where status = :status")
    fun getRequestsFlow(status: Stage): Flow<List<DatabasePendingRequest>>

    @Upsert
    fun savePendingRequests(requests: List<DatabasePendingRequest>)

    @Query("delete from DatabasePendingRequest")
    fun invalidateCache()

    // TODO: Nuke this once server is ready
    @Query("update DatabasePendingRequest SET status = :decision WHERE _id = :requestId")
    fun updateRequest(requestId: String, decision: Decision)
}
// TODO: Use orderby once date things is implemented
@Database(entities = [DatabasePendingRequest::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RequestDatabase: RoomDatabase() {
    abstract val requestsDao: RequestsDao
}
