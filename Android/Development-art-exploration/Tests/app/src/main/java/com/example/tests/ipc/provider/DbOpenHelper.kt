package com.example.tests.ipc.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * @auther hy
 * create on 2021/08/06 下午2:44
 */
class DbOpenHelper(private val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_VERSION = 1

        private const val DB_NAME = "book_provider.db"
         const val BOOK_TABLE_NAME = "book"
         const val USER_TABLE_NAME = "user"

        private const val CREATE_BOOK_TABLE = "create table if not exists $BOOK_TABLE_NAME (_id integer primary key, name text)"
        private const val CREATE_USER_TABLE = "create table if not exists $USER_TABLE_NAME (_id integer primary key, name text,sex int)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            it.execSQL(CREATE_BOOK_TABLE)
            it.execSQL(CREATE_USER_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}