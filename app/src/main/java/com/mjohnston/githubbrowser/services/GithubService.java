package com.mjohnston.githubbrowser.services;

import io.reactivex.Observable;

import com.mjohnston.githubbrowser.model.SearchFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mattjohnston on 4/14/17.
 */

public interface GithubService {

    @GET("/search/repositories")
    Observable<SearchFeed> searchRepositories(@Query("q") String q);
}
