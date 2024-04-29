package com.example.aidlclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.aidl.IRemoteService

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    var iRemoteService: IRemoteService? = null

    val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            iRemoteService = IRemoteService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "Service has unexpectedly disconnected")
            iRemoteService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun connectAidlService(view: View) {
        //使用显示Intent启动服务
        Intent().also { intent ->
            intent.setClassName("com.example.aidl", "com.example.aidl.Service.RemoteService")
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun getRemoteServiceID(view: View) {
        Toast.makeText(this, "进程pid=${iRemoteService?.pid}", Toast.LENGTH_LONG).show()
    }

    fun sendRectMessage(view: View) {
        val rect = Rect(1,2,3,4)
        iRemoteService?.sendRect(rect)
    }
}