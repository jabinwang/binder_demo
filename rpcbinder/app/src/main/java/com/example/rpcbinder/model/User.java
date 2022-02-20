package com.example.rpcbinder.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.rpcbinder.aidl.Book;

public class User implements Parcelable {


    public int userId;
    public String userName;
    public boolean male;

    public Book book;
    public User() {

    }
    private User(Parcel in){
        this.userId = in.readInt();
        this.userName = in.readString();
        this.male = in.readInt() == 1;
        this.book = in.readParcelable(Thread.currentThread().getContextClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeInt(male ? 1 : 0);
        dest.writeParcelable(book, 0);
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", male=" + male +
                ", book=" + book +
                '}';
    }
}
