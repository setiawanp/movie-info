package org.themoviedb.movieinfo.testcase;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;
import org.themoviedb.movieinfo.R;
import org.themoviedb.movieinfo.TestApplication;
import org.themoviedb.movieinfo.data.MockMovieResponses;
import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;
import org.themoviedb.movieinfo.domain.service.IMovieService;
import org.themoviedb.movieinfo.ui.activity.MovieDetailActivity;
import org.themoviedb.movieinfo.ui.fragment.MovieListFragment;
import org.themoviedb.movieinfo.util.RobolectricUtil;

import rx.Observable;

import java.util.Arrays;

@RunWith(RobolectricGradleTestRunner.class)
public class MovieListFragmentTest extends BaseTest {

    private MovieListFragment mFragment;

    private View mRootView;

    private PaginatedList<Movie> mPaginatedMovies;

    @Before
    public void setUp() {
        mPaginatedMovies = new PaginatedList<>(Arrays.asList(
                MockMovieResponses.MOVIE1.toMovie(), MockMovieResponses.MOVIE2.toMovie()), 2, 1);

        IMovieService movieService = ((TestApplication) RuntimeEnvironment.application)
                .getApplicationComponent().providesMovieService();
        Mockito.when(movieService.getNowPlayingMovies(Mockito.anyInt()))
                .thenReturn(Observable.just(mPaginatedMovies));
        Mockito.when(movieService.getFavouriteMovies(Mockito.anyInt()))
                .thenReturn(Observable.just(mPaginatedMovies));

        mFragment = MovieListFragment.newInstance(3, MovieListFragment.TYPE_NOW_PLAYING);
        SupportFragmentTestUtil.startVisibleFragment(mFragment);
        mRootView = mFragment.getView();
        Assert.assertNotNull(mRootView);
    }

    @Test
    public void testClickThumbnail() {
        RobolectricUtil.forceScheduler();

        RecyclerView rv = (RecyclerView) mRootView.findViewById(R.id.recycler_view);
        rv.measure(0, 0);
        rv.layout(0, 0, 1000, 1000);

        Assert.assertEquals(mPaginatedMovies.getData().size(), rv.getChildCount());
        View child = rv.getChildAt(0);
        child.performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(mFragment.getActivity());
        Intent nextIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(nextIntent);

        Movie extraMovie = (Movie) shadowIntent.getParcelableExtra("movie");
        Assert.assertEquals(extraMovie, MockMovieResponses.MOVIE1.toMovie());
        Assert.assertEquals(shadowIntent.getComponent().getClassName(),
                MovieDetailActivity.class.getName());
    }
}
