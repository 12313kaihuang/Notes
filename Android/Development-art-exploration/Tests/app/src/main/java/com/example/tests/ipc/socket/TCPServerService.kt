package com.example.tests.ipc.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.tests.ipc.socket.SocketTestTestActivity.Companion.TAG
import java.io.*
import java.lang.Exception
import java.net.ServerSocket
import java.net.Socket
import kotlin.random.Random

class TCPServerService : Service() {

    private var mIsServiceDestroyed = false

    //随机问题
    private val mDefinedMessages = arrayOf(
        "你好啊，哈哈",
        "请问你叫什么名字呀？",
        "今天北京天气不错啊，sky",
        "你知道吗？我可是可以和多个人同时聊天等哦",
        "给你讲个笑话吧：据说爱笑的人运气不会太差，不知道真假"
    )

    override fun onCreate() {
        Thread(TCPServer()).start()
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        mIsServiceDestroyed = true
        super.onDestroy()
    }

    private inner class TCPServer : Runnable {
        private var serverSocket: ServerSocket? = null

        override fun run() {
            try {
                serverSocket = ServerSocket(8688)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e(TAG, "establich tcp server failed, port:8688 ", e)
                return
            }
            while (!mIsServiceDestroyed) {
                tryReceiveRequest()
            }
        }

        private fun tryReceiveRequest() {
            try {
                val client = serverSocket?.accept()
                Log.d(TAG, "tryReceiveRequest: accept")
                //开启自线程处理接收逻辑，使得后面的请求不被阻塞
                Thread {
                    responseClient(client)
                }.start()
            } catch (e: Exception) {
                Log.e(TAG, "tryReceiveRequest error ", e)
                e.printStackTrace()
            }
        }

        private fun responseClient(client: Socket?) {
            //用于接收客户端消息
            val reader = BufferedReader(InputStreamReader(client!!.getInputStream()))
            //用于向客户端发送消息
            val writer =
                PrintWriter(BufferedWriter(OutputStreamWriter(client.getOutputStream())), true)
            writer.println("欢迎来到聊天室！")

            while (!mIsServiceDestroyed) {
                val line = reader.readLine()
                Log.d(TAG, "responseClient received msg:$line")
                if (line == null) break
                val random = Random(0).nextInt(mDefinedMessages.size)
                writer.println(mDefinedMessages[random])
                Log.d(TAG, "responseClient send msg: ${mDefinedMessages[random]}")
            }
            Log.d(TAG, "responseClient client quit ")
            Utils.close(reader)
            Utils.close(writer)
            client.close()
        }

    }
}