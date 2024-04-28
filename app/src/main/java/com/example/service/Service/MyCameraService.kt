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
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.example.service.R
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MyCameraService : Service() {
    private fun startForeground() {
        // 检查权限
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (cameraPermission == PackageManager.PERMISSION_DENIED) {
            stopSelf()
            return
        }
        try {
            // 创建通知管理类
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as
                    NotificationManager
            // 若API级别大于等于26，则需创建通知渠道
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("CHANNEL_ID", "前台Service通知",
                    NotificationManager.IMPORTANCE_DEFAULT)
                manager.createNotificationChannel(channel)
            }
            //创建通知，通知渠道为CHANNEL_ID
            val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
                .build()
            // 启动前台服务，如果设备的API级别大于或等于30，前台服务的类型被设置为FOREGROUND_SERVICE_TYPE_CAMERA；
            // 否则，前台服务的类型被设置为0。
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
                e.printStackTrace()
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