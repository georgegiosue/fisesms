package xyz.ggeorge.fisesms.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import xyz.ggeorge.core.domain.Fise
import java.time.LocalDateTime
import java.util.Date
import java.util.concurrent.TimeUnit

@Entity(tableName = "fise")
data class FiseEntity (
    @PrimaryKey val code: String,
    @ColumnInfo(name = "dni") val dni: String,
    @ColumnInfo(name = "amount") val amount: Double?,
    @ColumnInfo(name = "processed_timestamp") val processedTimestamp: String  = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString(),
) {
    fun toDomain() = Fise.Processed(
        code = code,
        dni = dni,
        amount = amount,
        processedTimestamp = processedTimestamp
    )
}