// IBookManager.aidl
package com.example.tests.ipc.aidl;

// Declare any non-default types here with import statements
import com.example.tests.ipc.aidl.IBook;
import com.example.tests.ipc.aidl.IOnNewBookArrivedListener;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<Book> getAllBooks();

    //非基本类型要标记方向
    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}