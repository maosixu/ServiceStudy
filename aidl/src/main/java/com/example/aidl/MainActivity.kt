package com.example.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.aidl.Service.RemoteService

class MainActivity : AppCompatActivity() {
    companion object{
        const val TAG = "MainActivity"
    }
    var iRemoteService: IRemoteService? = null

    val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            iRemoteService = IRemoteService.Stub.asInterface(service)
        }

        // Called when the connection with the service disconnects unexpectedly.
        override fun onServiceDisconnected(className: ComponentName) {
            Log.e(TAG, "Service has unexpectedly disconnected")
            iRemoteService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Intent(this,RemoteService::class.java).also {
            bindService(it,mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun getPid(view: View){
        Toast.makeText(this,"进程pid=${iRemoteService?.pid}",Toast.LENGTH_LONG).show()
    }
}