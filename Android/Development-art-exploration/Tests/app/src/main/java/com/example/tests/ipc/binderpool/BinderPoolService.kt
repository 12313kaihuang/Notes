package com.example.tests.ipc.binderpool

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.tests.ipc.binderpool.BinderPool.Companion.TAG

class BinderPoolService : Service() {

    private val mBinderPool = BinderPool.BinderPoolImpl()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind: ")
        return mBinderPool
    }

}
