package com.example.bindservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.view.View
import android.view.WindowInsetsAnimation.Bounds
import android.widget.Toast
import com.example.bindservice.Service.LocalService
import com.example.bindservice.Service.MessengerService

private const val  MSG_SAY_HELLO = 1

class MainActivity : AppCompatActivity() {
    private lateinit var lService:LocalService
    private var mBound: Boolean = false

    private var mService:Messenger?=null
    private var bound:Boolean = false

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LocalService.LocalBinder
            lService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }
    }

    private val mConnection = object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
            bound = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun bindService(view: View){
        Intent(this, LocalService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    fun unbindService(view: View){
        unbindService(connection)
    }

    fun getRandomNumber(view: View) {
        if (mBound) {
            val num = lService.randomNumber
            Toast.makeText(this, "number: $num", Toast.LENGTH_SHORT).show()
        }
    }

    fun bindMessengerService(view: View){
        Intent(this, MessengerService::class.java).also { intent ->
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun sayHello(view: View){
        if(!bound)return
        val  msg = Message.obtain(null,MSG_SAY_HELLO,0,0)
        try{
            mService?.send(msg)
        }catch (e:RemoteException){
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        mBound = false

        if (bound) {
            unbindService(mConnection)
            bound = false
        }
    }
}