package com.pockettravel.guestbook.provider;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.pockettravel.guestbook.db.GuestContract;

public class GuestContentProviderContract {

    public static final String AUTHORITY = "com.pockettravel.guestbook.items";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static interface CommonColumns extends BaseColumns {
    }

    public static final class Items implements CommonColumns {
        /**
         * The content URI for this table.
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(GuestContentProviderContract.CONTENT_URI, "items");

        /**
         * The mime type of a directory of items.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.de.com.pockettravel.guestbook.guest_items";

        /**
         * The mime type of a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.de.com.pockettravel.guestbook.guest_items";

        /**
         * A projection of all columns
         * in the items table.
         */
        public static final String[] PROJECTION_ALL =
                {_ID, GuestContract.Guest.COLUMN_NAME};

        /**
         * The default sort order for
         * queries containing NAME fields.
         */
        public static final String SORT_ORDER_DEFAULT =
                _ID + " ASC";
    }
}
