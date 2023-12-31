package one.njk.celestidesk.database

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import one.njk.celestidesk.network.Stage

@Dao
interface RequestsDao {

    @Query("select * from DatabasePendingRequest where status = :status")
    fun getRequestsFlow(status: Stage): Flow<List<DatabasePendingRequest>>

    @Query("select * from DatabasePendingRequest where status = :one OR status = :two")
    fun getPendingRequestsFlow(one: Stage = Stage.IN_REVIEW, two: Stage = Stage.IN_PROCESS): Flow<List<DatabasePendingRequest>>

    @Upsert
    fun savePendingRequests(requests: List<DatabasePendingRequest>)

    @Query("delete from DatabasePendingRequest")
    fun invalidateCache()
}

@Dao
interface TransactionDao {

    @Upsert
    fun updateTransactions(transactions: List<DatabaseTransaction>)

    @Query("select * from DatabaseTransaction")
    fun getTransactionsFlow(): Flow<List<DatabaseTransaction>>

    @Query("select * from FtsTransaction where FtsTransaction match :term")
    fun searchTransactions(term: String): Flow<List<DatabaseTransaction>>

    @Query("delete from DatabaseTransaction")
    fun invalidateCache()
}

@Database(entities = [DatabasePendingRequest::class, DatabaseTransaction::class, FtsTransaction::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RequestDatabase: RoomDatabase() {
    abstract val requestsDao: RequestsDao
    abstract val transactionDao: TransactionDao
}