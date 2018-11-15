package com.devgeek.cataloguemovie.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.devgeek.cataloguemovie.FavouriteHelper;
import com.devgeek.cataloguemovie.database.DbFavourite;

import static com.devgeek.cataloguemovie.database.DbFavourite.AUTHORITY;
import static com.devgeek.cataloguemovie.database.DbFavourite.CONTENT_URI;

/**
 * Created by GunSky7 on 6/8/2018.
 */

public class FavouriteProvider extends ContentProvider {
    private static final int FAVOURITE = 1;
    private static final int FAVOURITE_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, DbFavourite.TABLE_NAME, FAVOURITE);

        sUriMatcher.addURI(AUTHORITY, DbFavourite.TABLE_NAME + "/#", FAVOURITE_ID);
    }

    private FavouriteHelper favouriteHelper;

    @Override
    public boolean onCreate() {
        favouriteHelper = new FavouriteHelper(getContext());
        favouriteHelper.open();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case FAVOURITE:
                cursor = favouriteHelper.queryProvider();
                break;

            case FAVOURITE_ID:
                cursor = favouriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;

            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;

        switch (sUriMatcher.match(uri)) {
            case FAVOURITE:
                added = favouriteHelper.insertProvider(values);
                break;

            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;

        switch (sUriMatcher.match(uri)) {
            case FAVOURITE_ID:
                deleted = favouriteHelper.deleteProvider(uri.getLastPathSegment());
                break;

            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s, @Nullable String[] strings) {
        int updated;

        switch (sUriMatcher.match(uri)) {
            case FAVOURITE_ID:
                updated = favouriteHelper.updateProvider(uri.getLastPathSegment(), values);
                break;

            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return  updated;
    }
}
