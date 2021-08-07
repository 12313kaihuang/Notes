package com.example.tests

import com.example.tests.activity.TestActivityTest
import com.example.tests.base.BaseTestActivity
import com.example.tests.ipc.IpcTestActivity

class MainTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        addBtn("第1章 Activity") {
            goToPage(TestActivityTest::class.java)
        }
        addBtn("第2章 IPC机制") {
            goToPage(IpcTestActivity::class.java)
        }
    }
}