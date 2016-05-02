package com.android.serverwarrior.popularmoviesone;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Server Warrior on 5/1/2016.
 */
// public class FetchMoviePoster extends AsyncTask<String[], Void,  String[][]> {
public class FetchMoviePoster extends AsyncTask<String[], Void,  List<Movie>> {

    private final String LOG_TAG = FetchMoviePoster.class.getSimpleName();
    private final String MOVIE_POSTER_BASE = "http://image.tmdb.org/t/p/";
    private final String MOVIE_POSTER_SIZE ="w185";

    @Override
    //    protected  String[][] doInBackground(String[]... params) {
    protected List<Movie> doInBackground(String[]... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieResponseJsonStr = null;

        try {
            String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
            String apiKey = "&api_key=" + BuildConfig.MOVIE_DB_API_KEY;
            URL url = new URL(baseUrl.concat(apiKey));

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // return null;
                movieResponseJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append((line + "\n"));
            }

            if (buffer.length() == 0) {
                //return null;
                movieResponseJsonStr = null;
            }
            movieResponseJsonStr = buffer.toString();

            Log.v("Response", movieResponseJsonStr);
        } catch (IOException e) {
            Log.e("URL connection", "Error", e);
            //return null;
            movieResponseJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("URL connection", "Error", e);
                }
            }
        }

        try {
            return getMoviePostersFromJson(movieResponseJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    //   private String[][] getMoviePostersFromJson(String movieResponseJsonStr) throws JSONException {
    private List<Movie> getMoviePostersFromJson(String movieResponseJsonStr) throws JSONException {
        {
            final String MD_ARRAY_OF_MOVIES = "results";
            final String MD_POSTER_PATH = "poster_path";
            final String MD_OVERVIEW = "overview";
            final String MD_RELEASE_DATE = "release_date";
            final String MD_ORIGINAL_TITLE = "original_title";
            final String MD_VOTE_AVG = "vote_average";

            JSONObject moviePostersJson = new JSONObject(movieResponseJsonStr);
            JSONArray moviePosterArray = moviePostersJson.getJSONArray(MD_ARRAY_OF_MOVIES);

            List<Movie> movies = new ArrayList<Movie>();

            for(int i = 0; i < moviePosterArray.length(); i++ ){
                JSONObject movie = moviePosterArray.getJSONObject(i);
                String title = movie.getString(MD_ORIGINAL_TITLE);
                String poster = MOVIE_POSTER_BASE + MOVIE_POSTER_SIZE + movie.getString(MD_POSTER_PATH);
                String overview = movie.getString(MD_OVERVIEW);
                String voteAverage = movie.getString(MD_VOTE_AVG);
                String releaseDate = getYear(movie.getString(MD_RELEASE_DATE));;
                movies.add(new Movie(title, poster, overview, voteAverage, releaseDate));
            }
            return movies;
        }
    }

    private String getYear(String date) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar cal = Calendar.getInstance();
        try{
            cal.setTime(dateFormat.parse(date));
        }catch(ParseException e){
            e.printStackTrace();
        }
        return Integer.toString(cal.get(Calendar.YEAR));
    }

}