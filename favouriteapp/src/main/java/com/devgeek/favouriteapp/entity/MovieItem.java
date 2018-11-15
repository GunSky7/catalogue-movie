package com.devgeek.favouriteapp.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.devgeek.favouriteapp.database.DbFavourite;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.provider.BaseColumns._ID;
import static com.devgeek.favouriteapp.database.DbFavourite.getColumnInt;
import static com.devgeek.favouriteapp.database.DbFavourite.getColumnString;

/**
 * Created by GunSky7 on 4/3/2018.
 */

public class MovieItem implements Parcelable {
    private int id;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;

    public MovieItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
    }

    public MovieItem(Cursor cursor){
        this.id = getColumnInt(cursor, _ID);
        this.title = getColumnString(cursor, DbFavourite.FavouriteColumns.TITLE);
        this.overview = getColumnString(cursor, DbFavourite.FavouriteColumns.OVERVIEW);
        this.releaseDate = getColumnString(cursor, DbFavourite.FavouriteColumns.RELEASE_DATE);
        this.posterPath = getColumnString(cursor, DbFavourite.FavouriteColumns.POSTER_PATH);
    }

    protected MovieItem(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {

        @Override
        public MovieItem createFromParcel(Parcel source) {
            return new MovieItem(source);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };
}
