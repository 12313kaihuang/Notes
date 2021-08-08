package com.yu.hu.libcommon.base.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.hu.libcommon.adapter.BtnAdapter
import com.yu.hu.libcommon.base.AbstractTestFragmentActivity
import com.yu.hu.libcommon.databinding.LayoutBaseBinding
import java.lang.Exception

abstract class AbstractTestFragment : BaseFragment<LayoutBaseBinding>() {

    protected lateinit var mAdapter: BtnAdapter
    private val btns: MutableList<BtnAdapter.BtnItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = BtnAdapter()
        onInitBtns()
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LayoutBaseBinding =
        LayoutBaseBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tvText.text = getName()
        mViewBinding.recyclerView.adapter = mAdapter
        mViewBinding.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mAdapter.submitList(btns)
    }

    protected open fun onInitBtns() {

    }

    protected fun addBtn(btnText: String, clickListener: (() -> Unit)?) {
        btns.add(BtnAdapter.BtnItem(btnText, clickListener))
    }

    protected open fun getName(): String = this::class.java.simpleName

    fun <T : AbstractTestFragment> startFragment(clz: Class<T>) {
        try {
            (activity as AbstractTestFragmentActivity).startFragment(clz)
        } catch (e: Exception) {
            Log.e(getName(), "startFragment error: ", e)
        }
    }
}