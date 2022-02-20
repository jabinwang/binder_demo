// INewBookArrivedListener.aidl
package com.example.rpcbinder.aidl;

// Declare any non-default types here with import statements
import com.example.rpcbinder.aidl.Book;
interface INewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void onNewBookArrived(in Book book);
}