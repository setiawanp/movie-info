package org.themoviedb.movieinfo.testcase;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;
import org.themoviedb.movieinfo.R;
import org.themoviedb.movieinfo.TestApplication;
import org.themoviedb.movieinfo.data.MockMovieResponses;
import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;
import org.themoviedb.movieinfo.domain.service.IMovieService;
import org.themoviedb.movieinfo.ui.activity.MovieDetailActivity;
import org.themoviedb.movieinfo.util.RobolectricUtil;

import rx.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
public class MovieDetailActivityTest extends BaseTest {

    private MovieDetailActivity mActivity;

    private Movie mMovie;

    private PaginatedList<Movie> mSimilarMovies;

    private void init(Movie movie, List<Movie> similarMovies) {
        mMovie = movie;
        if (similarMovies == null) similarMovies = new ArrayList<>();
        mSimilarMovies = new PaginatedList<>(similarMovies, similarMovies.size(), 1);
        IMovieService movieService = ((TestApplication)RuntimeEnvironment.application)
                .getApplicationComponent().providesMovieService();
        Mockito.when(movieService.getMovieDetail(Mockito.anyLong()))
                .thenReturn(Observable.just(mMovie));
        Mockito.when(movieService.getSimilarMovies(Mockito.anyLong()))
                .thenReturn(Observable.just(mSimilarMovies));
        mActivity = Robolectric.buildActivity(MovieDetailActivity.class)
                .withIntent(MovieDetailActivity.newIntent(RuntimeEnvironment.application, mMovie))
                .create()
                .get();
    }

    @Test
    public void testCompleteMovieAndSimilarBoundedToView() {
        init(MockMovieResponses.MOVIE1.toMovie(),
                Arrays.asList(MockMovieResponses.MOVIE2.toMovie()));
        testMovieBoundedToView(mMovie, mSimilarMovies);
    }

    @Test
    public void testMovieWithoutSimilarBoundedToView() {
        init(MockMovieResponses.MOVIE1.toMovie(), null);
        testMovieBoundedToView(mMovie, mSimilarMovies);
    }

    @Test
    public void testMovieWithoutTrailerBoundedToView() {
        init(MockMovieResponses.MOVIE_WITHOUT_TRAILER.toMovie(),
                Arrays.asList(MockMovieResponses.MOVIE2.toMovie()));
        testMovieBoundedToView(mMovie, mSimilarMovies);
    }

    @Test
    public void testMovieWithoutTaglineBoundedToView() {
        init(MockMovieResponses.MOVIE_WITHOUT_TAGLINE.toMovie(),
                Arrays.asList(MockMovieResponses.MOVIE2.toMovie()));
        testMovieBoundedToView(mMovie, mSimilarMovies);
    }

    @Test
    public void testMovieWithoutYoutubeTrailerBoundedToView() {
        init(MockMovieResponses.MOVIE_WITHOUT_YOUTUBE_TRAILER.toMovie(),
                Arrays.asList(MockMovieResponses.MOVIE2.toMovie()));
        testMovieBoundedToView(mMovie, mSimilarMovies);
    }

    @Test
    public void testMovieWithoutTrailerAndSimilarBoundedToView() {
        init(MockMovieResponses.MOVIE_WITHOUT_TRAILER.toMovie(), null);
        testMovieBoundedToView(mMovie, mSimilarMovies);
    }

    @Test
    public void testClickSimilarMovie() {
        List<Movie> similarMovies = Arrays.asList(
                MockMovieResponses.MOVIE2.toMovie(), MockMovieResponses.MOVIE3.toMovie());
        init(MockMovieResponses.MOVIE1.toMovie(), similarMovies);
        RobolectricUtil.forceScheduler();

        RecyclerView rv = (RecyclerView) mActivity.findViewById(R.id.similar_list);
        rv.measure(0, 0);
        rv.layout(0, 0, 1000, 1000);

        Assert.assertEquals(similarMovies.size(), rv.getChildCount());
        View child = rv.getChildAt(0);
        child.performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(mActivity);
        Intent nextIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(nextIntent);

        Movie extraMovie = (Movie) shadowIntent.getParcelableExtra("movie");
        Assert.assertEquals(extraMovie, MockMovieResponses.MOVIE2.toMovie());
        Assert.assertEquals(shadowIntent.getComponent().getClassName(), MovieDetailActivity.class.getName());
    }

