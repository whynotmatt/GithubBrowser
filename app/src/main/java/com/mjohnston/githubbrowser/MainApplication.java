package com.mjohnston.githubbrowser;

import android.app.Application;

import com.mjohnston.githubbrowser.di.ApplicationComponent;
import com.mjohnston.githubbrowser.di.ApplicationModule;
import com.mjohnston.githubbrowser.di.DaggerApplicationComponent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by mattjohnston on 4/15/17.
 */

public class MainApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // create dagger components
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule()).build();

        // setup remote image loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

    }


    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
