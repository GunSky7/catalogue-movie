package com.devgeek.cataloguemovie;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.devgeek.cataloguemovie.database.DbFavourite;
import com.devgeek.cataloguemovie.widget.FavouriteWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.OVERVIEW;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.POSTER_PATH;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.RELEASE_DATE;
import static com.devgeek.cataloguemovie.database.DbFavourite.FavouriteColumns.TITLE;

/**
 * Created by GunSky7 on 6/25/2018.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private List<Bitmap> mWidgetItems = new ArrayList<>();
    private List<MovieItem> mMovies = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    private Cursor cursor;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();

        String[] projection = new String[]{BaseColumns._ID, TITLE, OVERVIEW, RELEASE_DATE, POSTER_PATH};

        cursor = mContext.getContentResolver().query(
                DbFavourite.CONTENT_URI,
                projection,
                null,
                null,
                null);

        Binder.restoreCallingIdentity(identityToken);

        if (cursor.moveToFirst()) {
            do {
                MovieItem item = new MovieItem();

                item.setId(cursor.getInt(0));
                item.setTitle(cursor.getString(1));
                item.setOverview(cursor.getString(2));
                item.setReleaseDate(cursor.getString(3));
                item.setPosterPath(cursor.getString(4));

                mMovies.add(item);
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

//        Log.e("Isi list movie", mMovies.toString());

        Bitmap bmp = null;
        try {
            bmp = Glide.with(mContext)
                    .asBitmap()
                    .load("http://image.tmdb.org/t/p/w185" + mMovies.get(position).getPosterPath())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

        }catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error","error");
        }

        rv.setImageViewBitmap(R.id.imgv_widget_poster, bmp);
        rv.setTextViewText(R.id.tv_widget_release_date, mMovies.get(position).getReleaseDate());

        Bundle extras = new Bundle();
        extras.putInt(FavouriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
