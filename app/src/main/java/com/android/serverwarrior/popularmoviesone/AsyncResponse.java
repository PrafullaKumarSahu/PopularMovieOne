package com.android.serverwarrior.popularmoviesone;

import java.util.List;

/**
 * Created by Server Warrior on 5/2/2016.
 */
public interface AsyncResponse {
    void onTaskCompleted(List<Movie> results);
}
