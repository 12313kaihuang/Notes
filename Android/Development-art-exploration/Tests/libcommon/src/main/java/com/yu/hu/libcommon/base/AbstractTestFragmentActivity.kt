package com.yu.hu.libcommon.base

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.yu.hu.libcommon.databinding.AcitvityFragmentContainerBinding

abstract class AbstractTestFragmentActivity :
    BaseFragmentActivity<AcitvityFragmentContainerBinding>() {

    override fun crateBinding(): AcitvityFragmentContainerBinding =
        AcitvityFragmentContainerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startDefaultFragment()
    }

    abstract fun startDefaultFragment()

    override fun <T : Fragment> startFragment(clz: Class<T>) {
        val fragment = clz.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(mViewBinding.fragmentContainer.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /**
     * 实现Fragment的回退
     */
    override fun onBackPressed() {
        Log.d(getName(), "onBackPressed: ${supportFragmentManager.backStackEntryCount}")
        if (supportFragmentManager.backStackEntryCount > 1) {
//            supportFragmentManager.popBackStack()
            super.onBackPressed() //super里面会处理这个栈，但是因为又默认的fragment，所以当只剩一个 fragment是应该返回
        } else {
            finish()
        }
    }
}