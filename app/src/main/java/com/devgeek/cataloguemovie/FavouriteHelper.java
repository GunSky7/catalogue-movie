package com.devgeek.cataloguemovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.devgeek.cataloguemovie.database.DbFavourite;
import com.devgeek.cataloguemovie.database.DbFavouriteHelper;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.OVERVIEW;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.POSTER_PATH;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.RELEASE_DATE;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.TITLE;
import static com.devgeek.cataloguemovie.database.DbFavourite.TABLE_NAME;

/**
 * Created by GunSky7 on 6/7/2018.
 */

public class FavouriteHelper {
    private Context context;
    private DbFavouriteHelper dbFavouriteHelper;

    private SQLiteDatabase database;

    public FavouriteHelper(Context context) {
        this.context = context;
    }

    public FavouriteHelper open() throws SQLException {
        dbFavouriteHelper = new DbFavouriteHelper(context);

        database = dbFavouriteHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        dbFavouriteHelper.close();
    }

    public MovieItem getDataByTitle(String title) {
        Cursor cursor = database.query(TABLE_NAME, null, TITLE + " LIKE ?", new String[]{title}, null, null, _ID + " ASC", null);
        cursor.moveToFirst();

        MovieItem movieItem = new MovieItem();

        if (cursor.getCount() > 0) {
            movieItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
            movieItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
            movieItem.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
            movieItem.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
            movieItem.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
        }

        cursor.close();

        return movieItem;
    }

    public ArrayList<MovieItem> getAllData() {
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();

        ArrayList<MovieItem> arrayList = new ArrayList<>();

        MovieItem movieItem;

        if (cursor.getCount() > 0) {
            do {
                movieItem = new MovieItem();
                movieItem.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movieItem.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movieItem.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movieItem.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movieItem.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));

                arrayList.add(movieItem);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        cursor.close();

        return arrayList;
    }

    public long insert(MovieItem movieItem) {
        ContentValues initialValues = new ContentValues();

        initialValues.put(TITLE, movieItem.getTitle());
        initialValues.put(OVERVIEW, movieItem.getOverview());
        initialValues.put(RELEASE_DATE, movieItem.getReleaseDate());
        initialValues.put(POSTER_PATH, movieItem.getPosterPath());

        return database.insert(TABLE_NAME, null, initialValues);
    }

    public void beginTransaction() {database.beginTransaction();}

    public void setTransactionSuccess() {database.setTransactionSuccessful();}

    public void endTransaction() {database.endTransaction();}

    public void insertTransaction(MovieItem movieItem) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + TITLE + ", " + OVERVIEW + ", " + RELEASE_DATE + ", " + POSTER_PATH + ") VALUES (?, ?)";

        SQLiteStatement stmt = database.compileStatement(sql);

        stmt.bindString(1, movieItem.getTitle());
        stmt.bindString(2, movieItem.getOverview());
        stmt.bindString(3, movieItem.getReleaseDate());
        stmt.bindString(4, movieItem.getPosterPath());

        stmt.execute();

        stmt.clearBindings();
    }

    public int update(MovieItem movieItem) {
        ContentValues args = new ContentValues();

        args.put(TITLE, movieItem.getTitle());
        args.put(OVERVIEW, movieItem.getOverview());
        args.put(RELEASE_DATE, movieItem.getReleaseDate());
        args.put(POSTER_PATH, movieItem.getPosterPath());

        return database.update(TABLE_NAME, args, _ID + "= '" + movieItem.getId() + "'", null);
    }

    public int delete(int id) {
        return database.delete(TABLE_NAME, _ID + " = '" + id + "'", null);
    }

    /* ---------- Content Provider ---------- */
    public Cursor queryByIdProvider(String id){
        return database.query(TABLE_NAME
                ,null
                ,_ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }

    public Cursor queryProvider(){
        return database.query(TABLE_NAME
                ,null
                ,null
                ,null
                ,null
                ,null
                ,_ID + " DESC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(TABLE_NAME,null,values);
    }

    public int updateProvider(String id,ContentValues values){
        return database.update(TABLE_NAME,values,_ID +" = ?",new String[]{id} );
    }

    public int deleteProvider(String id){
        return database.delete(TABLE_NAME,_ID + " = ?", new String[]{id});
    }
}
