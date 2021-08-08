package com.example.tests

import com.example.tests.activity.TestActivityTest
import com.example.tests.bitmap.BitmapTestActivity
import com.example.tests.bitmap.BitmapTestFragment
import com.example.tests.ipc.IpcTestActivity
import com.yu.hu.libcommon.base.AbstractTestActivity

class MainTestActivity : AbstractTestActivity() {

    override fun onInitBtns() {
        addBtn("第1章 Activity") {
            startActivity(TestActivityTest::class.java)
        }
        addBtn("第2章 IPC机制") {
            startActivity(IpcTestActivity::class.java)
        }
        addBtn("第12章 Bitmap的加载和Cache") {
            startActivity(BitmapTestActivity::class.java)
        }
    }
}