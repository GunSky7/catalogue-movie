package com.devgeek.cataloguemovie.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.devgeek.cataloguemovie.BuildConfig;
import com.devgeek.cataloguemovie.adapter.MovieAdapter;
import com.devgeek.cataloguemovie.MovieAsyncTaskLoader;
import com.devgeek.cataloguemovie.MovieItem;
import com.devgeek.cataloguemovie.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieItem>> {
    public static final String TAG = HomeFragment.class.getSimpleName();
    static final String KEYWORDS = "keywords";
    static final String STATE_SEARCH = "state_search";
    static final String STATE_LIST = "state_list";

    private String keywords = "";
    private ArrayList<MovieItem> mData;

    private MovieAdapter movieAdapter;

    @BindView(R.id.sv_home) SearchView svHome;
    @BindView(R.id.pb_list_movie) ProgressBar pbMovies;
    @BindView(R.id.rv_home) RecyclerView rvHome;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        svHome.setIconified(false);
        svHome.clearFocus();

        rvHome.setVisibility(View.INVISIBLE);
        pbMovies.setVisibility(View.INVISIBLE);

        movieAdapter = new MovieAdapter(getContext());

        getLoaderManager().initLoader(0, null, this);

        svHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                svHome.clearFocus();

                keywords = query;

                Bundle bundle = new Bundle();
                bundle.putString(KEYWORDS, keywords);

                rvHome.setVisibility(View.INVISIBLE);
                pbMovies.setVisibility(View.VISIBLE);

                getLoaderManager().restartLoader(0, bundle, HomeFragment.this);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    @Override
    public Loader<ArrayList<MovieItem>> onCreateLoader(int id, Bundle args) {
        String keywords = "";

        if (args != null) {
            keywords = args.getString(KEYWORDS);
        }

        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + BuildConfig.THE_MOVIE_DB_API_KEY + "&language=en-US&query=" + keywords;

        return new MovieAsyncTaskLoader(getContext(), url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItem>> loader, ArrayList<MovieItem> data) {
        rvHome.setVisibility(View.VISIBLE);
        pbMovies.setVisibility(View.INVISIBLE);

        movieAdapter.setListMovie(data);
        movieAdapter.notifyDataSetChanged();

        mData = data;

        rvHome.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvHome.setAdapter(movieAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItem>> loader) {
        movieAdapter.setListMovie(null);
        movieAdapter.notifyDataSetChanged();
    }
}
