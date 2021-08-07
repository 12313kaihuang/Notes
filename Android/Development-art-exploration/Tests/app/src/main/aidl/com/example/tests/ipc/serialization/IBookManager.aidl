// IBookManager.aidl
package com.example.tests.ipc.serialization;

// Declare any non-default types here with import statements
import com.example.tests.ipc.serialization.IBook;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}