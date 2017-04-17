package com.mjohnston.githubbrowser.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mjohnston.githubbrowser.services.GithubService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mattjohnston on 4/14/17.
 */

@Module
public class ApplicationModule {

    public static String GITHUB_BASE_URL = "https://api.github.com";

    @Provides
    @Singleton
    GithubService providesGithubService(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GITHUB_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(GithubService.class);
    }

    @Provides
    Gson providesGson() {

        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
    }

}
