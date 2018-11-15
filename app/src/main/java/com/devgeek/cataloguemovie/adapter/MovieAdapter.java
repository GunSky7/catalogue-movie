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

import com.devgeek.cataloguemovie.activity.DetailActivity;
import com.devgeek.cataloguemovie.MovieItem;
import com.devgeek.cataloguemovie.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.devgeek.cataloguemovie.database.DbFavourite.CONTENT_URI;

/**
 * Created by GunSky7 on 4/3/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MovieItem> listMovie;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MovieItem item = getListMovie().get(position);

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185" + item.getPosterPath())
                .fit()
                .into(holder.imgPoster);

        holder.tvTitle.setText(item.getTitle());
        holder.tvOverview.setText(item.getOverview());
        holder.tvReleaseDate.setText(item.getReleaseDate());

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);

                intent.putExtra(DetailActivity.ID, item.getId());
                intent.putExtra(DetailActivity.TITLE, item.getTitle());
                intent.putExtra(DetailActivity.OVERVIEW, item.getOverview());
                intent.putExtra(DetailActivity.RELEASE_DATE, item.getReleaseDate());
                intent.putExtra(DetailActivity.POSTER_PATH, item.getPosterPath());

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
        return getListMovie().size();
    }

    public ArrayList<MovieItem> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<MovieItem> listMovie) {
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
