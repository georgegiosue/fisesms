package xyz.ggeorge.fisesms.interactors.abstraction

import android.content.Context
import xyz.ggeorge.core.domain.SMS

interface ISMSManager {

    fun checkBefore(sms: SMS): Boolean
    suspend fun send(sms: SMS, context: Context)
}