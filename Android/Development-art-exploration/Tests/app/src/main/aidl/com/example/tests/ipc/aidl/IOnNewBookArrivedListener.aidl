// IOnNewBookArrivedListener.aidl
package com.example.tests.ipc.aidl;

// Declare any non-default types here with import statements
import com.example.tests.ipc.aidl.IBook;

interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book book);
}