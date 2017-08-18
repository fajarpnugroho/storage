package com.pockettravel.guestbook;


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pockettravel.guestbook.db.GuestContract;

public class GuestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public GuestAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        String name = cursor.getString(cursor
                .getColumnIndexOrThrow(GuestContract.Guest.COLUMN_NAME));
        long id = cursor.getLong(cursor
                .getColumnIndexOrThrow(GuestContract.Guest._ID));

        TextHolder viewHolder = (TextHolder) holder;
        viewHolder.bind(name);

        viewHolder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public static class BaseHolder extends RecyclerView.ViewHolder {

        public BaseHolder(@LayoutRes int resId, ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        }
    }

    public static class TextHolder extends BaseHolder {

        private TextView textView;

        public TextHolder(ViewGroup parent) {
            super(android.R.layout.simple_list_item_1, parent);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void bind(String name) {
            textView.setText(name);
        }
    }


}
