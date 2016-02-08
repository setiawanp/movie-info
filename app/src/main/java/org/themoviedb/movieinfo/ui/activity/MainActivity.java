package org.themoviedb.movieinfo.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;

import org.themoviedb.movieinfo.R;
import org.themoviedb.movieinfo.ui.adapter.HomePagerAdapter;
import org.themoviedb.movieinfo.ui.fragment.MovieListFragment;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private Fragment[] fragments = new Fragment[] {
            MovieListFragment.newInstance(3, MovieListFragment.TYPE_NOW_PLAYING),
            MovieListFragment.newInstance(3, MovieListFragment.TYPE_FAVOURITES)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        initComponents();
    }

    private void initComponents() {
        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(), fragments,
                getResources().getStringArray(R.array.home_pager_title)));
        tabLayout.setupWithViewPager(viewPager);
    }
}
