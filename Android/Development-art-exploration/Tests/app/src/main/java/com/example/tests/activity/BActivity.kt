package com.example.tests.activity

import com.example.tests.base.BaseActivity

class BActivity : BaseActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        addBtn("to C Activity") {
            goToPage(CActivity::class.java)
        }
    }
}