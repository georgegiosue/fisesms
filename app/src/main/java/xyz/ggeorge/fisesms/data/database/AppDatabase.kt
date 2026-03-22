package xyz.ggeorge.fisesms.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import xyz.ggeorge.fisesms.data.dao.FiseDao
import xyz.ggeorge.fisesms.data.entities.FiseEntity
import xyz.ggeorge.fisesms.data.entities.ProcessingResultEntity

@Database(
    entities = [FiseEntity::class, ProcessingResultEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract val fiseDao: FiseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `processing_result` (
                        `id` INTEGER NOT NULL PRIMARY KEY,
                        `state` TEXT NOT NULL,
                        `smsBody` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
