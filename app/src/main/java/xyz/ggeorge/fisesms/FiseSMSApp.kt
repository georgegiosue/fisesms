package xyz.ggeorge.fisesms

/*import xyz.ggeorge.fisesms.BuildConfig*/
import android.app.Application
import timber.log.Timber
import xyz.ggeorge.fisesms.framework.logger.ReleaseTree

class FiseSMSApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}
