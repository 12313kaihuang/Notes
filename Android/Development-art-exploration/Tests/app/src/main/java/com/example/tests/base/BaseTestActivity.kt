package com.example.tests.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tests.databinding.ActivityBaseBinding

/**
 * @auther hy
 * create on 2021/07/30 下午3:30
 */
abstract class BaseTestActivity : BaseActivity<ActivityBaseBinding>() {
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

    override fun crateBinding(): ActivityBaseBinding =
        ActivityBaseBinding.inflate(layoutInflater)


    override fun onStart() {
        super.onStart()
        mAdapter.submitList(btns)
    }

    protected open fun onInitBtns() {

    }

    protected fun addBtn(btnText: String, clickListener: (() -> Unit)?) {
        btns.add(BtnAdapter.BtnItem(btnText, clickListener))
    }

    protected fun <T : Activity> goToPage(clz: Class<T>) {
        startActivity(Intent(this, clz))
    }

    fun getName(): String = this::class.java.simpleName
}