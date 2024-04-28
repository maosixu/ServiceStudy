package com.example.bindservice.Service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import java.util.Random


class LocalService : Service() {

    private val binder = LocalBinder()

    private val mGenerator = Random()

    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    inner class LocalBinder:Binder(){
        /**
         * 返回LocalService实例
         */
        fun getService():LocalService = this@LocalService
    }

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(this,"service binding",Toast.LENGTH_LONG).show()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Toast.makeText(this,"service unbinding",Toast.LENGTH_LONG).show()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Toast.makeText(this,"service done",Toast.LENGTH_LONG).show()
        super.onDestroy()
    }
}