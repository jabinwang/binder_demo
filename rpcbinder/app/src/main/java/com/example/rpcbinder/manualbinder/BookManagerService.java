package com.example.rpcbinder.manualbinder;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;

import androidx.annotation.Nullable;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {


    private AtomicBoolean mServiceDestroyed = new AtomicBoolean(false);

    private final CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();

        mBookList.add(new Book(1, "android"));
        mBookList.add(new Book(2, "ios"));
    }

    private final Binder mBinder = new BookManagerImpl() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mServiceDestroyed.set(true);
        super.onDestroy();
    }
}
