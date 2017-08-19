package com.pockettravel.guestbook;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pockettravel.guestbook.db.GuestContract;
import com.pockettravel.guestbook.provider.GuestContentProviderContract;


public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASK_ID = 0;
    public static final String EXTRA_ID = "extra_id";

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.simple_list_item_1);

        textView = (TextView) findViewById(android.R.id.text1);

        getSupportLoaderManager().initLoader(TASK_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long guestID = getIntent().getLongExtra(EXTRA_ID, -1);
        Uri uri = GuestContentProviderContract.Items.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(guestID)).build();
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor
                    .getColumnIndexOrThrow(GuestContract.Guest.COLUMN_NAME));
            textView.setText(name);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // do nothing
    }
}
