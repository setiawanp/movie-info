package org.themoviedb.movieinfo.ui.activity;

import android.support.v7.app.AppCompatActivity;

import org.themoviedb.movieinfo.MovieApplication;
import org.themoviedb.movieinfo.di.component.ApplicationComponent;

public class BaseActivity extends AppCompatActivity {

    public final ApplicationComponent getApplicationComponent() {
        return ((MovieApplication) getApplication()).getApplicationComponent();
    }
}
