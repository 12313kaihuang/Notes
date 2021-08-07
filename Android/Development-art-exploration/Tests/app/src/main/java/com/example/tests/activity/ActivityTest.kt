package com.example.tests.activity

import android.content.Intent
import com.example.tests.base.BaseActivity

class ActivityTest : BaseActivity() {
    companion object {
        const val TAG = "ActivityTest"
    }

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("test1 step1") { test1() }
        addBtn("test1 step2") { test1() }
        addBtn("test2") { test2() }
        addBtn("test3") { test3() }
        addBtn("test4") { test4() }
    }

    /**
     * BActivity和CActivity的启动模式均为singleTask，且taskAffinity并不是默认的任务栈
     * 首先启动B然后在B中启动C，在C中启动本Activity，此时通过`adb shell dumpsys activity activities`
     * 命令查看Activity任务栈，可以发现此时本
     */
    private fun test1() {
        goToPage(BActivity::class.java)
    }

    /**
     * 隐式启动Activity时，若注册表中IntentFilter中没有加入category（
     * android.intent.category.DEFAULT），会报错
     * No Activity found to handle Intent { act=com.example.activity.test.D }
     */
    private fun test2() {
        val intent = Intent("com.example.activity.test.D")
        startActivity(intent)
    }

    //见EActivity
    private fun test3() {
        goToPage(EActivity::class.java)
    }

    //见FActivity
    private fun test4() {
        goToPage(FActivity::class.java)
    }
}