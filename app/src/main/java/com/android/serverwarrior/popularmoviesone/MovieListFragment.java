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

    public MovieListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

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

    public class FetchMoviePoster extends AsyncTask<Void, Void, Void>{

        private final String LOG_TAG = FetchMoviePoster.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieResponseJsonStr = null;

            try{
                String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc";
                String apiKey = "&api_key=" + BuildConfig.MOVIE_DB_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream == null){
                   // return null;
                    movieResponseJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null){
                    buffer.append((line + "\n"));
                }

                if(buffer.length() == 0){
                    //return null;
                    movieResponseJsonStr = null;
                }
                movieResponseJsonStr = buffer.toString();

                Log.v("Response", movieResponseJsonStr);
            }catch(IOException e){
                Log.e("URL connection", "Error", e);
                //return null;
                movieResponseJsonStr = null;
            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch (final IOException e){
                        Log.e("URL connection", "Error", e);
                    }
                }
            }
            return  null;
        }
    }
}
