package com.example.tests.bitmap.imageloader

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.yu.hu.libcommon.util.BitmapUtil
import java.io.FileDescriptor

object ImageResizer {

    fun decodeSampledBitmapFromResource(
        res: Resources, redId: Int,
        reqWidth: Int, reqHeight: Int
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, redId, options)

        options.inSampleSize = BitmapUtil.calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, redId, options)
    }

    fun decodeSampledBitmapFromFileDescriptor(
        fd: FileDescriptor,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFileDescriptor(fd, null, options)

        options.inSampleSize = BitmapUtil.calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFileDescriptor(fd, null, options)
    }

}