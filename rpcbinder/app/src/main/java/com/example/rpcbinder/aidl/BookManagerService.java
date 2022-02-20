package com.example.rpcbinder.aidl;

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

    private final CopyOnWriteArrayList<INewBookArrivedListener> mListenerList = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<INewBookArrivedListener> mListenerList1= new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        mBookList.add(new Book(1, "android"));
        mBookList.add(new Book(2, "ios"));
        new Thread(new ServiceWorker()).start();
    }

    private final Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(5000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(INewBookArrivedListener listener) throws RemoteException {
           mListenerList1.register(listener);
            if (!mListenerList.contains(listener)) {
                mListenerList.add(listener);
            }
        }

        @Override
        public void unregisterListener(INewBookArrivedListener listener) throws RemoteException {
           mListenerList1.unregister(listener);
            if (mListenerList.contains(listener)) {
                mListenerList.remove(listener);
            }
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
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

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        int n = mListenerList1.beginBroadcast();
        for (int i = 0; i < n; i++) {
            INewBookArrivedListener l = mListenerList1.getBroadcastItem(i);
            if (l != null) {
                l.onNewBookArrived(book);
            }
        }
        mListenerList1.finishBroadcast();
    }
    private class ServiceWorker implements Runnable{
        @Override
        public void run() {
            while (!mServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size() + 1;
                Book book = new Book(bookId, "new book#" + bookId);
                try {
                    onNewBookArrived(book);
                }catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
