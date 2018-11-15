package com.devgeek.favouriteapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devgeek.favouriteapp.entity.MovieItem;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.imgv_detail_poster) ImageView imgvDetailPoster;
    @BindView(R.id.imgv_toolbar) ImageView imgvToolbarPoster;
    @BindView(R.id.tv_detail_title) TextView tvDetailTitle;
    @BindView(R.id.tv_detail_overview) TextView tvDetailOverview;
    @BindView(R.id.tv_detail_release_date) TextView tvDetailReleaseDate;
    @BindView(R.id.btn_detail_watch) Button btnDetailWatch;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Uri uri;
    private MovieItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null){
                if(cursor.moveToFirst()) item = new MovieItem(cursor);
                cursor.close();

                toolbar.setTitle(item.getTitle());

                tvDetailTitle.setText(item.getTitle());
                tvDetailReleaseDate.setText(item.getReleaseDate());
                tvDetailOverview.setText(item.getOverview());

                Picasso.with(getBaseContext())
                        .load("http://image.tmdb.org/t/p/w185" + item.getPosterPath())
                        .fit()
                        .into(imgvDetailPoster);

                Picasso.with(getApplicationContext())
                        .load("http://image.tmdb.org/t/p/w185" + item.getPosterPath())
                        .fit()
                        .into(imgvToolbarPoster);
            }
        } else {
            toolbar.setTitle("Not Found!");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailActivity.this, "Please go to Main Apps to change favourite!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
