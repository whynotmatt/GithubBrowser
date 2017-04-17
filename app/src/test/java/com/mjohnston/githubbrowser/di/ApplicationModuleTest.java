package com.mjohnston.githubbrowser.di;


import com.google.gson.Gson;
import com.mjohnston.githubbrowser.services.GithubService;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Created by mattjohnston on 4/15/17.
 */

public class ApplicationModuleTest {

    @Test
    public void returnsGitService() {

        ApplicationModule module = new ApplicationModule();

        Gson gson = new Gson();
        GithubService service = module.providesGithubService(gson);

        assertNotNull(service);

    }

    @Test
    public void returnsGson() {

        ApplicationModule module = new ApplicationModule();
        assertNotNull(module.providesGson());
    }
}