    @Test
    public void testClickTrailer() {
        Movie movie = MockMovieResponses.MOVIE1.toMovie();
        init(movie, Arrays.asList(MockMovieResponses.MOVIE2.toMovie(),
                MockMovieResponses.MOVIE3.toMovie()));
        RobolectricUtil.forceScheduler();

        RecyclerView rv = (RecyclerView) mActivity.findViewById(R.id.trailer_list);
        rv.measure(0, 0);
        rv.layout(0, 0, 1000, 1000);

        Assert.assertEquals(movie.getTrailers().size(), rv.getChildCount());
        View child = rv.getChildAt(0);
        child.performClick();

        ShadowActivity shadowActivity = Shadows.shadowOf(mActivity);
        Intent nextIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(nextIntent);

        Assert.assertEquals(shadowIntent.getAction(), Intent.ACTION_VIEW);
        Assert.assertEquals(shadowIntent.getData(), Uri.parse(movie.getTrailers().get(0).getVideoUrl()));
    }

    private void testMovieBoundedToView(Movie movie, PaginatedList<Movie> similarMovies) {
        RobolectricUtil.forceScheduler();

        testTextViewText(R.id.title_text, movie.getTitle());
        if (TextUtils.isEmpty(movie.getTagline())) {
            testViewVisible(R.id.tagline_text, View.GONE);
        } else {
            testTextViewText(R.id.tagline_text, movie.getTagline());
            testViewVisible(R.id.tagline_text, View.VISIBLE);
        }
        testTextViewText(R.id.overview_text, movie.getOverview());
        testTextViewText(R.id.rating_text, RuntimeEnvironment.application.getResources()
                .getString(R.string.format_rating, movie.getVoteAverage(), movie.getVoteCount()));
        testTextViewText(R.id.released_text, movie.getFormattedReleasedDate());
        testTextViewText(R.id.runtime_text, RuntimeEnvironment.application.getResources()
                .getString(R.string.format_runtime, movie.getRuntime()));
        testTextViewText(R.id.genres_text, TextUtils.join(", ", movie.getGenres()));

        boolean hasTrailers = !movie.getTrailers().isEmpty();
        testViewVisible(R.id.trailers_label, hasTrailers ? View.VISIBLE : View.GONE);
        testViewVisible(R.id.trailer_list, hasTrailers ? View.VISIBLE : View.GONE);
        testRecyclerViewItemCount(R.id.trailer_list, movie.getTrailers().size());

        boolean hasSimilar = !similarMovies.getData().isEmpty();
        testViewVisible(R.id.similar_label, hasSimilar ? View.VISIBLE : View.GONE);
        testViewVisible(R.id.similar_list, hasSimilar ? View.VISIBLE : View.GONE);
        testRecyclerViewItemCount(R.id.similar_list, similarMovies.getData().size());
    }

    private void testRecyclerViewItemCount(@IdRes int resId, int expectedItemCount) {
        RecyclerView rv = (RecyclerView) mActivity.findViewById(resId);
        Assert.assertNotNull(rv);

        rv.measure(0, 0);
        rv.layout(0, 0, 1000, 1000);
        Assert.assertEquals(expectedItemCount, rv.getChildCount());
    }

    private void testViewVisible(@IdRes int resId, int expectedVisibility) {
        View view = mActivity.findViewById(resId);
        Assert.assertNotNull(view);
        Assert.assertEquals(expectedVisibility, view.getVisibility());
    }

    private void testTextViewText(@IdRes int resId, String expectedText) {
        TextView textView = (TextView) mActivity.findViewById(resId);
        Assert.assertNotNull(textView);
        Assert.assertEquals(expectedText, textView.getText());
    }
}
