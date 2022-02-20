package com.example.rpcbinder.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.rpcbinder.R;

import java.util.List;

public class BookManagerActivity extends Activity {
    private static final String TAG = "BookManagerActivity";

    private IBookManager mRemoteBookManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

   private final INewBookArrivedListener mListener = new INewBookArrivedListener.Stub() {
       @Override
       public void onNewBookArrived(Book book) throws RemoteException {
           Log.e(TAG, "onNewBookArrived: recv new book" + book.toString()  );
       }
   };

    private final ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManager = bookManager;
            try {
                List<Book> list = bookManager.getBookList();
                Log.e(TAG, "onServiceConnected: book list " + list.toString() );
                Book book = new Book(3, "java");
                bookManager.addBook(book);
                Log.e(TAG, "onServiceConnected: add book" + book);
                List<Book> list2 = bookManager.getBookList();
                Log.e(TAG, "onServiceConnected: book list 2 " + list2.toString());

                bookManager.registerListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive()) {
            try {
                mRemoteBookManager.unregisterListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConn);
        super.onDestroy();
    }
}
