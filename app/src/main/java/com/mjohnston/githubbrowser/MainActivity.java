package com.mjohnston.githubbrowser;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mjohnston.githubbrowser.di.ApplicationComponent;
import com.mjohnston.githubbrowser.fragment.BaseFragment;
import com.mjohnston.githubbrowser.fragment.ResultsFragment;
import com.mjohnston.githubbrowser.model.SearchFeed;
import com.mjohnston.githubbrowser.services.GithubService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.toString();

    @BindView(R.id.searchView)
    protected SearchView searchView;

    @BindView(R.id.instructions)
    protected TextView instructions;

    @Inject
    protected GithubService githubService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // setup Dagger
        ApplicationComponent applicationComponent = ((MainApplication)getApplication()).getApplicationComponent();
        applicationComponent.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupSearchView();

        // if activity is being recreated, like during orientation change, don't recreate the fragment
        if (savedInstanceState == null) {
            // add default results fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            ResultsFragment resultsFragment = new ResultsFragment();
            fragmentTransaction.add(R.id.content, resultsFragment, ResultsFragment.FRAGMENT_TAG);
            fragmentTransaction.commit();
        }

    }


    protected void setupSearchView() {

        // setup search view
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (!query.isEmpty()) {
                    // execute search
                    doSearch(query);

                    // remove focus from search box
                    searchView.clearFocus();

                    // hide instructions after a search is performed
                    instructions.setVisibility(View.GONE);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                // show instructions when query is empty
                if (newText.isEmpty()) {
                    instructions.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        // hide instructions if the search view contains a query when this activity was resumed
        if (searchView.getQuery() != null && !searchView.getQuery().toString().isEmpty()) {
            instructions.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {

        // get current fragment
        BaseFragment fragment = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.content);

        if (fragment != null && fragment.interceptBackPressed()) {
            // the fragment handled the back press
        }
        // include fragment backstack when clicking back button
        else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    protected void doSearch(String query) {

        Observable<SearchFeed> repoQuery = githubService.searchRepositories(query);

        repoQuery.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<SearchFeed>() {
                    @Override
                    public void onNext(SearchFeed value) {
                        updateSearchResults(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"error searching repo", e);

                        // let results fragment update itself with new search state
                        ResultsFragment fragment = (ResultsFragment)getSupportFragmentManager().findFragmentById(R.id.content);
                        fragment.stopSearch();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


        FragmentManager fragmentManager = getSupportFragmentManager();

        // if we are searching from a detail screen, remove all child fragments
        // and only show the results fragment
        if (!(fragmentManager.findFragmentById(R.id.content) instanceof ResultsFragment)) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }


        // notify Results fragment that search has started
        ResultsFragment fragment = (ResultsFragment)fragmentManager.findFragmentByTag(ResultsFragment.FRAGMENT_TAG);
        if (fragment != null) {
            fragment.startSearch();
        }


    }



    protected void updateSearchResults(SearchFeed searchFeed) {

        FragmentManager fragmentManager = getSupportFragmentManager();

        // update results fragment with new results
        ResultsFragment fragment = (ResultsFragment) fragmentManager.findFragmentByTag(ResultsFragment.FRAGMENT_TAG);
        fragment.setSearchResults(searchFeed.getItems());
    }
}
