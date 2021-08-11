package com.example.tests.bitmap

import com.example.tests.bitmap.load.LoadBitmapFragment
import com.yu.hu.libcommon.base.fragment.AbstractTestFragment

class BitmapTestFragment : AbstractTestFragment() {

    override fun onInitBtns() {
        addBtn("Bitmap加载 Test") { startFragment(LoadBitmapFragment::class.java) }
    }

    companion object {
        const val TAG = "BitmapTest"
    }
}