package com.example.tests.bitmap

import com.yu.hu.libcommon.base.AbstractTestFragmentActivity

class BitmapTestActivity : AbstractTestFragmentActivity() {
    override fun startDefaultFragment() {
        startFragment(BitmapTestFragment::class.java)
    }
}