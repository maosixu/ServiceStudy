package com.example.bindservice.Service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast

class MessengerService : Service() {
    private lateinit var mMessager: Messenger

    companion object {
        const val MSG_SAY_HELLO = 1
        const val MSG_FROM_SERVICE = 2
    }

    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_SAY_HELLO -> {
                    val info = msg.data.getString("client")
                    Toast.makeText(applicationContext, info, Toast.LENGTH_LONG)
                        .show()
                    val replyMsg = Message.obtain(null, MSG_FROM_SERVICE)
                    val bundle = Bundle()
                    bundle.putString("server","Hello from MessengerService")
                    replyMsg.data = bundle
                    msg.replyTo.send(replyMsg)
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(applicationContext, "binding", Toast.LENGTH_LONG).show()
        mMessager = Messenger(IncomingHandler(this))
        return mMessager.binder
    }
}