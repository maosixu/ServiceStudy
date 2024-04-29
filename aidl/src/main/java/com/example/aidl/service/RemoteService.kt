package com.example.aidl.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Process
import android.widget.Toast
import com.example.aidl.IRemoteService

class RemoteService : Service() {

    lateinit var context:Context

    override fun onCreate() {
        context = this
        super.onCreate()
    }

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(context,"service binding",Toast.LENGTH_LONG).show()
        return binder
    }

    private val binder = object : IRemoteService.Stub() {
        override fun getPid(): Int {
            return Process.myPid()
        }

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String
        ) {
            // Does nothing.
        }

        override fun sendRect(rect: Rect?) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, rect.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}