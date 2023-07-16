package one.njk.celestidesk.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestsDao {

    @Query("select * from databasependingrequest")
    fun getPendingRequests(): Flow<List<DatabasePendingRequest>>

    @Upsert
    fun savePendingRequests(requests: List<DatabasePendingRequest>)
}

@Database(entities = [DatabasePendingRequest::class], version = 1)
abstract class RequestDatabase: RoomDatabase() {
    abstract val requestsDao: RequestsDao
}
