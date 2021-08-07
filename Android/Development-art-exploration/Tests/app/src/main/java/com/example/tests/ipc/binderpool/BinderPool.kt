package com.example.tests.ipc.binderpool

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import java.util.concurrent.CountDownLatch

class BinderPool private constructor(context: Context) {

    /**
     * 注意：init内的代码块会与有初始值比如mBinderPoolConnection的属性
     * 按声明顺序加入到构造函数中，所以connectBinderPoolService方法声明在这里，
     * 调用bindService时connection会为null，但是放到后面则不会有这个问题
     */
    init {
//        connectBinderPoolService()
    }

    private val mContext: Context = context
    private lateinit var mConnectBindingPoolCountDownLatch: CountDownLatch
    private var mBinderPool: IBinderPool? = null

    @Synchronized
    private fun connectBinderPoolService() {
        mConnectBindingPoolCountDownLatch = CountDownLatch(1)
        val intent = Intent(mContext, BinderPoolService::class.java)
        mContext.bindService(intent, mBinderPoolConnection, Context.BIND_AUTO_CREATE)
        try {
            Log.d(TAG, "${Thread.currentThread().name} onServiceConnected: wait connection")
            mConnectBindingPoolCountDownLatch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun queryBinder(binderCode: Int): IBinder? {
        var res: IBinder? = null
        try {
            res = mBinderPool?.queryBinder(binderCode)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        return res
    }

    private val mBinderPoolConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBinderPool = IBinderPool.Stub.asInterface(service)
            try {
                mBinderPool?.asBinder()?.linkToDeath(mBinderPoolDeathRecipient, 0)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            mConnectBindingPoolCountDownLatch.countDown()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    private val mBinderPoolDeathRecipient: IBinder.DeathRecipient =
        IBinder.DeathRecipient {
            Log.d(TAG, "binder died ")
            unlinkToDeath()
            mBinderPool = null
            connectBinderPoolService()
        }

    private fun unlinkToDeath() {
        mBinderPool?.asBinder()?.unlinkToDeath(mBinderPoolDeathRecipient, 0)
    }

    class BinderPoolImpl : IBinderPool.Stub() {
        override fun queryBinder(binderCode: Int): IBinder? {
            Log.d(TAG, "queryBinder: $binderCode")
            return when (binderCode) {
                BINDER_SECURITY_CENTER -> SecurityCenterImpl()
                BINDER_COMPUTE -> ComputeImpl()
                else -> null
            }
        }
    }

    class SecurityCenterImpl : ISecurityCenter.Stub() {

        override fun encrypt(content: String?): String = Utils.encrypt(content, SECRET_CODE)

        override fun decrypt(password: String?): String = encrypt(password)

    }

    class ComputeImpl : ICompute.Stub() {
        override fun add(a: Int, b: Int): Int {
            return a + b
        }
    }

    companion object {
        const val TAG = "BinderPoolTest"
        const val BINDER_COMPUTE = 0
        const val BINDER_SECURITY_CENTER = 1

        private const val SECRET_CODE = '^'

        @SuppressLint("StaticFieldLeak")
        @Volatile
        var sInstance: BinderPool? = null

        fun getInstance(context: Context): BinderPool {
            if (sInstance == null) {
                synchronized(this) {
                    if (sInstance == null) {
                        sInstance = BinderPool(context)
                    }
                }
            }
            return sInstance!!
        }
    }

    init {
        connectBinderPoolService()
    }
}