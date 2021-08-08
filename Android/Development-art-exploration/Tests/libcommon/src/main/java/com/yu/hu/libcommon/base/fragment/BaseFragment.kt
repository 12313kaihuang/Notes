package com.yu.hu.libcommon.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V : ViewBinding> : Fragment() {

    protected lateinit var mViewBinding: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = createViewBinding(inflater,container)
        return mViewBinding.root
    }

    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): V
}