package com.devgeek.cataloguemovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.OVERVIEW;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.POSTER_PATH;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.RELEASE_DATE;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.TITLE;
import static com.devgeek.cataloguemovie.database.DbFavourite.TABLE_NAME;

/**
 * Created by GunSky7 on 6/7/2018.
 */

public class DbFavouriteHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "dbfavourite";

    private static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_FAVOURITE = "create table " + TABLE_NAME +
            " (" + _ID  + " integer primary key autoincrement, " +
            TITLE + " text not null, " +
            OVERVIEW + " text not null, " +
            RELEASE_DATE + " text not null, " +
            POSTER_PATH + " text not null);";

    public DbFavouriteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVOURITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
