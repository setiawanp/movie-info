package org.themoviedb.movieinfo.testcase;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.themoviedb.movieinfo.Constants;
import org.themoviedb.movieinfo.data.MockMovieResponses;
import org.themoviedb.movieinfo.data.db.AppDatabase;
import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;
import org.themoviedb.movieinfo.data.response.MovieResponse;
import org.themoviedb.movieinfo.domain.api.MovieApi;
import org.themoviedb.movieinfo.domain.service.MovieService;
import org.themoviedb.movieinfo.util.AssertMovie;

import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.Arrays;
import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
public class MovieServiceTest extends BaseTest {

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Mock
    MovieApi mMovieApi;

    @InjectMocks
    MovieService mMovieService;

    @Before
    public void setUp() {
        FlowManager.init(new FlowConfig.Builder(RuntimeEnvironment.application).build());
    }

    @After
    public void tearDown() {
        FlowManager.getDatabase(AppDatabase.NAME).reset(RuntimeEnvironment.application);
        FlowManager.destroy();
    }

    @Test
    public void testGetMovieDetail() {
        MovieResponse expectedResponse = MockMovieResponses.MOVIE1;
        Mockito.when(mMovieApi.getMovieDetail(Mockito.anyLong())).thenReturn(Observable.just(expectedResponse));

        TestSubscriber<Movie> testSubscriber = new TestSubscriber<>();
        mMovieService.getMovieDetail(1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        List<Movie> actualItems = testSubscriber.getOnNextEvents();
        Assert.assertEquals(1, actualItems.size());
        AssertMovie.assertEquals(expectedResponse, actualItems.get(0));
    }

    @Test
    public void testGetSimilarMovies() {
        PaginatedList<MovieResponse> paginatedMovies = new PaginatedList<>(
                Arrays.asList(MockMovieResponses.MOVIE1, MockMovieResponses.MOVIE2), 1, 2);
        Mockito.when(mMovieApi.getSimilarMovies(Mockito.anyLong())).thenReturn(Observable.just(paginatedMovies));

        TestSubscriber<PaginatedList<Movie>> testSubscriber = new TestSubscriber<>();
        mMovieService.getSimilarMovies(1).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        List<PaginatedList<Movie>> actualItems = testSubscriber.getOnNextEvents();
        Assert.assertEquals(1, actualItems.size());
        AssertMovie.assertEquals(paginatedMovies, actualItems.get(0));
    }

    @Test
    public void testGetNowPlayingMoviesFirstPage() {
        PaginatedList<MovieResponse> paginatedMoviesFirstPage = new PaginatedList<>(
                Arrays.asList(MockMovieResponses.MOVIE1, MockMovieResponses.MOVIE2), 1, 2);
        PaginatedList<MovieResponse> paginatedMoviesOthers = new PaginatedList<>(
                Arrays.asList(MockMovieResponses.MOVIE3, MockMovieResponses.MOVIE2), 2, 4);
        Mockito.when(mMovieApi.getNowPlayingMovies(Matchers.eq(Constants.FIRST_PAGE)))
                .thenReturn(Observable.just(paginatedMoviesFirstPage));
        Mockito.when(mMovieApi.getNowPlayingMovies(AdditionalMatchers.not(Matchers.eq(
                Constants.FIRST_PAGE))))
                .thenReturn(Observable.just(paginatedMoviesOthers));

        TestSubscriber<PaginatedList<Movie>> testSubscriber = new TestSubscriber<>();
        mMovieService.getNowPlayingMovies().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        List<PaginatedList<Movie>> actualItems = testSubscriber.getOnNextEvents();
        Assert.assertEquals(1, actualItems.size());
        AssertMovie.assertEquals(paginatedMoviesFirstPage, actualItems.get(0));
    }

    @Test
    public void testGetNowPlayingMoviesOtherPage() {
        PaginatedList<MovieResponse> paginatedMoviesFirstPage = new PaginatedList<>(
                Arrays.asList(MockMovieResponses.MOVIE1, MockMovieResponses.MOVIE2), 1, 2);
        PaginatedList<MovieResponse> paginatedMoviesOthers = new PaginatedList<>(
                Arrays.asList(MockMovieResponses.MOVIE3, MockMovieResponses.MOVIE2), 2, 4);
        Mockito.when(mMovieApi.getNowPlayingMovies(Matchers.eq(Constants.FIRST_PAGE)))
                .thenReturn(Observable.just(paginatedMoviesFirstPage));
        Mockito.when(mMovieApi.getNowPlayingMovies(AdditionalMatchers.not(Matchers.eq(Constants.FIRST_PAGE))))
                .thenReturn(Observable.just(paginatedMoviesOthers));

        TestSubscriber<PaginatedList<Movie>> testSubscriber = new TestSubscriber<>();
        mMovieService.getNowPlayingMovies(2).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        List<PaginatedList<Movie>> actualItems = testSubscriber.getOnNextEvents();
        Assert.assertEquals(1, actualItems.size());
        AssertMovie.assertEquals(paginatedMoviesOthers, actualItems.get(0));
    }

    @Test
    public void testAddSingleFavouriteMovie() {
        testGetFavouriteMovies(0);
        testAddFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), true);
        testGetFavouriteMovies(1);
    }

    @Test
    public void testAddMultipleDifferentFavouriteMovie() {
        testGetFavouriteMovies(0);
        testAddFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), true);
        testGetFavouriteMovies(1);
        testAddFavouriteMovie(MockMovieResponses.MOVIE2.toMovie(), true);
        testGetFavouriteMovies(2);
        testAddFavouriteMovie(MockMovieResponses.MOVIE3.toMovie(), true);
        testGetFavouriteMovies(3);
    }

    @Test
    public void testAddMultipleSameFavouriteMovie() {
        testGetFavouriteMovies(0);
        testAddFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), true);
        testGetFavouriteMovies(1);
        testAddFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), false);
        testGetFavouriteMovies(1);
    }

    @Test
    public void testRemoveSingleFavouriteMovie() {
        testGetFavouriteMovies(0);
        testAddFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), true);
        testGetFavouriteMovies(1);
        testRemoveFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), true);
        testGetFavouriteMovies(0);
    }

    @Test
    public void testRemoveMultipleDifferentFavouriteMovie() {
        testGetFavouriteMovies(0);
        testAddFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), true);
        testGetFavouriteMovies(1);
        testAddFavouriteMovie(MockMovieResponses.MOVIE2.toMovie(), true);
        testGetFavouriteMovies(2);
        testRemoveFavouriteMovie(MockMovieResponses.MOVIE2.toMovie(), true);
        testGetFavouriteMovies(1);
        testRemoveFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), true);
        testGetFavouriteMovies(0);
    }

    @Test
    public void testRemoveMultipleSameFavouriteMovie() {
        testGetFavouriteMovies(0);
        testAddFavouriteMovie(MockMovieResponses.MOVIE1.toMovie(), true);
        testGetFavouriteMovies(1);
        testAddFavouriteMovie(MockMovieResponses.MOVIE2.toMovie(), true);
        testGetFavouriteMovies(2);
        testRemoveFavouriteMovie(MockMovieResponses.MOVIE2.toMovie(), true);
        testGetFavouriteMovies(1);
        testRemoveFavouriteMovie(MockMovieResponses.MOVIE2.toMovie(), false);
        testGetFavouriteMovies(1);
    }

    @Test
    public void testIsFavouriteMovie() {
        testGetFavouriteMovies(0);

        Movie movie1 = MockMovieResponses.MOVIE1.toMovie();
        Assert.assertEquals(false, mMovieService.isFavourite(movie1));
        testAddFavouriteMovie(movie1, true);
        Assert.assertEquals(true, mMovieService.isFavourite(movie1));
        testGetFavouriteMovies(1);

        testRemoveFavouriteMovie(movie1, true);
        Assert.assertEquals(false, mMovieService.isFavourite(movie1));
        testGetFavouriteMovies(0);
    }

    private void testAddFavouriteMovie(Movie movie, boolean expectedReturnValue) {
        TestSubscriber<Boolean> testSubscriberBool = new TestSubscriber<>();
        mMovieService.favouriteMovie(movie).subscribe(testSubscriberBool);
        Assert.assertEquals(1, testSubscriberBool.getOnNextEvents().size());
        Assert.assertEquals(expectedReturnValue, testSubscriberBool.getOnNextEvents().get(
                0).booleanValue());
    }

    private void testRemoveFavouriteMovie(Movie movie, boolean expectedReturnValue) {
        TestSubscriber<Boolean> testSubscriberBool = new TestSubscriber<>();
        mMovieService.unfavouriteMovie(movie).subscribe(testSubscriberBool);
        Assert.assertEquals(1, testSubscriberBool.getOnNextEvents().size());
        Assert.assertEquals(expectedReturnValue, testSubscriberBool.getOnNextEvents().get(
                0).booleanValue());
    }

    private void testGetFavouriteMovies(int expectedNumOfRows) {
        TestSubscriber<PaginatedList<Movie>> testSubscriberPaginatedPre = new TestSubscriber<>();
        mMovieService.getFavouriteMovies().subscribe(testSubscriberPaginatedPre);
        Assert.assertEquals(1, testSubscriberPaginatedPre.getOnNextEvents().size());
        PaginatedList<Movie> actualPaginated = testSubscriberPaginatedPre.getOnNextEvents().get(0);
        Assert.assertEquals(expectedNumOfRows, actualPaginated.getData().size());
    }
}
