package xyz.ggeorge.fisesms.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import xyz.ggeorge.fisesms.data.entities.FiseEntity
import xyz.ggeorge.fisesms.data.entities.ProcessingResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FiseDao {
    @Query("SELECT * FROM fise ORDER BY processed_timestamp ASC")
    fun getAllOrderedByProcessedTimestampAsc(): Flow<List<FiseEntity>>

    @Query("SELECT * FROM fise ORDER BY processed_timestamp DESC")
    fun getAllOrderedByProcessedTimestampDesc(): Flow<List<FiseEntity>>

    @Query("SELECT * FROM fise WHERE code LIKE :code")
    fun findByCode(code: String): FiseEntity

    @Query("SELECT * FROM fise WHERE dni LIKE :dni")
    fun findByDNI(dni: String): List<FiseEntity>

    @Upsert
    suspend fun upsert(fise: FiseEntity)

    @Insert
    suspend fun insertAll(vararg fises: FiseEntity)

    @Delete
    fun delete(fise: FiseEntity)

    @Upsert
    suspend fun upsertResult(result: ProcessingResultEntity)

    @Query("SELECT * FROM processing_result WHERE id = 1")
    fun observeLatestResult(): Flow<ProcessingResultEntity?>

    @Query("DELETE FROM processing_result WHERE id = 1")
    suspend fun clearLatestResult()
}
