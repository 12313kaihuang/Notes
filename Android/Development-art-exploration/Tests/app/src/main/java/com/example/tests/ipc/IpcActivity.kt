package com.example.tests.ipc

import com.example.tests.base.BaseActivity
import com.example.tests.ipc.aidl.AIDLTestActivity
import com.example.tests.ipc.messenger.MessengerTestActivity
import com.example.tests.ipc.provider.ProviderTestActivity
import com.example.tests.ipc.serialization.SerializationTestActivity

/**
 * @auther hy
 * create on 2021/08/02 下午7:50
 */
class IpcActivity : BaseActivity() {

    override fun onInitBtns() {
        addBtn("Serialization test") { goToPage(SerializationTestActivity::class.java) }
        addBtn("Messenger test") { goToPage(MessengerTestActivity::class.java) }
        addBtn("AIDL Test") {
            goToPage(AIDLTestActivity::class.java)
        }
        addBtn("ContentProvider Test") {
            goToPage(ProviderTestActivity::class.java)
        }
    }
}