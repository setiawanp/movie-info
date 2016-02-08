package org.themoviedb.movieinfo.ui.fragment;

import android.support.v4.app.Fragment;

import org.themoviedb.movieinfo.MovieApplication;
import org.themoviedb.movieinfo.di.component.ApplicationComponent;

public class BaseFragment extends Fragment {

    public final ApplicationComponent getApplicationComponent() {
        return ((MovieApplication) getActivity().getApplication()).getApplicationComponent();
    }
}
