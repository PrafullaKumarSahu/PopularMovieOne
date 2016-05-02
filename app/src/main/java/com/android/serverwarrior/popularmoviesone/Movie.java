package com.android.serverwarrior.popularmoviesone;

/**
 * Created by Server Warrior on 5/1/2016.
 */
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Movie implements Parcelable {
    private String title;
    private String poster;
    private String overview;
    private String voteAverage;
    private String releaseDate;

    public Movie(String title, String poster, String overview,
                 String voteAverage, String releaseDate){
        this.title = title;
        this.poster = poster;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        Log.v("Movie title", title);
        return title;
    }

    public String getPoster() {
        Log.v("Movie poster", poster);
        return poster;
    }

    public String getOverview() {
        Log.v("Movie overview", overview);
        return overview;
    }

    public String getVoteAverage() {
        Log.v("Movie voteAverage", voteAverage);
        return voteAverage;
    }

    public String getReleaseDate() {
        Log.v("Movie releaseDate", releaseDate);
        return releaseDate;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(poster);
        out.writeString(overview);
        out.writeString(voteAverage);
        out.writeString(releaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Movie(Parcel in) {
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            Log.v("Movie size", size + "" );
            return new Movie[size];
        }
    };
}
