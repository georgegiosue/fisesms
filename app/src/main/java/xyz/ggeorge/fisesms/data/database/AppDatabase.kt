package xyz.ggeorge.fisesms.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.ggeorge.fisesms.data.dao.FiseDao
import xyz.ggeorge.fisesms.data.entities.FiseEntity

@Database(
    entities = [FiseEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val fiseDao: FiseDao
}