package com.example.tests.ipc.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.os.RemoteException
import android.util.Log
import com.example.tests.base.BaseTestActivity
import com.example.tests.ipc.aidl.AIDLConstants.TAG

/**
 * @auther hy
 * create on 2021/08/02 下午7:54
 */
class AIDLTestTestActivity : BaseTestActivity() {

    private var mBookService: IBookManager? = null

    override fun onInitBtns() {
        addBtn("test1") {
            mBookService?.addBook(Book(5, "《Android开发艺术探索》"))
            val allBooks = mBookService?.allBooks
            //aidl转换只能转换为ArrayList
            Log.d(TAG, "test1 list ${allBooks?.javaClass?.canonicalName} ")
            Log.d(TAG, "test1 allBooks: ${allBooks.toString()}")
        }

        addBtn("test2") {
            //注册监听器 但是Listener传到另一个进程就对应另一个副本了、
            //解注册时会解注册失败，需要使用RemoteCallbackList来解决
            mBookService?.registerListener(listener)
            mBookService?.addBook(Book(5, "《Android开发艺术探索》"))
        }

        addBtn("test3") {
            //调用aidl接口时，本线程会阻塞住，所以需要开启子线程去执行
            Thread {
                val allBooks = mBookService?.allBooks
                logd("test3 allBooks: ${allBooks.toString()}")
            }.start()
        }

        //service 可以通过checkCallingOrSelfPermission来检查权限实现权限控制
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindService(Intent(this, BookManagerService::class.java), conn, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBookService?.let {
            if (it.asBinder().isBinderAlive) {
                try {
                    logd("unregister listener $listener")
                    it.unregisterListener(listener)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
        unbindService(conn)
    }

    private val conn = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            IBookManager.Stub.asInterface(service).also {
                mBookService = it
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    private val listener = object : IOnNewBookArrivedListener.Stub() {
        override fun onNewBookArrived(book: Book?) {
            logd("onNewBookArrived $book")
        }
    }

    private fun logd(msg: String = "") {
        Log.d(TAG, "${Process.myPid()} ${Thread.currentThread().name} $msg")
    }
}