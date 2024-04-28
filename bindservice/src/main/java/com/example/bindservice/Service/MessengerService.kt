package com.example.bindservice.Service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast

private const val  MSG_SAY_HELLO = 1

class MessengerService : Service() {
    private lateinit var mMessager:Messenger

    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ):Handler(){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                MSG_SAY_HELLO -> Toast.makeText(applicationContext,"hello!",Toast.LENGTH_LONG).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(applicationContext,"binding",Toast.LENGTH_LONG).show()
        mMessager = Messenger(IncomingHandler(this))
        return mMessager.binder
    }
}