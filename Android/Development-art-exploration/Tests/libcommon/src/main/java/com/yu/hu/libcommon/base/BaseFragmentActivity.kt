package com.yu.hu.libcommon.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragmentActivity<V : ViewBinding> : BaseActivity<V>() {

    abstract fun <T : Fragment> startFragment(clz: Class<T>)
}