package com.example.tests.ipc.binderpool

import android.os.RemoteException
import android.util.Log
import android.widget.Toast
import com.example.tests.base.BaseTestActivity
import java.lang.Exception

class BinderPoolTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        //注意需要在自线程中执行
        addBtn("security binder Test") { Thread { testSecurityBinder() }.start() }
        addBtn("compute binder Test") { Thread { testCompute() }.start() }
    }

    private fun testCompute() {
        val pool = BinderPool.getInstance(this)
        val binder = pool.queryBinder(BinderPool.BINDER_COMPUTE)
        val server: ICompute = ICompute.Stub.asInterface(binder)
        val s = "testCompute: 1 + 2 = ${server.add(1, 2)} "
        runOnUiThread {
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
        }
        Log.d(BinderPool.TAG, s)
    }

    private fun testSecurityBinder() {
        try {
            val pool = BinderPool.getInstance(this)
            val binder = pool.queryBinder(BinderPool.BINDER_SECURITY_CENTER)
            val server: ISecurityCenter = ISecurityCenter.Stub.asInterface(binder)
            val msg = "hello-word-安卓"
            Log.d(BinderPool.TAG, "content:$msg")
            try {
                val pwd = server.encrypt(msg)
                Log.d(BinderPool.TAG, "encrypt:$pwd")
                Log.d(BinderPool.TAG, "decrypt:${server.decrypt(pwd)}")
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}