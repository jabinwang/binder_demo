package com.example.rpcbinder.provider;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.rpcbinder.R;
import com.example.rpcbinder.aidl.Book;
import com.example.rpcbinder.model.User;
import com.example.rpcbinder.utils.Constants;


public class ProviderActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri bookUri = Uri.parse("content://com.example.rpcbinder.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 5);
        values.put("name", "go");
        getContentResolver().insert(bookUri, values);

        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null,  null);
        while (bookCursor.moveToNext()) {
            Book book = new Book();
            book.bookId = bookCursor.getInt(0);
            book.bookName = bookCursor.getString(1);
            Log.e(Constants.TAG, "query book: " + book.toString());
        }
        bookCursor.close();

        Uri userUri = Uri.parse("content://com.example.rpcbinder.provider/user");
        Cursor userCursor = getContentResolver().query(userUri, new String[]{"_id", "name", "sex"}, null, null, null);
        while (userCursor.moveToNext()) {
            User user = new User();
            user.userId = userCursor.getInt(0);
            user.userName = userCursor.getString(1);
            user.male = userCursor.getInt(2) ==1;
            Log.e(Constants.TAG, "query user: " + user.toString());
        }
        userCursor.close();

    }
}
