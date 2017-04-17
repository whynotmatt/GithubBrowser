package com.mjohnston.githubbrowser.fragment;

import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {

    public boolean interceptBackPressed() {
        return false;
    }
}
