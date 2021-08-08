package com.example.tests.activity

import com.yu.hu.libcommon.base.BaseTestActivity

class BTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("to C Activity") {
            goToPage(CTestActivity::class.java)
        }
    }
}