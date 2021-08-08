package com.example.tests.ipc.provider

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.contentValuesOf
import com.yu.hu.libcommon.base.BaseTestActivity
import com.example.tests.ipc.aidl.Book

/**
 * @auther hy
 * create on 2021/08/05 下午9:06
 */
class ProviderTestTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        //同样需要注意线程安全
        //调用方会阻塞，被调用方存在并发问题
        addBtn("query Book") {
            queryBook()
        }
        addBtn("query User") {
            queryUser()
        }
    }

    private fun queryBook() {
        val uri = Uri.parse("content://com.example.tests.ipc.provicer.BookProvider/book")
        val values = contentValuesOf("_id" to 6, "name" to "程序设计的艺术")
        val observer = MContentObserver(Handler(Looper.getMainLooper()))
        contentResolver.registerContentObserver(uri, true, observer)
        contentResolver.insert(uri, values)
        val cursor = contentResolver.query(uri, arrayOf("_id", "name"), null, null, null)
        cursor?.let {
            while (it.moveToNext()) {
                val book = Book(it.getInt(0), it.getString(1))
                Log.d(TAG, "queryBook: $book")
            }
        }
        cursor?.close()
        contentResolver.unregisterContentObserver(observer)
    }

    private fun queryUser() {
        val uri = Uri.parse("content://com.example.tests.ipc.provicer.BookProvider/user")
        val cursor = contentResolver.query(uri, arrayOf("_id", "name", "sex"), null, null, null)
        cursor?.let {
            while (it.moveToNext()) {
                val user = User(it.getInt(0), it.getString(1), it.getInt(2) == 0)
                Log.d(TAG, "queryBook: $user")
            }
        }
        cursor?.close()
    }

    companion object {
        const val TAG = "ContentProviderTest"
    }

    data class User(val id: Int, val name: String, val isMale: Boolean)

    class MContentObserver(handler: Handler) : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            Log.d(TAG, "${Thread.currentThread().name} onChange: $selfChange")
        }

        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)

            Log.d(TAG, "${Thread.currentThread().name} onChange: $selfChange ${uri.toString()}")
        }
    }
}