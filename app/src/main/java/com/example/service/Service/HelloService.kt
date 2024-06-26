package com.example.service.Service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.widget.Toast

class HelloService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler : ServiceHandler?=null

    private inner class ServiceHandler(looper: Looper):Handler(looper){
        override fun handleMessage(msg: Message) {
            try {
                Thread.sleep(5000)
            }catch (e:InterruptedException){
                Thread.currentThread().interrupt()
            }
            stopSelf(msg.arg1)
        }
    }

    override fun onCreate() {
        //创建一个名为ServiceStartArguments的后台优先级线程
        HandlerThread("ServiceStartArguments",Process.THREAD_PRIORITY_BACKGROUND).apply {
            start() //启动线程
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    //通过startService启动时会被调用
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this,"service starting", Toast.LENGTH_LONG).show()
        serviceHandler?.obtainMessage()?.also { msg->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Toast.makeText(this,"service done",Toast.LENGTH_LONG).show()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}