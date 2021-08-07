package com.example.tests.ipc

import com.example.tests.base.BaseTestActivity
import com.example.tests.ipc.aidl.AIDLTestTestActivity
import com.example.tests.ipc.binderpool.BinderPoolTestActivity
import com.example.tests.ipc.messenger.MessengerTestTestActivity
import com.example.tests.ipc.provider.ProviderTestTestActivity
import com.example.tests.ipc.serialization.SerializationTestTestActivity
import com.example.tests.ipc.socket.SocketTestTestActivity

/**
 * @auther hy
 * create on 2021/08/02 下午7:50
 */
class IpcTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        addBtn("Serialization test") { goToPage(SerializationTestTestActivity::class.java) }
        addBtn("Messenger test") { goToPage(MessengerTestTestActivity::class.java) }
        addBtn("AIDL Test") {
            goToPage(AIDLTestTestActivity::class.java)
        }
        addBtn("ContentProvider Test") {
            goToPage(ProviderTestTestActivity::class.java)
        }
        addBtn("Socket Test") { goToPage(SocketTestTestActivity::class.java) }
        addBtn("BinderPool Test") { goToPage(BinderPoolTestActivity::class.java) }
    }
}