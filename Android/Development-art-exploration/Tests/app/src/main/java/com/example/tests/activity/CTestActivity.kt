package com.example.tests.activity

import com.yu.hu.libcommon.base.BaseTestActivity

class CTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("to Main") {
            goToPage(TestActivityTest::class.java)
        }
    }
}