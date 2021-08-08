package com.yu.hu.libcommon.util

import android.content.Context
import android.util.Log

object ScreenUtil {

    private const val TAG = "ScreenUtil"

    fun dp2px(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        Log.d(TAG, "screen density:$density result:${(density * dp + 0.5f).toInt()}")
        return (density * dp + 0.5f).toInt()
    }
}