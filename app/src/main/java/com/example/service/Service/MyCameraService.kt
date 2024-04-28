package com.example.service.Service

import android.Manifest
import android.app.ForegroundServiceStartNotAllowedException
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.graphics.BitmapFactory
import android.os.Build
import android.os.HandlerThread
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.example.service.R
import java.lang.Exception


class MyCameraService : Service() {

    private fun startForeground() {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_DENIED) {
            stopSelf()
            return
        }
        try {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("CHANNEL_ID", "前台Service通知",
                    NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)
            }
            val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
                .build()
            ServiceCompat.startForeground(
                this, 100, notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
                } else {
                    0
                },
            )
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {

            }
        }
    }

    override fun onCreate() {
        startForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this,"service starting", Toast.LENGTH_LONG).show()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
       return null
    }

    override fun onDestroy() {
        Toast.makeText(this,"service done", Toast.LENGTH_LONG).show()
        super.onDestroy()
    }
}