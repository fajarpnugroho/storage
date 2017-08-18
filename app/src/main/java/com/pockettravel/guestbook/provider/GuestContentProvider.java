package com.pockettravel.guestbook.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pockettravel.guestbook.db.DbHelper;
import com.pockettravel.guestbook.db.GuestContract;


public class GuestContentProvider extends ContentProvider {

    // helper constants for use with the UriMatcher
    private static final int ITEM_LIST = 1;
    private static final int ITEM_ID = 2;
    private static final UriMatcher URI_MATCHER;

    private DbHelper dbHelper = null;

    // prepare the UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(GuestContentProviderContract.AUTHORITY, "items", ITEM_LIST);
        URI_MATCHER.addURI(GuestContentProviderContract.AUTHORITY, "items/#", ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ITEM_LIST:
                return GuestContentProviderContract.Items.CONTENT_TYPE;
            case ITEM_ID:
                return GuestContentProviderContract.Items.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (URI_MATCHER.match(uri)) {
            case ITEM_LIST:
                builder.setTables(GuestContract.Guest.TABLE_NAME);
                break;
        }

        Cursor cursor = builder.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (URI_MATCHER.match(uri) != ITEM_LIST) {
            throw new IllegalArgumentException(
                    "Unsupported URI for insertion: " + uri);
        }

        Uri itemUri;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (URI_MATCHER.match(uri) == ITEM_LIST) {
            long id = db.insert(GuestContract.Guest.TABLE_NAME, null, values);
            if ( id > 0 ) {
                itemUri = ContentUris.withAppendedId(uri, id);
            } else {
                throw new android.database.SQLException("Failed to insert row into " + uri);
            }
        } else {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return itemUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case ITEM_ID:
                String idStr = uri.getLastPathSegment();
                String where = GuestContract.Guest._ID + " = " + idStr;
                delCount = db.delete(GuestContract.Guest.TABLE_NAME, where, selectionArgs);
                break;
        }
        // notify all listeners of changes:
        if (delCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
