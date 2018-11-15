package com.devgeek.favouriteapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devgeek.favouriteapp.DetailActivity;
import com.devgeek.favouriteapp.R;
import com.devgeek.favouriteapp.database.DbFavourite;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.devgeek.favouriteapp.database.DbFavourite.CONTENT_URI;
import static com.devgeek.favouriteapp.database.DbFavourite.FavouriteColumns.OVERVIEW;
import static com.devgeek.favouriteapp.database.DbFavourite.FavouriteColumns.POSTER_PATH;
import static com.devgeek.favouriteapp.database.DbFavourite.FavouriteColumns.RELEASE_DATE;
import static com.devgeek.favouriteapp.database.DbFavourite.FavouriteColumns.TITLE;
import static com.devgeek.favouriteapp.database.DbFavourite.getColumnString;

/**
 * Created by GunSky7 on 6/8/2018.
 */

public class FavouriteAdapter extends CursorAdapter {
    @BindView(R.id.imgv_poster) ImageView imgvPoster;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_overview) TextView tvOverview;
    @BindView(R.id.tv_release_date) TextView tvReleaseDate;
    @BindView(R.id.btn_movie_detail) Button btnDetail;
    @BindView(R.id.btn_movie_share) Button btnShare;

    public FavouriteAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        if (cursor != null) {
            ButterKnife.bind(view);

            tvTitle.setText(getColumnString(cursor, TITLE));
            tvOverview.setText(getColumnString(cursor, OVERVIEW));
            tvReleaseDate.setText(getColumnString(cursor, RELEASE_DATE));

            Picasso.with(context)
                    .load("http://image.tmdb.org/t/p/w185" + getColumnString(cursor, POSTER_PATH))
                    .fit()
                    .into(imgvPoster);

            final int position = cursor.getPosition();

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(position);

                    Intent intent = new Intent(context, DetailActivity.class);

                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DbFavourite.FavouriteColumns._ID));

                    Uri uri = Uri.parse(CONTENT_URI + "/" + id);

                    intent.setData(uri);

                    context.startActivity(intent);
                }
            });

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor.moveToPosition(position);

                    Toast.makeText(context, "http://image.tmdb.org/t/p/w185" + getColumnString(cursor, POSTER_PATH), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
