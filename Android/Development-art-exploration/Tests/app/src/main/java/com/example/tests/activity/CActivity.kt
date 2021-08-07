package com.example.tests.activity

import com.example.tests.base.BaseActivity

class CActivity : BaseActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("to Main") {
            goToPage(ActivityTest::class.java)
        }
    }
}