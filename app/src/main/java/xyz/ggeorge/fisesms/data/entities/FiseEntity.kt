package xyz.ggeorge.fisesms.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date
import java.util.concurrent.TimeUnit

@Entity(tableName = "fise")
data class FiseEntity (
    @PrimaryKey val code: String,
    @ColumnInfo(name = "dni") val dni: String,
    @ColumnInfo(name = "processed_timestamp") val processedTimestamp: String  = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString(),
)