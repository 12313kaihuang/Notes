package com.example.tests.ipc.aidl

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import com.example.tests.ipc.aidl.AIDLConstants.TAG
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @auther hy
 * create on 2021/08/02 下午8:09
 */
class BookManagerService : Service() {

    /**
     * aidl的方法执行在Binder线程池中，所以需要考虑并发的情况
     * CopyOnWriteArrayList是一个线程安全的list，支持并发读写
     */
    private val mBookList: MutableList<Book> = CopyOnWriteArrayList()

    //使用RemoteCallbackList 解决注册/解注册时Listener不一致问题
    private val mListener: RemoteCallbackList<IOnNewBookArrivedListener> = RemoteCallbackList()

    override fun onCreate() {
        super.onCreate()
        mBookList.add(Book(1, "高等数学"))
        mBookList.add(Book(2, "语文"))
    }

    @SuppressLint("UseCheckPermission")
    override fun onBind(intent: Intent?): IBinder {
        //检查权限
        checkCallingOrSelfPermission("")
        return mBinder
    }

    private val mBinder: Binder = object : IBookManager.Stub() {
        override fun getAllBooks(): MutableList<Book> {
            Log.d(TAG, "${Process.myPid()} ${Thread.currentThread().name} getAllBooks ")
            return mBookList
        }

        override fun addBook(book: Book?) {
            Log.d(TAG, "${Process.myPid()} ${Thread.currentThread().name} addBook $book ")
            book?.let {
                mBookList.add(it)
//                mListener.forEach { listener ->
//                    logd("notify new book arrived $listener")
//                    listener.onNewBookArrived(book)
//                }
                val size: Int = mListener.beginBroadcast()
                for (i in 0 until size) {
                    try {
                        logd("notify new book arrived ")
                        mListener.getBroadcastItem(i)?.onNewBookArrived(book)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
                mListener.finishBroadcast()
            }
        }

        override fun registerListener(listener: IOnNewBookArrivedListener?) {
//            if (!mListener.contains(listener)) {
//                mListener.add(listener!!)
//            } else {
//                logd("registerListener: already exists")
//            }
//            logd("registerListener: size: ${mListener.size}")
            mListener.register(listener)
            val size = mListener.beginBroadcast()
            logd("registerListener: size: $size")
            mListener.finishBroadcast()
        }

        override fun unregisterListener(listener: IOnNewBookArrivedListener?) {
//            if (mListener.contains(listener)) {
//                mListener.remove(listener!!)
//                logd("unregisterListener: succeed")
//            } else {
//                logd("unregisterListener: unregisterListener: not find")
//            }
//            logd("unregisterListener: size: ${mListener.size}")
            mListener.unregister(listener)
            val size = mListener.beginBroadcast()
            logd("unregisterListener: size: $size")
            mListener.finishBroadcast()
        }

    }

    fun logd(msg: String = "") {
        Log.d(TAG, "${Process.myPid()} ${Thread.currentThread().name} $msg")
    }
}