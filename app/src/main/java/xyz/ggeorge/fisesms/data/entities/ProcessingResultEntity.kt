package xyz.ggeorge.fisesms.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "processing_result")
data class ProcessingResultEntity(
    @PrimaryKey val id: Long = 1,
    val state: String,
    val smsBody: String,
    val timestamp: Long = System.currentTimeMillis()
)
