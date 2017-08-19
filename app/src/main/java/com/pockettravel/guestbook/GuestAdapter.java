package com.pockettravel.guestbook;


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pockettravel.guestbook.db.GuestContract;

public class GuestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Cursor cursor;

    private ItemClickListener clickListener;

    public GuestAdapter(Context context,
                        ItemClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        return new TextHolder(parent, clickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,
                                 int position) {
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
        return cursor == null ? 0 : cursor.getCount();
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

    public static class TextHolder extends BaseHolder implements View.OnClickListener {

        private final ItemClickListener clickListener;
        private TextView textView;

        public TextHolder(ViewGroup parent,
                          ItemClickListener clickListener) {
            super(android.R.layout.simple_list_item_1, parent);
            this.clickListener = clickListener;
            textView = (TextView) itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(this);
        }

        public void bind(String name) {
            textView.setText(name);
        }

        @Override
        public void onClick(View v) {
            if (clickListener == null) return;
            clickListener.onItemClick((Long) itemView.getTag());
        }
    }

    public interface ItemClickListener {
        void onItemClick(long id);
    }


}
