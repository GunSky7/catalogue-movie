package com.devgeek.cataloguemovie.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.devgeek.cataloguemovie.BuildConfig;
import com.devgeek.cataloguemovie.adapter.MovieAdapter;
import com.devgeek.cataloguemovie.MovieAsyncTaskLoader;
import com.devgeek.cataloguemovie.MovieItem;
import com.devgeek.cataloguemovie.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItem>> {
    public static final String TAG = NowPlayingFragment.class.getSimpleName();

    public RecyclerView rvNowPlaying;
    public ProgressBar pbNowPlaying;
    public MovieAdapter movieAdapter;

    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        rvNowPlaying = (RecyclerView) view.findViewById(R.id.rv_now_playing);

        pbNowPlaying = (ProgressBar) view.findViewById(R.id.pb_now_playing);

        rvNowPlaying.setVisibility(View.INVISIBLE);
        pbNowPlaying.setVisibility(View.INVISIBLE);

        movieAdapter = new MovieAdapter(getContext());

        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public Loader<ArrayList<MovieItem>> onCreateLoader(int id, Bundle args) {
        pbNowPlaying.setVisibility(View.VISIBLE);

        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY + "&language=en-US";

        return new MovieAsyncTaskLoader(getContext(), url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> data) {
        rvNowPlaying.setVisibility(View.VISIBLE);
        pbNowPlaying.setVisibility(View.INVISIBLE);

        movieAdapter.setListMovie(data);
        movieAdapter.notifyDataSetChanged();

        rvNowPlaying.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNowPlaying.setAdapter(movieAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {
        movieAdapter.setListMovie(null);
        movieAdapter.notifyDataSetChanged();
    }
}
