package com.example.tests.activity

import com.yu.hu.libcommon.base.AbstractTestActivity

class CTestActivity : AbstractTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("to Main") {
            startActivity(TestActivityTest::class.java)
        }
    }
}