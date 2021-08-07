package com.example.tests.ipc.messenger

import android.os.Bundle
import android.widget.Button
import com.example.tests.base.BaseActivity

class MessengerTestActivity : BaseActivity() {

    private lateinit var client: MessengerClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        client = MessengerClient()
    }

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("test1") { client.bindService(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        client.unBindService(this)
    }
}