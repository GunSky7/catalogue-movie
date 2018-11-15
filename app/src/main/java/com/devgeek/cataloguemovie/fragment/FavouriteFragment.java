package com.devgeek.cataloguemovie.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.devgeek.cataloguemovie.FavouriteHelper;
import com.devgeek.cataloguemovie.MovieItem;
import com.devgeek.cataloguemovie.R;
import com.devgeek.cataloguemovie.adapter.FavouriteAdapter;
import com.devgeek.cataloguemovie.adapter.MovieAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.devgeek.cataloguemovie.database.DbFavourite.CONTENT_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment {

    @BindView(R.id.rv_favourite) RecyclerView rvFavourite;
    @BindView(R.id.pb_favourite) ProgressBar pbFavourite;

    private FavouriteAdapter favouriteAdapter;
    FavouriteHelper favouriteHelper;

    private Cursor list;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        ButterKnife.bind(this, view);

        // Untuk menyimpan data sehingga tidak perlu mendownload ulang data ketika terjadi perubahan state
        setRetainInstance(true);

        favouriteAdapter = new FavouriteAdapter(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        list = getActivity().getContentResolver().query(CONTENT_URI,null,null,null,null);

        rvFavourite.setVisibility(View.VISIBLE);
        pbFavourite.setVisibility(View.INVISIBLE);
        favouriteAdapter.setListMovie(list);
        favouriteAdapter.notifyDataSetChanged();

        rvFavourite.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavourite.setAdapter(favouriteAdapter);
    }
}
