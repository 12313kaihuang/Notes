package com.example.tests.bitmap.load

import com.example.tests.R
import com.yu.hu.libcommon.adapter.BtnAdapter
import com.yu.hu.libcommon.base.fragment.AbstractTestFragment

class LoadBitmapFragment : AbstractTestFragment() {
    private val mImgItem = BitmapAdapter.ImgItem(R.drawable.bus)

    override fun onInitBtns() {
        super.onInitBtns()
        addItem(mImgItem)

        /**
         * screen density:3.5
         * decodeResource origin width:5120 height:3413
         * decodeResource result width:2987 height:1991 size:22M
         * 2987 = 5120 / 3 * 3.5 / 2，2是缩放比例
         */
        addBtn("加载原色图片") {
            mImgItem.showType = 1
            mAdapter.notifyItemChanged(0)
        }

        addBtn("加载黑白图片") {
            mImgItem.showType = 2
            mAdapter.notifyItemChanged(0)
        }

        addBtn("加载圆角图片") {
            mImgItem.showType = 3
            mAdapter.notifyItemChanged(0)
        }
    }

    override fun createAdapter(): BtnAdapter = BitmapAdapter()
}