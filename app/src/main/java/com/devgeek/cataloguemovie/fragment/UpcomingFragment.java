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
public class UpcomingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItem>> {
    public static final String TAG = UpcomingFragment.class.getSimpleName();

    public RecyclerView rvUpcoming;

    public ProgressBar pbUpcoming;

    public MovieAdapter movieAdapter;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        rvUpcoming = (RecyclerView) view.findViewById(R.id.rv_upcoming);

        pbUpcoming = (ProgressBar) view.findViewById(R.id.pb_upcoming);

        rvUpcoming.setVisibility(View.INVISIBLE);
        pbUpcoming.setVisibility(View.INVISIBLE);

        movieAdapter = new MovieAdapter(getContext());

        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public Loader<ArrayList<MovieItem>> onCreateLoader(int id, Bundle args) {
        pbUpcoming.setVisibility(View.VISIBLE);

        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY + "&language=en-US";

        return new MovieAsyncTaskLoader(getContext(), url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> data) {
        rvUpcoming.setVisibility(View.VISIBLE);
        pbUpcoming.setVisibility(View.INVISIBLE);

        movieAdapter.setListMovie(data);
        movieAdapter.notifyDataSetChanged();

        rvUpcoming.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvUpcoming.setAdapter(movieAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {
        movieAdapter.setListMovie(null);
        movieAdapter.notifyDataSetChanged();
    }
}
