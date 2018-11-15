package com.devgeek.cataloguemovie;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by GunSky7 on 4/3/2018.
 */

public class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItem>> {
    private ArrayList<MovieItem> mData;

    private boolean mHasResult = false;

    private String url;

    public MovieAsyncTaskLoader(Context context, String url) {
        super(context);

        onContentChanged();

        this.url = url;
    }

    @Override
    public void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        } else if (mHasResult) {
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(ArrayList<MovieItem> data) {
        mData = data;
        mHasResult = true;

        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (mHasResult) {
            onReleaseResources(mData);

            mData = null;

            mHasResult = false;
        }
    }

    @Override
    public ArrayList<MovieItem> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItem> movieItems = new ArrayList<>();

        Log.d("loadInBackground()", url);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();

                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);

                    JSONObject reponseObject = new JSONObject(result);

                    JSONArray list = reponseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);

                        MovieItem item = new MovieItem(movie);

                        movieItems.add(item);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return movieItems;
    }

    protected void onReleaseResources(ArrayList<MovieItem> data) {

    }
}
