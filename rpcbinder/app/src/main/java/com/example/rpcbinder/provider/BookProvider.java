package com.example.rpcbinder.provider;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rpcbinder.utils.Constants;

public class BookProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.rpcbinder.provider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        Log.e(Constants.TAG, "onCreate: current thread " + Thread.currentThread().getName() );
        initProviderData();
        return false;
    }

    private void initProviderData(){
        db = new DbOpenHelper(getContext()).getWritableDatabase();
        db.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        db.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);

        db.execSQL("insert into book values(3, 'Android');");
        db.execSQL("insert into book values(4, 'ios')");
        db.execSQL("insert into user values(1, 'john', 1)");
        db.execSQL("insert into user values(2, 'jame', 0)");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(Constants.TAG, "query: thread " + Thread.currentThread().getName());

        String table = getTableName(uri);
        if (table != null) {
            return db.query(table, projection, selection, selectionArgs, null, null, sortOrder,null );
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e(Constants.TAG, "getType: ");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e(Constants.TAG, "insert: thread " + Thread.currentThread().getName() );
        String table = getTableName(uri);
        if (table != null) {
            db.insert(table, null, values);
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(Constants.TAG, "delete: thread " + Thread.currentThread().getName() );
        String table = getTableName(uri);
        int count = 0;
        if (table != null) {
             count =  db.delete(table, selection, selectionArgs );
            if (count > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(Constants.TAG, "update: thread " + Thread.currentThread().getName() );
        String table = getTableName(uri);
        int row = 0;
        if (table != null) {
             row =  db.update(table, values, selection, selectionArgs);
             if (row > 0) {
                 getContext().getContentResolver().notifyChange(uri, null);
             }
        }
        return row;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
