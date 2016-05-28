package com.android.serverwarrior.popularmoviesone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder container containing a simple view.
 */
public class MovieListFragment extends Fragment  implements SharedPreferences.OnSharedPreferenceChangeListener {

    //private final String LOG_TAG = MovieListFragment.class.getSimpleName();
    final static int REQ_CODE = 1;
    private static final String STORED_MOVIES = "stored_movies";

    private ImageAdapter mMoviePosterAdapter;

    private SharedPreferences prefs;

    private String sortOrder;

    private final List<Movie> movies = new ArrayList<Movie>();



    public MovieListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext())
                .registerOnSharedPreferenceChangeListener(this);

        sortOrder = prefs.getString(getString(R.string.sort_array_key), getString(R.string.display_preferences_sort_default_value));

        if(savedInstanceState != null){
            ArrayList<Movie> storedMovies;
            storedMovies = savedInstanceState.getParcelableArrayList(STORED_MOVIES);
            movies.clear();
            movies.addAll(storedMovies);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.sort_array_key))){
            String prefSortOrder = prefs.getString(getString(R.string.sort_array_key), getString(R.string.display_preferences_sort_default_value));
            sortOrder = prefSortOrder;
            getMovies();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
       // return super.onOptionsItemSelected(item);

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            //startActivityForResult(new Intent(getActivity(), SettingsActivity.class), REQ_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMoviePosterAdapter = new ImageAdapter(
                getActivity(),
                R.layout.list_movie_poster,
                R.id.list_movie_poster_imageview,
                new ArrayList<String>());

        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.movie_gridview);
        gridview.setAdapter(mMoviePosterAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie details = movies.get(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("movies_details", details);
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        String prefSortOrder = prefs.getString(getString(R.string.sort_array_key), getString(R.string.display_preferences_sort_default_value));

      /*  if(movies.size() > 0 && prefSortOrder.equals(prefSortOrder)){
            updateMoviePosters();
        } else{*/
            sortOrder = prefSortOrder;
            getMovies();
       // }
    }

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String prefSortOrder = prefs.getString(getString(R.string.sort_array_key), getString(R.string.display_preferences_sort_default_value));
        if (requestCode == REQ_CODE) {
            // Make sure the request was successful
           if (resultCode == Activity.RESULT_OK) {
               sortOrder = prefSortOrder;
               getMovies();
           }
        }
    }*/


    private void getMovies() {
       FetchMoviePoster fetchMoviePoster = new FetchMoviePoster(new AsyncResponse() {
           @Override
           public void onTaskCompleted(List<Movie> results) {
               movies.clear();
               movies.addAll(results);
               updateMoviePosters();
           }
       });
        fetchMoviePoster.execute(sortOrder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> storedMovies = new ArrayList<Movie>();
        storedMovies.addAll(movies);
        outState.putParcelableArrayList(STORED_MOVIES, storedMovies);
    }

    private void updateMoviePosters() {
        mMoviePosterAdapter.clear();
        for(Movie movie : movies) {
            mMoviePosterAdapter.add(movie.getPoster());
        }
    }

}
