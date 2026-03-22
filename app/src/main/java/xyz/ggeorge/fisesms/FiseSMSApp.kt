package xyz.ggeorge.fisesms

import android.app.Application
import androidx.room.Room
import timber.log.Timber
import xyz.ggeorge.fisesms.data.dao.FiseDao
import xyz.ggeorge.fisesms.data.database.AppDatabase
import xyz.ggeorge.fisesms.framework.logger.ReleaseTree
import xyz.ggeorge.fisesms.framework.notifications.FiseNotificationHelper

class FiseSMSApp : Application() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "fise_db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }

    val fiseDao: FiseDao by lazy { db.fiseDao }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

        FiseNotificationHelper.createChannel(this)
    }
}
