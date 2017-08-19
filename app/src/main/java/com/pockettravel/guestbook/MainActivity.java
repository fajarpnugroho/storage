package com.pockettravel.guestbook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pockettravel.guestbook.db.GuestContract;
import com.pockettravel.guestbook.provider.GuestContentProviderContract;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>,GuestAdapter.ItemClickListener {

    private static final int TASK_LOADER_ID = 0;
    private static final String TAG = "MainActivity";

    private EditText nameInput;
    private Button btnSave;
    private RecyclerView recyclerView;

    private GuestAdapter guestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = (EditText) findViewById(R.id.guest_name_input);
        btnSave = (Button) findViewById(R.id.btn_save);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        guestAdapter = new GuestAdapter(this, this);
        recyclerView.setAdapter(guestAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                // using if view is dragable
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                removeData(String.valueOf(id));
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);

        btnSave.setOnClickListener(this);

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    private void removeData(String id) {
        Uri uri = GuestContentProviderContract.Items.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();
        getContentResolver().delete(uri, null, null);
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        String name = nameInput.getText().toString();
        insertNewData(name);
        nameInput.setText("");
    }

    private void insertNewData(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GuestContract.Guest.COLUMN_NAME, name);

        getContentResolver().insert(GuestContentProviderContract.Items.CONTENT_URI,
                contentValues);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, GuestContentProviderContract.Items.CONTENT_URI,
                GuestContentProviderContract.Items.PROJECTION_ALL, null, null,
                GuestContentProviderContract.Items.SORT_ORDER_DEFAULT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        guestAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        guestAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, id);
        startActivity(intent);
    }
}
