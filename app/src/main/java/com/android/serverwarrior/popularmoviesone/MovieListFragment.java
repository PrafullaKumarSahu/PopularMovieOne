package com.android.serverwarrior.popularmoviesone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment {

    public ImageAdapter mImageAdapter;
    public MovieListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.movie_gridview);
        gridview.setAdapter(new ImageAdapter(getContext()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviePosterss();
    }

    private void updateMoviePosterss() {
        FetchMoviePoster moviePosters = new FetchMoviePoster();
        moviePosters.execute();
    }

   // public class FetchMoviePoster extends AsyncTask<String[], Void,  String[][]> {
    public class FetchMoviePoster extends AsyncTask<String[], Void,  String[]> {

        private final String LOG_TAG = FetchMoviePoster.class.getSimpleName();

        @Override
   //    protected  String[][] doInBackground(String[]... params) {
        protected  String[] doInBackground(String[]... params) {

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

                //Log.v("Response", movieResponseJsonStr);
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
       private String[] getMoviePostersFromJson(String movieResponseJsonStr) throws JSONException {
            {
                final String MD_PAGE = "page";
                final String MD_RESULTS = "results";
                final String MD_POSTER_PATH = "poster_path";
                final String MD_ADULT = "adult";
                final String MD_OVERVIEW = "overview";
                final String MD_RELEASE_DATE = "release_date";
                final String MD_GENER_IDS = "gener_ids";
                final String MD_MOVIE_ID = "id";
                final String MD_ORIGINAL_TITLE = "original_title";
                final String MD_ORIGINAL_LANGUAGE = "original_language";
                final String MD_TITLE = "title";
                final String MD_BACKDROP_PATH = "backdrop_path";
                final String MD_POPULARITY = "popularity";
                final String MD_VOTE_COUNT = "vote_count";
                final String MD_VIDEO = "video";
                final String MD_VOTE_AVG = "vote_average";



                JSONObject moviePostersJson = new JSONObject(movieResponseJsonStr);
              //  Log.v("Json data", moviePostersJson + "");
                JSONArray moviePosterArray = moviePostersJson.getJSONArray("results");

               // String[][] resultStrs = new String[moviePosterArray.length()][16];
                String[] resultStrs = new String[moviePosterArray.length()];

                for(int i = 0; i < moviePosterArray.length(); i++ ){
                    //resultStrs[moviePosterArray.getJSONObject(i).getInt("id")][i]  =  moviePosterArray.getJSONObject(i).getString("original_title");
                    resultStrs[i]  =  moviePosterArray.getJSONObject(i).getString("original_title");
                   // Log.v("Json data", moviePosterArray.getJSONObject(i).getString("original_title") + " bcd");
                }

                //Log.v("Json data", resultStrs + " bcd");
                //JSONObject poster_path = results.getJSONObject("poster_path");

                return resultStrs;
            }
        }

        /*@Override
        protected void onPostExecute(String[][] results) {
            super.onPostExecute(results);
           // Log.v("results", results + "" );
            if(results != null){
                //mImageAdapter.clear();
                for(String[] movieDetails : results){
                    new ImageAdapter(movieDetails);
                }
            }
        }*/
    }
}
