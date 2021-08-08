package com.yu.hu.libcommon.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import com.yu.hu.libcommon.adapter.BtnAdapter
import com.yu.hu.libcommon.databinding.LayoutBaseBinding

/**
 * @auther hy
 * create on 2021/07/30 下午3:30
 */
abstract class AbstractTestActivity : BaseActivity<LayoutBaseBinding>() {
    protected lateinit var mAdapter: BtnAdapter
    private val btns: MutableList<BtnAdapter.BtnItem> = mutableListOf()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding.tvText.text = getName()
        mAdapter = BtnAdapter()
        mViewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mViewBinding.recyclerView.adapter = mAdapter
        onInitBtns()
    }

    override fun crateBinding(): LayoutBaseBinding = LayoutBaseBinding.inflate(layoutInflater)

    override fun onStart() {
        super.onStart()
        mAdapter.submitList(btns)
    }

    protected open fun onInitBtns() {

    }

    protected fun addBtn(btnText: String, clickListener: (() -> Unit)?) {
        btns.add(BtnAdapter.BtnItem(btnText, clickListener))
    }

}