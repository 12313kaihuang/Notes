package com.yu.hu.libcommon.util

import android.R.attr
import android.content.Context
import android.graphics.*
import android.util.Log


object BitmapUtil {

    private const val TAG = "BitmapUtil"

    fun decodeResource(
        context: Context, idRes: Int, width: Int, height: Int,
        corner: Int = 0
    ): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(context.resources, idRes, options)
        Log.d(TAG, "decodeResource origin width:${options.outWidth} height:${options.outHeight}")

        val inSampleSize = calculateInSampleSize(options, width, height)
        options.inSampleSize = inSampleSize
        options.inJustDecodeBounds = false
        var bitmap = BitmapFactory.decodeResource(context.resources, idRes, options)
        if (corner > 0) {
            val temp = bitmap
            bitmap = transform2Corner(temp, corner.toFloat())
            temp.recycle()
        }
        Log.d(
            TAG,
            "decodeResource result width:${bitmap.width} height:${bitmap.height} size:${bitmap.byteCount / (1024 * 1024)}M"
        )
        return bitmap
    }

    fun transform2Gray(bitmap: Bitmap): Bitmap {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(newBitmap)
        val paint = Paint()
        val colorFilter = ColorMatrix().also { it.setSaturation(0f) }
        paint.colorFilter = ColorMatrixColorFilter(colorFilter)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return newBitmap
    }

    fun transform2Corner(bitmap: Bitmap, corner: Float): Bitmap? {
        Log.d(TAG, "transform2Corner corner:$corner")
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.RGB_565)

        val canvas = Canvas(newBitmap)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        //xfermode貌似在 Android 9.0以上失效来需要用Shader来实现
        //但是默认的bitmap背景貌似是黑色的，这里先设置成来白色来做来一个假象
        //如果是自定义View通过这个方法应该就没问题，可以抽空看下glide源码
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        canvas.drawColor(Color.WHITE)
        canvas.drawRoundRect(rectF, corner, corner, paint)
        return newBitmap
    }

    fun calculateInSampleSize(
        options: BitmapFactory.Options,
        targetWidth: Int,
        targetHeight: Int
    ): Int {
        var inSampleSize = 1
        if (options.outHeight > targetHeight || options.outWidth > targetWidth) {
            val w = options.outWidth / 2
            val h = options.outHeight / 2
            while (w / inSampleSize >= targetWidth || h / inSampleSize >= targetHeight)
                inSampleSize = inSampleSize shl 1
        }
        return inSampleSize
    }

}