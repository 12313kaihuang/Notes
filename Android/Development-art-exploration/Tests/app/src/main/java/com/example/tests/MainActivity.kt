package com.example.tests

import com.example.tests.activity.ActivityTest
import com.example.tests.base.BaseActivity
import com.example.tests.ipc.IpcActivity

class MainActivity : BaseActivity() {

    override fun onInitBtns() {
        addBtn("第1章 Activity") {
            goToPage(ActivityTest::class.java)
        }
        addBtn("第2章 IPC机制") {
            goToPage(IpcActivity::class.java)
        }
    }
}