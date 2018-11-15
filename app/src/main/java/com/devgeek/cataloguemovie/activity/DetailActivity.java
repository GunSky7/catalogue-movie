package com.devgeek.cataloguemovie.activity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devgeek.cataloguemovie.AppPreferences;
import com.devgeek.cataloguemovie.FavouriteHelper;
import com.devgeek.cataloguemovie.MovieItem;
import com.devgeek.cataloguemovie.R;
import com.squareup.picasso.Picasso;

import static com.devgeek.cataloguemovie.database.DbFavourite.CONTENT_URI;

public class DetailActivity extends AppCompatActivity {

    public static String ID = "id";
    public static String TITLE = "title";
    public static String RELEASE_DATE = "release_date";
    public static String OVERVIEW = "overview";
    public static String POSTER_PATH = "poster_path";

    private ImageView imgvDetailPoster, imgvToolbarPoster;
    private TextView tvDetailTitle, tvDetailReleaseDate, tvDetailOverview;
    private Button btnDetailWatch;

    FloatingActionButton fab;

    private AppPreferences appPreferences;

    private boolean favourite;
    private FavouriteHelper favouriteHelper;
    private MovieItem item;
    private String title;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra(TITLE));

        imgvToolbarPoster = findViewById(R.id.imgv_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab = findViewById(R.id.fab);

        imgvDetailPoster = findViewById(R.id.imgv_detail_poster);
        tvDetailTitle = findViewById(R.id.tv_detail_title);
        tvDetailReleaseDate = findViewById(R.id.tv_detail_release_date);
        tvDetailOverview = findViewById(R.id.tv_detail_overview);
        btnDetailWatch = findViewById(R.id.btn_detail_watch);

        /*tvDetailTitle.setText(getIntent().getStringExtra(TITLE));
        tvDetailReleaseDate.setText(getIntent().getStringExtra(RELEASE_DATE));
        tvDetailOverview.setText(getIntent().getStringExtra(OVERVIEW));*/

        favouriteHelper = new FavouriteHelper(getApplicationContext());

        uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null){
                if(cursor.moveToFirst()) item = new MovieItem(cursor);
                cursor.close();

                title = item.getTitle();

                tvDetailTitle.setText(item.getTitle());
                tvDetailReleaseDate.setText(item.getReleaseDate());
                tvDetailOverview.setText(item.getOverview());

                Picasso.with(getBaseContext())
                        .load("http://image.tmdb.org/t/p/w185" + item.getPosterPath())
                        .fit()
                        .into(imgvDetailPoster);

                Picasso.with(getBaseContext())
                        .load("http://image.tmdb.org/t/p/w185" + item.getPosterPath())
                        .fit()
                        .into(imgvToolbarPoster);
            }
        } else {
            title = getIntent().getStringExtra(TITLE);

            tvDetailTitle.setText(getIntent().getStringExtra(TITLE));
            tvDetailReleaseDate.setText(getIntent().getStringExtra(RELEASE_DATE));
            tvDetailOverview.setText(getIntent().getStringExtra(OVERVIEW));

            Picasso.with(getBaseContext())
                    .load("http://image.tmdb.org/t/p/w185" + getIntent().getStringExtra(POSTER_PATH))
                    .fit()
                    .into(imgvDetailPoster);

            Picasso.with(getBaseContext())
                    .load("http://image.tmdb.org/t/p/w185" + getIntent().getStringExtra(POSTER_PATH))
                    .fit()
                    .into(imgvToolbarPoster);

            item = new MovieItem();

            item.setId(getIntent().getIntExtra(ID, 0));
            item.setTitle(getIntent().getStringExtra(TITLE));
            item.setOverview(getIntent().getStringExtra(OVERVIEW));
            item.setReleaseDate(getIntent().getStringExtra(RELEASE_DATE));
            item.setPosterPath(getIntent().getStringExtra(POSTER_PATH));


        }
    }

    @Override
    public void onResume() {
        super.onResume();

        appPreferences = new AppPreferences(getApplicationContext());

        favourite = appPreferences.getFavourite(title);

        if (favourite) {
            fab.setImageResource(R.drawable.ic_star_black);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();

                if (favourite) {
                    if (uri == null) {
                        uri = Uri.parse(appPreferences.getUri(getIntent().getStringExtra(TITLE) + " uri"));
                    }
                    appPreferences.setFavourite(item.getTitle(), false);
                    favourite = false;

                    getContentResolver().delete(uri,null,null);

                    fab.setImageResource(R.drawable.ic_star_border_black);
                } else {
                    appPreferences.setFavourite(getIntent().getStringExtra(TITLE), true);
                    favourite = true;

                    values.put(TITLE, getIntent().getStringExtra(TITLE));
                    values.put(OVERVIEW, getIntent().getStringExtra(OVERVIEW));
                    values.put(RELEASE_DATE, getIntent().getStringExtra(RELEASE_DATE));
                    values.put(POSTER_PATH, getIntent().getStringExtra(POSTER_PATH));

                    uri = getContentResolver().insert(CONTENT_URI,values);

                    appPreferences.setUri(title + " uri", uri);

                    fab.setImageResource(R.drawable.ic_star_black);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
