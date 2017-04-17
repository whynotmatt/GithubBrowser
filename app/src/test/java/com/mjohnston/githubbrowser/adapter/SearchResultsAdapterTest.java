package com.mjohnston.githubbrowser.adapter;

import com.mjohnston.githubbrowser.BuildConfig;
import com.mjohnston.githubbrowser.model.Repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * we need to use the Robolectric test runner because the SearchResultsAdapter includes
 * Android classes that are not available in normal junit tests.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SearchResultsAdapterTest {

    @Test
    public void testMaxResults() {

        List<Repository> tooMuchData = new ArrayList();
        for (int i=0; i< SearchResultsAdapter.MAX_RESULTS + 5; i++) {
            Repository repo = new Repository();
            repo.setName("repo " + i);
            tooMuchData.add(repo);
        }

        // adapter limits results to MAX_RESULTS
        SearchResultsAdapter adapter = new SearchResultsAdapter(tooMuchData, null);
        Assert.assertEquals(SearchResultsAdapter.MAX_RESULTS, adapter.getItemCount());



        List<Repository> tooLittleData = new ArrayList();
        for (int i=0; i< SearchResultsAdapter.MAX_RESULTS - 5; i++) {
            Repository repo = new Repository();
            repo.setName("repo " + i);
            tooLittleData.add(repo);
        }

        SearchResultsAdapter adapter2 = new SearchResultsAdapter(tooLittleData, null);
        Assert.assertEquals(tooLittleData.size(), adapter2.getItemCount());


    }
}

