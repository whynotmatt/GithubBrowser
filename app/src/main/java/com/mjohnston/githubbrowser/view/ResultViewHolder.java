package com.mjohnston.githubbrowser.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mjohnston.githubbrowser.R;
import com.mjohnston.githubbrowser.model.Repository;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mattjohnston on 4/14/17.
 */

public class ResultViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.repoName)
    TextView repoName;

    @BindView(R.id.repoDescription)
    TextView description;

    @BindView(R.id.repoUpdated)
    TextView updated;

    @BindView(R.id.repoStars)
    TextView stars;

    protected Repository repository;

    public ResultViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    public void bind(Repository repository) {

        this.repository = repository;

        repoName.setText(repository.getName());
        description.setText(repository.getDescription());
        updated.setText(repository.getFormattedDate());

        if (repository.getStars() != null) {
            stars.setText(NumberFormat.getInstance().format(repository.getStars()));
        }
        else {
            stars.setText(0);
        }

    }

    public Repository getRepository() {
        return repository;
    }

}
