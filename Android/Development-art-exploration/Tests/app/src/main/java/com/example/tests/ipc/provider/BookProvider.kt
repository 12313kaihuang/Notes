package com.example.tests.ipc.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.util.Log
import com.example.tests.ipc.provider.ProviderTestActivity.Companion.TAG
import java.lang.RuntimeException
import java.sql.SQLDataException

/**
 * @auther hy
 * create on 2021/08/05 下午9:01
 */
class BookProvider : ContentProvider() {

    init {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE)
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE)
    }

    private lateinit var mContext: Context
    private lateinit var mDb: SQLiteDatabase

    override fun onCreate(): Boolean {
        log("onCreate")
        mContext = context!!
        initProviderData()
        return true
    }

    private fun initProviderData() {
        mDb = DbOpenHelper(mContext).writableDatabase
        mDb.execSQL("delete from ${DbOpenHelper.BOOK_TABLE_NAME}")
        mDb.execSQL("delete from ${DbOpenHelper.USER_TABLE_NAME}")
        mDb.execSQL("insert into book values(3,'Android');")
        mDb.execSQL("insert into book values(4,'IOS');")
        mDb.execSQL("insert into book values(5,'HTML5');")
        mDb.execSQL("insert into user values(1,'Jack',1);")
        mDb.execSQL("insert into user values(2,'Bob',0);")
    }

    private fun getTableName(uri: Uri): String = when (sUriMatcher.match(uri)) {
        BOOK_URI_CODE -> DbOpenHelper.BOOK_TABLE_NAME
        USER_URI_CODE -> DbOpenHelper.USER_TABLE_NAME
        else -> throw RuntimeException()
    }

    override fun getType(uri: Uri): String? {
        log("getType")
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        log("insert")
        val table = getTableName(uri)
        mDb.insert(table, null, values)
        mContext.contentResolver.notifyChange(uri, null)
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        log("delete")
        val table = getTableName(uri)
        val count = mDb.delete(table, selection, selectionArgs)
        if (count > 0) {
            mContext.contentResolver.notifyChange(uri, null)
        }
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        log("update")
        val table = getTableName(uri)
        val row = mDb.update(table, values, selection, selectionArgs)
        if (row > 0) {
            mContext.contentResolver.notifyChange(uri, null)
        }
        return row
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        log("query")
        val table = getTableName(uri)
        return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null)
    }

    private fun log(msg: String) {
        Log.d(TAG, "${Thread.currentThread().name} - $msg")
    }

    companion object {

        const val AUTHORITY = "com.example.tests.ipc.provicer.BookProvider"

        val BOOK_CONTENT_URI = Uri.parse("content://$AUTHORITY/book")
        val USER_CONTENT_URI = Uri.parse("content://$AUTHORITY/user")

        const val BOOK_URI_CODE = 0
        const val USER_URI_CODE = 1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

}