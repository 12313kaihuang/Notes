package com.example.tests.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.yu.hu.libcommon.base.BaseTestActivity
import com.example.tests.activity.TestActivityTest.Companion.TAG

class ETestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("startSelf(显示启动)") {
            val intent = Intent(this, ETestActivity::class.java)
            startActivity(intent)
        }

        //会以隐式方式启动，这里即会调用onNewIntent方法
        addBtn("startSelf(显示&隐式启动)") {
            val intent = Intent(this, ETestActivity::class.java)
            intent.action = "com.example.activity.test.E"
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        //这里会以SingleTop方式启动 EActivity
        addBtn("startSelf(显示&隐式启动)") {
            val intent = Intent(this, ETestActivity::class.java)
            intent.action = "com.example.activity.test"
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Toast.makeText(this, "onNewIntent", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onNewIntent: ")
    }
}