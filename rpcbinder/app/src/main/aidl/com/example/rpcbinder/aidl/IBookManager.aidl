// IBookManager.aidl
package com.example.rpcbinder.aidl;

// Declare any non-default types here with import statements
import com.example.rpcbinder.aidl.Book;
import com.example.rpcbinder.aidl.INewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(INewBookArrivedListener listener);
    void unregisterListener(INewBookArrivedListener listener);
}