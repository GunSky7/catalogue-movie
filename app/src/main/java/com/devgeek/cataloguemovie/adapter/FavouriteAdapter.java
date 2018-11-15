package com.devgeek.cataloguemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devgeek.cataloguemovie.MovieItem;
import com.devgeek.cataloguemovie.R;
import com.devgeek.cataloguemovie.activity.DetailActivity;
import com.squareup.picasso.Picasso;

import static com.devgeek.cataloguemovie.database.DbFavourite.CONTENT_URI;

/**
 * Created by GunSky7 on 6/8/2018.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private Context context;
    private Cursor listMovie;

    public FavouriteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MovieItem item = getItem(position);

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185" + item.getPosterPath())
                .fit()
                .into(holder.imgPoster);

        holder.imgPoster.setBackgroundColor(Color.GREEN);
        holder.tvTitle.setText(item.getTitle());
        holder.tvOverview.setText(item.getOverview());
        holder.tvReleaseDate.setText(item.getReleaseDate());

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);

                Uri uri = Uri.parse(CONTENT_URI + "/" + item.getId());

                intent.setData(uri);

                context.startActivity(intent);
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Movie is shared!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listMovie == null) return 0;
        return listMovie.getCount();
    }

    private MovieItem getItem(int position){
        if (!listMovie.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new MovieItem(listMovie);
    }

    public Cursor getListMovie() {
        return listMovie;
    }

    public void setListMovie(Cursor listMovie) {
        this.listMovie = listMovie;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvOverview, tvReleaseDate;
        Button btnDetail, btnShare;

        public ViewHolder(View itemView) {
            super(itemView);

            imgPoster = (ImageView) itemView.findViewById(R.id.imgv_poster);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvOverview = (TextView) itemView.findViewById(R.id.tv_overview);
            tvReleaseDate = (TextView) itemView.findViewById(R.id.tv_release_date);
            btnDetail = (Button) itemView.findViewById(R.id.btn_movie_detail);
            btnShare = (Button) itemView.findViewById(R.id.btn_movie_share);
        }
    }
}
