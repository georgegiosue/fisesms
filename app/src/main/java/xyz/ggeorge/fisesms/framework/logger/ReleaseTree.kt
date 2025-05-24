package xyz.ggeorge.fisesms.framework.logger

import android.util.Log
import timber.log.Timber

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.WARN || priority == Log.ERROR) {
            // Por ejemplo, envía a Crashlytics:
            // Crashlytics.log(priority, tag, message)
            // if (t != null) Crashlytics.recordException(t)
        }
        // Ignora DEBUG/INFO
    }
}
