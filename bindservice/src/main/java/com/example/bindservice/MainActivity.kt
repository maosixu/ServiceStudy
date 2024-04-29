package com.example.bindservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import android.view.View
import android.view.WindowInsetsAnimation.Bounds
import android.widget.Toast
import com.example.bindservice.Service.LocalService
import com.example.bindservice.Service.MessengerService

class MainActivity : AppCompatActivity() {
    private lateinit var lService:LocalService
    private var mBound: Boolean = false

    private var mService:Messenger?=null
    private var bound:Boolean = false

    private var clientMessenger: Messenger? = Messenger(ClientHandler())


    //扩展Binder类进行进程间通信
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

    //通过Messenger进行进程间通信
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

    inner class ClientHandler : Handler() {
        override fun handleMessage(msg: Message) {
            // 处理来自服务端的响应消息
            when (msg.what) {
                MessengerService.MSG_FROM_SERVICE -> {
                    val serviceReply = msg.data.getString("server")
                    Toast.makeText(applicationContext,serviceReply,Toast.LENGTH_LONG).show()
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStop() {
        super.onStop()
        if(mBound){
            unbindService(connection)
            mBound = false
        }
        if (bound) {
            unbindService(mConnection)
            bound = false
        }
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
            //获取Service中的randomNumber字段
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
            val bundle = Bundle()
            bundle.putString("client","客户端消息")
            //通过Messenger发送消息
            msg.replyTo = clientMessenger
            msg.data = bundle
            mService?.send(msg)
        }catch (e:RemoteException){
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        const val  MSG_SAY_HELLO = 1
    }
}