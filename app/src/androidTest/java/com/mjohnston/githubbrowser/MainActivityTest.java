package com.mjohnston.githubbrowser;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.mjohnston.githubbrowser.di.ApplicationComponent;
import com.mjohnston.githubbrowser.di.DaggerApplicationComponent;
import com.mjohnston.githubbrowser.di.MockApplicationModule;
import com.mjohnston.githubbrowser.fragment.DetailFragment;
import com.mjohnston.githubbrowser.fragment.ResultsFragment;
import com.mjohnston.githubbrowser.fragment.WebFragment;
import com.mjohnston.githubbrowser.model.SearchFeed;
import com.mjohnston.githubbrowser.services.GithubService;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import junit.framework.Assert;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;

import io.reactivex.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.web.assertion.WebViewAssertions.webMatches;
import static android.support.test.espresso.web.model.Atoms.getCurrentUrl;
import static android.support.test.espresso.web.sugar.Web.onWebView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by mattjohnston on 4/15/17.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class) {

        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();

            // setup mock components
            githubService = mock(GithubService.class);

            MockApplicationModule applicationModule = new MockApplicationModule();
            applicationModule.setGithubService(githubService);

            // assign the mock component to the application
            ApplicationComponent applicationComponent = DaggerApplicationComponent.builder().applicationModule(applicationModule).build();
            getApp().setApplicationComponent(applicationComponent);
        }

    };


    private GithubService githubService;


    private MainApplication getApp() {
        return (MainApplication) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }


    @Test
    public void searchText() {

        String searchString = "search string";

        // mock github response
        SearchFeed feed = MockHelper.loadSearchFeedFixture(InstrumentationRegistry.getInstrumentation()
                .getContext(), "github_search.json");

        Observable<SearchFeed> observable = Observable.just(feed);


        when(githubService.searchRepositories(searchString)).thenReturn(observable);

        // make sure the search view is open
        onView(withId(R.id.searchView)).perform(click());

        onView(isAssignableFrom(EditText.class)).perform(typeText(searchString), pressImeActionButton());

        // instructions should be hidden
        onView(withId(R.id.instructions)).check(matches(not(isDisplayed())));

        // make sure search was called
        verify(githubService).searchRepositories(searchString);

        // check fragment
        FragmentManager fragmentManager = mActivityRule.getActivity().getSupportFragmentManager();
        Assert.assertTrue(fragmentManager.findFragmentById(R.id.content) instanceof ResultsFragment);
        ResultsFragment fragment = (ResultsFragment) fragmentManager.findFragmentById(R.id.content);

        Assert.assertEquals(0, fragmentManager.getBackStackEntryCount());

        // hide empty view when results are available
        onView(withId(R.id.noResults)).check(matches(not(isDisplayed())));

        // check that we have results
        RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.recyclerView);
        Assert.assertEquals(20, recyclerView.getAdapter().getItemCount());

    }

    @Test
    public void navigateToDetail() {

        // mock github response
        SearchFeed feed = MockHelper.loadSearchFeedFixture(InstrumentationRegistry.getInstrumentation()
                .getContext(), "github_search.json");


        /*
        Ideally we would like to test the fragment directly. To get to the search results we have to
        first navigate through the app to get to teh detail screen. A way to optimize this process
        would be to create a TestActivity where you can assign a single fragment and test the
        fragment independently. Or we can create unit tests using Roboelectric to bypass the
        android instrumentation layer.
         */
        searchText();


        // check fragment
        FragmentManager fragmentManager = mActivityRule.getActivity().getSupportFragmentManager();


        // click on first item in result list
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // fragment should navigate to detail
        Assert.assertNotNull(fragmentManager.findFragmentByTag(DetailFragment.FRAGMENT_TAG));

        // check that detail fragment has been added
        Assert.assertEquals(1, fragmentManager.getBackStackEntryCount());

        Assert.assertTrue(fragmentManager.findFragmentById(R.id.content) instanceof  DetailFragment);
        DetailFragment fragment = (DetailFragment) fragmentManager.findFragmentByTag(DetailFragment.FRAGMENT_TAG);

        Assert.assertTrue(fragment.getArguments().containsKey(DetailFragment.REPOSITORY));

        // make sure repository was passed to fragment
        Assert.assertEquals(feed.getItems().get(0), fragment.getArguments().get(DetailFragment.REPOSITORY));

    }

    @Test
    public void viewRepoInternal() {

        // mock github response
        SearchFeed feed = MockHelper.loadSearchFeedFixture(InstrumentationRegistry.getInstrumentation()
                .getContext(), "github_search.json");

        try {

            // mock external webpage request
            MockWebServer server = new MockWebServer();
            server.enqueue(new MockResponse().setBody("test success"));
            server.play();
            URL mockUrl = server.getUrl("/repo");


            navigateToDetail();

            FragmentManager fragmentManager = mActivityRule.getActivity().getSupportFragmentManager();

            // set repo url to mock
            DetailFragment detailFragment = (DetailFragment)fragmentManager.findFragmentById(R.id.content);
            detailFragment.getRepository().setLink(mockUrl.toString());

            // fragment should navigate to git webpage
            onView(withId(R.id.internalLink)).perform(click());

            // check that web fragment has been added
            Assert.assertEquals(2, fragmentManager.getBackStackEntryCount());

            // fragment should navigate to web
            Assert.assertTrue(fragmentManager.findFragmentById(R.id.content) instanceof WebFragment);
            onWebView().withNoTimeout().check(webMatches(getCurrentUrl(), containsString(mockUrl.toString())));


            onView(withId(R.id.closeBtn)).perform(click());

            // back sure web fragment has been removed
            Assert.assertEquals(1, fragmentManager.getBackStackEntryCount());

            Assert.assertTrue(fragmentManager.findFragmentById(R.id.content) instanceof DetailFragment);


        }
        catch (IOException e) {
            Assert.fail("error testing internal repo web browser: " + e.getMessage());
        }

    }

    @Test
    public void viewRepoExternal() {

        Intents.init();

        // mock github response
        SearchFeed feed = MockHelper.loadSearchFeedFixture(InstrumentationRegistry.getInstrumentation()
                .getContext(), "github_search.json");

        navigateToDetail();

        Matcher expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), hasData(feed.getItems().get(0).getLink()));
        intending(expectedIntent).respondWith(new Instrumentation.ActivityResult(0, null));

        // fragment should open webpage externally
        onView(withId(R.id.externalLink)).perform(click());

        // check that web page is loaded in external browser
        intended(expectedIntent);

        Intents.release();
    }

    @Test
    public void searchText_empty() {

        String searchString = "";

        // make sure the search view is open
        onView(withId(R.id.searchView)).perform(click());

        onView(isAssignableFrom(EditText.class)).perform(typeText(searchString), pressImeActionButton());

        // no search, instructions should not be hidden
        onView(withId(R.id.instructions)).check(matches(isDisplayed()));

        // search should not be called
        verify(githubService, never()).searchRepositories(anyString());

    }


}
