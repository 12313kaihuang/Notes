package com.example.tests.activity

import com.example.tests.base.BaseTestActivity

class BTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("to C Activity") {
            goToPage(CTestActivity::class.java)
        }
    }
}