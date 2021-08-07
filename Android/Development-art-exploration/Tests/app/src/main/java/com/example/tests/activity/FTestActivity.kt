package com.example.tests.activity

import android.content.Intent
import com.example.tests.base.BaseTestActivity

class FTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()

        //还是会以strand方式启动
        addBtn("start main(only new task)") {
            val intent = Intent("com.example.activity.test")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        //会以singleTask方式启动
        addBtn("start main") {
            val intent = Intent("com.example.activity.test")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}