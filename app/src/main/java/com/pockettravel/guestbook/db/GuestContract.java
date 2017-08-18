package com.pockettravel.guestbook.db;


import android.provider.BaseColumns;

public class GuestContract {

    private GuestContract() {}

    public static class Guest implements BaseColumns {
        public static final String TABLE_NAME = "guest";
        public static final String COLUMN_NAME = "name";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + _ID + " INTEGER PRIMARY KEY, "
                        + COLUMN_NAME + " TEXT NOT NULL)";

    }
}
