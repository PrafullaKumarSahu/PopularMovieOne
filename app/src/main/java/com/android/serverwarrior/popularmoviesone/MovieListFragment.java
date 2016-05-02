package com.android.serverwarrior.popularmoviesone;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment {

    private static final String STORED_MOVIES = "stored_movies";

    private ImageAdapter mMoviePosterAdapter;

    private final String LOG_TAG = MovieListFragment.class.getSimpleName();

    List<Movie> movies = new ArrayList<Movie>();

    private SharedPreferences prefs;

    public MovieListFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if(savedInstanceState != null){
            ArrayList<Movie> storedMovies;
            storedMovies = savedInstanceState.<Movie>getParcelableArrayList(STORED_MOVIES);
            movies.clear();
            movies.addAll(storedMovies);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

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
                Toast.makeText(getContext(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviePosters();
    }

    private void updateMoviePosters() {
        FetchMoviePoster moviePosters = new FetchMoviePoster();
        moviePosters.execute();
    }

}
