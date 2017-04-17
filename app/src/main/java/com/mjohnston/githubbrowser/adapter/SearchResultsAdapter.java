package com.mjohnston.githubbrowser.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjohnston.githubbrowser.R;
import com.mjohnston.githubbrowser.model.Repository;
import com.mjohnston.githubbrowser.view.ResultViewHolder;

import java.util.List;

/**
 * Created by mattjohnston on 4/14/17.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<ResultViewHolder> {

    protected List<Repository> data;

    public static final int MAX_RESULTS = 20;

    protected OnClickListener clickListener;

    public SearchResultsAdapter(List<Repository> data, OnClickListener clickListener ) {
        this.data = data;
        this.clickListener = clickListener;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_result, parent, false);

        final ResultViewHolder viewHolder = new ResultViewHolder(inflatedView);

        if (clickListener != null) {
            inflatedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(viewHolder);
                }
            });
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {

        Repository repository = data.get(position);
        holder.bind(repository);


    }

    @Override
    public int getItemCount() {

        return data.size() > MAX_RESULTS ? MAX_RESULTS : data.size();

    }

    public interface OnClickListener {

        void onClick(ResultViewHolder viewHolder);
    }
}
