package com.example.tests.activity

import com.yu.hu.libcommon.base.AbstractTestActivity

class BTestActivity : AbstractTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("to C Activity") {
            startActivity(CTestActivity::class.java)
        }
    }
}