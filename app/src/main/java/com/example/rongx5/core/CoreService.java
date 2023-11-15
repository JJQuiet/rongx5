//package com.example.rongx5.core;
//
//import android.content.Intent
//import org.linphone.LinphoneApplication
//import org.linphone.LinphoneApplication.Companion.coreContext
//import org.linphone.LinphoneApplication.Companion.corePreferences
//import org.linphone.LinphoneApplication.Companion.ensureCoreExists
//import org.linphone.core.tools.Log
//import org.linphone.core.tools.service.CoreService
//
//class CoreService extends CoreService() {
//        override fun onCreate() {
//        super.onCreate()
//        Log.i("[Service] Created")
//        }
//
//        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.i("[Service] Ensuring Core exists")
//        if (corePreferences.keepServiceAlive) {
//        Log.i("[Service] Starting as foreground to keep app alive in background")
//        if (!ensureCoreExists(
//        applicationContext,
//        pushReceived = false,
//        service = this,
//        useAutoStartDescription = false
//        )
//        ) {
//        coreContext.notificationsManager.startForeground(this, false)
//        }
//        } else if (intent?.extras?.get("StartForeground") == true) {
//        Log.i("[Service] Starting as foreground due to device boot or app update")
//        if (!ensureCoreExists(
//        applicationContext,
//        pushReceived = false,
//        service = this,
//        useAutoStartDescription = true
//        )
//        ) {
//        coreContext.notificationsManager.startForeground(this, true)
//        }
//        coreContext.checkIfForegroundServiceNotificationCanBeRemovedAfterDelay(5000)
//        } else {
//        ensureCoreExists(
//        applicationContext,
//        pushReceived = false,
//        service = this,
//        useAutoStartDescription = false
//        )
//        }
//
//        coreContext.notificationsManager.serviceCreated(this)
//
//        return super.onStartCommand(intent, flags, startId)
//        }
//
//        override fun createServiceNotificationChannel() {
//        // Done elsewhere
//        }
//
//        override fun showForegroundServiceNotification() {
//        Log.i("[Service] Starting service as foreground")
//        coreContext.notificationsManager.startCallForeground(this)
//        }
//
//        override fun hideForegroundServiceNotification() {
//        Log.i("[Service] Stopping service as foreground")
//        coreContext.notificationsManager.stopCallForeground()
//        }
//
//        override fun onTaskRemoved(rootIntent: Intent?) {
//        if (LinphoneApplication.contextExists()) {
//        if (coreContext.core.callsNb > 0) {
//        Log.w(
//        "[Service] Task removed but there is at least one active call, do not stop the Core!"
//        )
//        } else if (!corePreferences.keepServiceAlive) {
//        if (coreContext.core.isInBackground) {
//        Log.i("[Service] Task removed, stopping Core")
//        coreContext.stop()
//        } else {
//        Log.w("[Service] Task removed but Core is not in background, skipping")
//        }
//        } else {
//        Log.i(
//        "[Service] Task removed but we were asked to keep the service alive, so doing nothing"
//        )
//        }
//        }
//
//        super.onTaskRemoved(rootIntent)
//        }
//
//        override fun onDestroy() {
//        if (LinphoneApplication.contextExists()) {
//        Log.i("[Service] Stopping")
//        coreContext.notificationsManager.serviceDestroyed()
//        }
//
//        super.onDestroy()
//        }
//        }
//
