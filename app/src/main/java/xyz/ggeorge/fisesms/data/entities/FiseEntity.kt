package xyz.ggeorge.fisesms.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fise")
data class FiseEntity (
    @PrimaryKey val code: String,
    @ColumnInfo(name = "dni") val dni: String,
    @ColumnInfo(name = "processed_timestamp", defaultValue = "CURRENT_TIMESTAMP") val processedTimestamp: String,
)