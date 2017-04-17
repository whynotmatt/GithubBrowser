package com.mjohnston.githubbrowser.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mjohnston.githubbrowser.R;
import com.mjohnston.githubbrowser.adapter.SearchResultsAdapter;
import com.mjohnston.githubbrowser.model.Repository;
import com.mjohnston.githubbrowser.view.ResultViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mattjohnston on 4/14/17.
 */

public class ResultsFragment extends BaseFragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.noResults)
    TextView noResultsView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    protected SearchResultsAdapter adapter;
    protected List<Repository> data;

    public static final String RESULTS = "results";

    public static final String FRAGMENT_TAG = "results";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data = new ArrayList<>();

        if (getArguments() != null && getArguments().containsKey(RESULTS) && getArguments().getSerializable(RESULTS) != null) {
            data.addAll((List<Repository>)getArguments().getSerializable(RESULTS));
        }

        adapter = new SearchResultsAdapter(data, clickListener);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_results, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(RESULTS)) {
            data.clear();
            data.addAll((List<Repository>)savedInstanceState.getSerializable(RESULTS));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(RESULTS, (Serializable)data);
    }

    public void setSearchResults(List<Repository> results) {

        data.clear();
        data.addAll(results);
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
        updateNoResults();

    }

    public void stopSearch() {

        progressBar.setVisibility(View.VISIBLE);
    }

    public void startSearch() {

        // clear current results
        data.clear();
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.VISIBLE);

        stopSearch();
    }

    protected void updateNoResults() {

        // toggle no results text based
        if (data.size() > 0) {
            noResultsView.setVisibility(View.GONE);
        }
        else {
            noResultsView.setVisibility(View.VISIBLE);
        }

    }

    protected void showDetail(Repository repository) {

        if (repository != null) {

            FragmentManager fragmentManager = getFragmentManager();

            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(DetailFragment.REPOSITORY, repository);
            detailFragment.setArguments(bundle);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.content, detailFragment, DetailFragment.FRAGMENT_TAG);
            transaction.addToBackStack(DetailFragment.FRAGMENT_TAG);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();

        }
    }

    protected SearchResultsAdapter.OnClickListener clickListener = new SearchResultsAdapter.OnClickListener() {
        @Override
        public void onClick(ResultViewHolder viewHolder) {
                showDetail(viewHolder.getRepository());
        }
    };
}