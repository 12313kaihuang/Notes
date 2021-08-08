package com.example.tests.ipc

import com.yu.hu.libcommon.base.AbstractTestActivity
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
class IpcTestActivity : AbstractTestActivity() {

    override fun onInitBtns() {
        addBtn("Serialization test") { startActivity(SerializationTestTestActivity::class.java) }
        addBtn("Messenger test") { startActivity(MessengerTestTestActivity::class.java) }
        addBtn("AIDL Test") {
            startActivity(AIDLTestTestActivity::class.java)
        }
        addBtn("ContentProvider Test") {
            startActivity(ProviderTestTestActivity::class.java)
        }
        addBtn("Socket Test") { startActivity(SocketTestTestActivity::class.java) }
        addBtn("BinderPool Test") { startActivity(BinderPoolTestActivity::class.java) }
    }
}