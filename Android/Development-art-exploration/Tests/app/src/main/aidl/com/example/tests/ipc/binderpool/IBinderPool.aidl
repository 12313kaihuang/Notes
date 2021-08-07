// IBinderPool.aidl
package com.example.tests.ipc.binderpool;

// Declare any non-default types here with import statements
//import android.os.Ibinder;

interface IBinderPool {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    IBinder queryBinder(int binderCode);
}