package com.pockettravel.guestbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pockettravel.guestbook.db.DbHelper;
import com.pockettravel.guestbook.db.GuestContract;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameInput;
    private Button btnSave;
    private RecyclerView recyclerView;

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;

    private GuestAdapter guestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openHelper = new DbHelper(this);
        db = openHelper.getWritableDatabase();

        nameInput = (EditText) findViewById(R.id.guest_name_input);
        btnSave = (Button) findViewById(R.id.btn_save);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        guestAdapter = new GuestAdapter(this, getAllData());
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

                if (removeData(id)) {
                    guestAdapter.swapCursor(getAllData());
                }
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = nameInput.getText().toString();

        if (insertNewData(name)) {
            nameInput.setText("");
            guestAdapter.swapCursor(getAllData());
        }
    }

    private boolean insertNewData(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GuestContract.Guest.COLUMN_NAME, name);

        return db.insert(GuestContract.Guest.TABLE_NAME, null, contentValues) > 0;
    }

    private boolean removeData(long id) {
        return db.delete(GuestContract.Guest.TABLE_NAME,
                GuestContract.Guest._ID + " = " + id, null) > 0;
    }

    private Cursor getAllData() {
        String[] projections = new String[] {
                GuestContract.Guest._ID,
                GuestContract.Guest.COLUMN_NAME
        };
        return db.query(GuestContract.Guest.TABLE_NAME, projections,
                null, null, null, null, null);
    }

}
