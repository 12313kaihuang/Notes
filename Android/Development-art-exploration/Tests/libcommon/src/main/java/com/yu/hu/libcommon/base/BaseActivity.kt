package com.yu.hu.libcommon.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<V : ViewBinding> : AppCompatActivity() {

    protected lateinit var mViewBinding: V

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = crateBinding()
        setContentView(mViewBinding.root)
    }

    abstract fun crateBinding(): V

    protected fun <T : Activity> startActivity(clz: Class<T>) {
        startActivity(Intent(this, clz))
    }

    protected open fun getName(): String = this::class.java.simpleName

}