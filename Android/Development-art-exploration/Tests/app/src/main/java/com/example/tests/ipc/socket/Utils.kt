package com.example.tests.ipc.socket

import java.io.BufferedReader
import java.io.PrintWriter
import java.lang.Exception

object Utils {
    fun close(reader: BufferedReader?) {
        if (reader == null) return
        try {
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun close(writer: PrintWriter?) {
        if (writer == null) return
        try {
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}