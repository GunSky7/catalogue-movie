package com.devgeek.favouriteapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devgeek.favouriteapp.adapter.FavouriteAdapter;
import com.devgeek.favouriteapp.database.DbFavourite;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.devgeek.favouriteapp.database.DbFavourite.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    private FavouriteAdapter favouriteAdapter;

    @BindView(R.id.lv_movies) ListView lvMovies;

    private final int LOAD_FAVOURITE = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Favourite Movies");

        ButterKnife.bind(this);

        favouriteAdapter = new FavouriteAdapter(this, null, true);

        lvMovies.setAdapter(favouriteAdapter);
        lvMovies.setOnItemClickListener(this);

        getSupportLoaderManager().initLoader(LOAD_FAVOURITE, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_FAVOURITE, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        favouriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        favouriteAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        Cursor cursor = (Cursor) favouriteAdapter.getItem(position);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbFavourite.FavouriteColumns._ID));

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.setData(Uri.parse(CONTENT_URI + "/" + id));

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOAD_FAVOURITE);
    }
}
