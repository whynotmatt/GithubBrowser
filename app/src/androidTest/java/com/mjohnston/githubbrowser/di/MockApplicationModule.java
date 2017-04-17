package com.mjohnston.githubbrowser.di;

import com.google.gson.Gson;
import com.mjohnston.githubbrowser.services.GithubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mattjohnston on 4/15/17.
 */

@Module
public class MockApplicationModule extends ApplicationModule  {

    private GithubService githubService;

    public void setGithubService(GithubService githubService) {
        this.githubService = githubService;
    }

    @Singleton
    @Provides
    GithubService providesGithubService(Gson gson) {

        return githubService;
    }



}
