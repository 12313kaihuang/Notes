package com.example.tests.ipc.serialization

import android.util.Log
import android.widget.Toast
import com.yu.hu.libcommon.base.BaseTestActivity
import java.io.*

class SerializationTestTestActivity : BaseTestActivity() {

    override fun onInitBtns() {
        super.onInitBtns()
        val serializableObject = SerializableObject(1, "test")

        addBtn("testSerializable1") { testSerializable1(serializableObject) }
        addBtn("testSerializable2") { testSerializable2(serializableObject) }
        addBtn("testSerializable3") { testSerializable3(serializableObject) }
    }


    private fun testSerializable1(`object`: SerializableObject) {
        writeSerializableObject(`object`)
        //修改serialVersionUID后，直接从cache中读取数据会报错
        //java.io.InvalidClassException: com.example.serializationtest.SerializableObject; local class incompatible: stream classdesc serialVersionUID = -3091935682059836537, local class serialVersionUID = -3091935682059836538
        val serializableObject = readSerializableObject()
        Toast.makeText(
            this, serializableObject?.toString() ?: "null",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun testSerializable2(`object`: SerializableObject) {
//        writeSerializableObject(object);
        //给SerializableObject 随便添加一个属性addedField,然后从之前的缓存中读取
        //不会报错，但是addedField不会被赋值，即默认值
        val serializableObject = readSerializableObject()
        Toast.makeText(
            this, serializableObject?.toString() ?: "null",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun testSerializable3(`object`: SerializableObject) {
//        object.addedField = 5;
//        writeSerializableObject(object);
        //将加的addedField赋值并序列化存入文件
        //然后将属性去掉，读取文件反序列化,也不会报错，但是显然会读不到这个属性的值
        val serializableObject = readSerializableObject()
        Toast.makeText(
            this, serializableObject?.toString() ?: "null",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun writeSerializableObject(`object`: SerializableObject) {
        var outputStream: ObjectOutputStream? = null
        try {
            val path = externalCacheDir!!.absolutePath + "/cache.txt"
            Log.d(TAG, "writeSerializableObject: path = $path")
            outputStream = ObjectOutputStream(FileOutputStream(path))
            outputStream.writeObject(`object`)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "writeSerializableObject: ", e)
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun readSerializableObject(): SerializableObject? {
        var stream: ObjectInputStream? = null
        try {
            val path = externalCacheDir!!.absolutePath + "/cache.txt"
            stream = ObjectInputStream(FileInputStream(path))
            return stream.readObject() as SerializableObject
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "readSerializableObject: ", e)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            Log.e(TAG, "readSerializableObject: ", e)
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }

    companion object {
        const val TAG = "ActivityTest"
    }
}