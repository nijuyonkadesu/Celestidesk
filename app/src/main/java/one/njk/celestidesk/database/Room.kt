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
}

// TODO: Use FTS4 once transaction things is implemented
@Database(entities = [DatabasePendingRequest::class, DatabaseTransaction::class, FtsTransaction::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RequestDatabase: RoomDatabase() {
    abstract val requestsDao: RequestsDao
    abstract val transactionDao: TransactionDao
}