package com.example.tests.ipc.socket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import com.example.tests.databinding.AcitivitySocketTestBinding
import com.yu.hu.libcommon.base.BaseActivity
import java.io.*
import java.lang.Exception
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

/**
 * 记得申请权限
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
class SocketTestTestActivity : BaseActivity<AcitivitySocketTestBinding>() {

    private lateinit var mPrintWriter: PrintWriter
    private lateinit var mClientSocket: Socket
    private val mExecutors = Executors.newCachedThreadPool()

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_RECEIVE_NEW_MSG -> {
                    val text = mViewBinding.msgText.text.toString()
                    if (TextUtils.isEmpty(text)) {
                        mViewBinding.msgText.text = msg.obj as String
                    } else {
                        mViewBinding.msgText.text = "$text${msg.obj}"
                    }
                }
                MSG_SOCKET_CONNECTED -> {
                    mViewBinding.sendBtn.isEnabled = true
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, TCPServerService::class.java)
        startService(intent)
        Thread {
            connectTCPServer()
        }.start()

        mViewBinding.sendBtn.setOnClickListener {
            mExecutors.submit { sendMsg() }
        }
    }

    private fun connectTCPServer() {
        var socket: Socket? = null
        while (socket == null) {
            try {
                socket = Socket("localhost", 8688)
                mClientSocket = socket
                mPrintWriter = PrintWriter(
                    BufferedWriter(
                        OutputStreamWriter(socket.getOutputStream())
                    ), true
                )
                mHandler.sendEmptyMessage(MSG_SOCKET_CONNECTED)
                Log.d(TAG, "connectTCPServer: connect server success")

            } catch (e: Exception) {
                SystemClock.sleep(1000)
                Log.w(TAG, "connectTCPServer error, retry 1s later")
            }
        }

        try {
            //接收服务端消息
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            while (!this.isFinishing) {
                val line = reader.readLine()
                Log.d(TAG, "received msg from server:$line")
                line?.let {
                    val showMsg =
                        "server " + fortmatDataTime(System.currentTimeMillis()) + ":$line\n"
                    mHandler.obtainMessage(MSG_RECEIVE_NEW_MSG, showMsg)
                        .sendToTarget()
                }
            }
            Log.d(TAG, "connectTCPServer: quite...")
            Utils.close(mPrintWriter)
            Utils.close(reader)
            socket.close()
        } catch (e: Exception) {
            Log.d(TAG, "connectTCPServer: ", e)
        }

    }

    private fun sendMsg() {
        val msg = mViewBinding.inputText.text.toString()
        if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
            mPrintWriter.println(msg)
            mViewBinding.inputText.setText("")
            val time = fortmatDataTime(System.currentTimeMillis())
            val showMsg = "client $time:$msg\n"
            mViewBinding.msgText.text =
                mViewBinding.msgText.text.toString() + showMsg
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun fortmatDataTime(currentTimeMillis: Long): String {
        return SimpleDateFormat("HH:mm:ss").format(Date(currentTimeMillis))
    }

    override fun crateBinding(): AcitivitySocketTestBinding {
        return AcitivitySocketTestBinding.inflate(layoutInflater)
    }


    companion object {
        const val TAG = "SocketTest"

        const val MSG_RECEIVE_NEW_MSG = 1
        const val MSG_SOCKET_CONNECTED = 2
    }
}