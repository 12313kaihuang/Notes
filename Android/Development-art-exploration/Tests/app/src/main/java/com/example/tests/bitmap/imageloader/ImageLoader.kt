@file:Suppress("unused")

package com.example.tests.bitmap.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.util.LruCache
import android.widget.ImageView
import com.example.tests.bitmap.BitmapTestFragment.Companion.TAG
import java.io.IOException
import java.lang.RuntimeException
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 没有找到DiskLruCache的源码，随意这个test只能搁置了，多看几遍逻辑被
 */
class ImageLoader private constructor(context: Context) {

    private val mContenxt: Context
    private var mIsDiskLruCacheCreated: Boolean = false

    private val mMemoryCache: LruCache<String, Bitmap>


    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val result = msg.obj as LoadResult
            val img = result.imageView
//            img.setImageBitmap(result.bitmap)
            val url = img.getTag(TAG_KEY_URI) as String
            if (url == result.url) {
                img.setImageBitmap(result.bitmap)
            } else {
                Log.w(TAG, "set img bitmap,but url has changed,ignored! ")
            }
        }
    }

    init {
        mContenxt = context.applicationContext
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, bitmap: Bitmap?): Int {
                return bitmap?.let {
                    it.rowBytes * it.height / 1024
                } ?: kotlin.run { super.sizeOf(key, bitmap) }
            }
        }
//        val diskCacheDir: File = getDiskCacheDir(mContenxt, "bitmap")
//        if (!diskCacheDir.exists()) {
//            diskCacheDir.mkdirs()
//        }
//        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
//            try {
//                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE)
//                mIsDiskLruCacheCreated = true
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
    }

    private fun addBitmap2Cache(key: String, bitmap: Bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap)
        }
    }

    private fun getBitmapFromMemCache(key: String): Bitmap? {
        return mMemoryCache.get(key)
    }

    fun bindBitmap(uri: String, imageView: ImageView, reqWidth: Int, reqHeight: Int) {
        imageView.setTag(TAG_KEY_URI, uri)
        val bitmap = loadBitmapFromMemCache(uri)
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
            return
        }
        THREAD_POOL_EXECUTOR.execute {
            val bitmap = loadBitmap(uri, reqWidth, reqHeight)
            if (bitmap != null) {
                val result = LoadResult(imageView, bitmap, uri)
                mHandler.obtainMessage(MESSAGE_POST_RESULT, result).sendToTarget()
            }
        }
    }

    private fun loadBitmap(uri: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        var bitmap = loadBitmapFromMemCache(uri)
        if (bitmap != null) {
            Log.d(TAG, "loadBitmapFromMemCache url:$uri")
            return bitmap
        }

        try {
            bitmap = loadBitmapFromDiskCache(uri, reqWidth, reqHeight)
            if (bitmap != null) {
                Log.d(TAG, "loadBitmapFromDisk url:$uri")
                return bitmap
            }
            bitmap = loadBitmapFromHttp(uri, reqWidth, reqHeight)
            Log.d(TAG, "loadBitmapFromHttp url:$uri ")
        } catch (e: IOException) {
            Log.e(TAG, "loadBitmap error ", e)
        }
        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Log.w(TAG, "encounter error:DiskLruCache is nor created.")
            bitmap = downloadBitmapFromUrl(uri)
        }
        return bitmap
    }


    private fun loadBitmapFromMemCache(url: String): Bitmap? {
        return getBitmapFromMemCache(hashKeyFromUrl(url))
    }

    @Throws(IOException::class)
    private fun loadBitmapFromHttp(uri: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw RuntimeException("can note visit network from UI Thread.")
        }
//        if (mDiskLruCache == null)return null
        val key = hashKeyFromUrl(uri)
//        val editor = mDiskLruCache.edit(key)
//        if (editor != null) {
//            val outputStream = editor.newOutputStream(DISK_CACHE_INDEX)
//            if (downloadUrlToStream(url, outputStream)) {
//                editor.commit()
//            } else {
//                editor.abort()
//            }
//        }
        return loadBitmapFromDiskCache(uri, reqWidth, reqHeight)
    }


    private fun loadBitmapFromDiskCache(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {
//        if (mDiskLruCache == null)return null
        var bitmap: Bitmap? = null
        val key = hashKeyFromUrl(url)
        //disk
        return bitmap
    }


    private fun downloadBitmapFromUrl(url: String): Bitmap? {
        TODO("Not yet implemented")
    }

    private fun hashKeyFromUrl(url: String): String {
        TODO("Not yet implemented")
    }

    data class LoadResult(val imageView: ImageView, val bitmap: Bitmap, val url: String)

    companion object {
        const val MESSAGE_POST_RESULT = 1
        val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        val CORE_POOL_SIZE = CPU_COUNT + 1
        val MAXUMUM_POOL_SIZE = CPU_COUNT * 2 + 1
        const val KEEP_ALIVE = 10L

        const val TAG_KEY_URI = 1  //todo
        const val DISK_CACHE_SIZE = 1024 * 1024 * 50//磁盘缓存最多50M
        const val IO_BUFFER_SIZE = 8 * 1024
        const val DISK_CACHE_INDEX = 0

        private val sThreadFactory = object : ThreadFactory {
            private val mCount = AtomicInteger(1)

            override fun newThread(r: Runnable?): Thread {
                return Thread(r, "ImageLoader#${mCount.getAndIncrement()}")
            }

        }

        val THREAD_POOL_EXECUTOR = ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXUMUM_POOL_SIZE,
            KEEP_ALIVE,
            TimeUnit.SECONDS,
            LinkedBlockingDeque(),
            sThreadFactory
        )

        fun build(context: Context): ImageLoader = ImageLoader(context)
    }
}