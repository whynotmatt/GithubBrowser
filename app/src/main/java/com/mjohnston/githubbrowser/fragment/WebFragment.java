package com.mjohnston.githubbrowser.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mjohnston.githubbrowser.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mattjohnston on 4/15/17.
 */

public class WebFragment extends BaseFragment {

    public static final String URL = "url";

    public static final String FRAGMENT_TAG = "web";

    @BindView(R.id.webView)
    WebView webView;


    protected String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(URL)) {
            url = getArguments().getString(URL);
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_web, container, false);
        ButterKnife.bind(this, rootView);

        if (webView != null && url != null) {

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new InternalWebViewClient());
            webView.loadUrl(url);
        }

        return rootView;

    }

    @OnClick(R.id.closeBtn)
    protected void clickedClose() {

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
    }

    public boolean interceptBackPressed() {

        // see if the back button should navigate the history of the webview
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        else {
            return false;
        }
    }

    // custom web view client to handle redirects inside the web view
    protected class InternalWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            shouldOverrideUrlLoading(view, url);
            return false;
        }
    }
}
