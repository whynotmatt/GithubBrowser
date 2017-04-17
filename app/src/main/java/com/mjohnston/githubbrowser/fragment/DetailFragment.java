package com.mjohnston.githubbrowser.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mjohnston.githubbrowser.R;
import com.mjohnston.githubbrowser.model.Repository;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mattjohnston on 4/15/17.
 */

public class DetailFragment extends BaseFragment {

    protected Repository repository;

    public static final String REPOSITORY = "repo";

    public static final String FRAGMENT_TAG = "detail";

    @BindView(R.id.repoName)
    TextView repoName;

    @BindView(R.id.repoDescription)
    TextView description;

    @BindView(R.id.repoSize)
    TextView size;

    @BindView(R.id.repoForks)
    TextView forks;

    @BindView(R.id.repoIssues)
    TextView issues;

    @BindView(R.id.repoLink)
    TextView link;

    @BindView(R.id.repoStars)
    TextView stars;

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.repoUpdated)
    TextView updated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(REPOSITORY) && getArguments().getSerializable(REPOSITORY) != null) {
            repository = (Repository) getArguments().getSerializable(REPOSITORY);
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (repository != null) {
            bindViewData();
        }

        return rootView;

    }

    protected void bindViewData() {

        if (repository != null) {
            repoName.setText(createRepoTitle(repository));

            link.setText(repository.getLink());

            description.setText(repository.getDescription());
            size.setText(repository.getFormattedSize());

            if (repository.getFormattedDate() != null) {
                updated.setText(repository.getFormattedDate());
            }

            NumberFormat format = NumberFormat.getInstance();

            if (repository.getNumberForks() != null) {
                forks.setText(format.format(repository.getNumberForks()));
            }
            if (repository.getNumberIssues() != null) {
                issues.setText(format.format(repository.getNumberIssues()));
            }
            if (repository.getStars() != null) {
                stars.setText(format.format(repository.getStars()));
            }

            if (repository.getOwnerAvatarUrl() != null) {
                // async load avatar url
                ImageLoader.getInstance().displayImage(repository.getOwnerAvatarUrl(), avatar);
            }
        }
    }

    // open repo homepage in external browser
    @OnClick(R.id.externalLink)
    protected void clickedViewExternal() {

        if (repository.getLink() == null) {
            Toast.makeText(getContext(), getString(R.string.repo_url_missing), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repository.getLink()));
        startActivity(intent);

    }

    // open repo homepage in internal web view
    @OnClick(R.id.internalLink)
    protected void clickedViewInternal() {

        if (repository.getLink() == null) {
            Toast.makeText(getContext(), getString(R.string.repo_url_missing), Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(WebFragment.URL, repository.getLink());
        fragment.setArguments(bundle);

        transaction.add(R.id.content, fragment, WebFragment.FRAGMENT_TAG);
        transaction.addToBackStack(WebFragment.FRAGMENT_TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

    }

    public Repository getRepository() {
        return repository;
    }

    protected SpannableStringBuilder createRepoTitle(Repository repository) {

        SpannableStringBuilder ss = new SpannableStringBuilder();

        ss.append(repository.getName());
        ss.append(" ");

        // make owner name a slightly smaller size
        SpannableString owner = new SpannableString("(" + repository.getOwnerName() + ")");
        owner.setSpan(new RelativeSizeSpan(0.8f), 0, owner.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.append(owner);

        return ss;

    }
}
