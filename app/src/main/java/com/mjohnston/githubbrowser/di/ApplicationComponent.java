package com.mjohnston.githubbrowser.di;

import com.mjohnston.githubbrowser.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by mattjohnston on 4/14/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {


    void inject(MainActivity activity);
}
