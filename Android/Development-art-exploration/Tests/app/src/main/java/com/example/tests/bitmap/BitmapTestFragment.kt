package com.example.tests.bitmap

import com.yu.hu.libcommon.base.fragment.AbstractTestFragment

class BitmapTestFragment : AbstractTestFragment() {

    override fun onInitBtns() {
        addBtn("Bitmap加载 Test") { startFragment(LoadBitmapFragment::class.java) }
    }
}