package org.themoviedb.movieinfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.themoviedb.movieinfo.R;
import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;
import org.themoviedb.movieinfo.data.model.YoutubeVideo;
import org.themoviedb.movieinfo.domain.service.IMovieService;
import org.themoviedb.movieinfo.ui.adapter.MovieAdapter;
import org.themoviedb.movieinfo.ui.adapter.SimilarMovieAdapter;
import org.themoviedb.movieinfo.ui.adapter.TrailerAdapter;
import org.themoviedb.movieinfo.ui.widget.ProportionalImageView;
import org.themoviedb.movieinfo.ui.widget.SpaceItemDecoration;
import org.themoviedb.movieinfo.util.RxUtils;
import org.themoviedb.movieinfo.util.ViewUtils;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MovieDetailActivity extends BaseActivity
        implements MovieAdapter.OnClickListener,
                   TrailerAdapter.OnClickListener,
                   SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_MOVIE = "movie";

    @Inject
    IMovieService mMovieService;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.appbar_layout)
    AppBarLayout appBarLayout;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.backdrop_image)
    ProportionalImageView backdropImage;

    @Bind(R.id.poster_image)
    ProportionalImageView posterImage;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.title_container)
    View titleContainer;

    @Bind(R.id.title_text)
    TextView titleText;

    @Bind(R.id.tagline_text)
    TextView taglineText;

    @Bind(R.id.overview_text)
    TextView overviewText;

    @Bind(R.id.rating_text)
    TextView ratingText;

    @Bind(R.id.released_text)
    TextView releasedText;

    @Bind(R.id.runtime_text)
    TextView runtimeText;

    @Bind(R.id.genres_text)
    TextView genresText;

    @Bind(R.id.trailers_label)
    TextView trailersLabel;

    @Bind(R.id.trailer_list)
    RecyclerView trailerList;

    @Bind(R.id.similar_label)
    TextView similarLabel;

    @Bind(R.id.similar_list)
    RecyclerView similarList;

    private Subscription mSubscription;
    private Movie mMovie;
    private boolean mIsFavourite;

    private TrailerAdapter mTrailerAdapter;
    private MovieAdapter mSimilarAdapter;

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        initViews();
        bindViews(mMovie);
        updateFab();
        getMovieDetail(mMovie.getId());
    }

    @Override
    protected void onDestroy() {
        RxUtils.unsubscribeAll(mSubscription);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onThumbnailClicked(Movie movie) {
        startActivity(MovieDetailActivity.newIntent(this, movie));
    }

    @Override
    public void onVideoClicked(YoutubeVideo video) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(video.getVideoUrl())));
    }

    @Override
    public void onRefresh() {
        getMovieDetail(mMovie.getId());
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        if (mIsFavourite) {
            mMovieService.unfavouriteMovie(mMovie);
            showMessage(getString(R.string.message_unfavourite_movie_success));
        }
        else {
            mMovieService.favouriteMovie(mMovie);
            showMessage(getString(R.string.message_favourite_movie_success));
        }
        updateFab();
    }

    private void getMovieDetail(long movieId) {
        if (RxUtils.isSubscriptionActive(mSubscription)) return;

        ViewUtils.setSwipeRefreshing(swipeRefreshLayout, true);
        mSubscription = Observable.zip(mMovieService.getMovieDetail(movieId),
                mMovieService.getSimilarMovies(movieId),
                new Func2<Movie, PaginatedList<Movie>, Pair<Movie, PaginatedList<Movie>>>() {
                    @Override
                    public Pair<Movie, PaginatedList<Movie>> call(Movie movie,
                                                                  PaginatedList<Movie> moviePaginatedList) {
                        return new Pair<>(movie, moviePaginatedList);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Pair<Movie, PaginatedList<Movie>>>() {
                    @Override
                    public void onCompleted() {
                        ViewUtils.setSwipeRefreshing(swipeRefreshLayout, false);
                        swipeRefreshLayout.setEnabled(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showMessage(e.getMessage());
                        ViewUtils.setSwipeRefreshing(swipeRefreshLayout, false);
                    }

                    @Override
                    public void onNext(Pair<Movie, PaginatedList<Movie>> pair) {
                        mMovie = pair.first;
                        bindSimilarMovies(pair.second);
                        bindViews(mMovie);
                    }
                });
    }

    private void initViews() {
        int space = getResources().getDimensionPixelSize(R.dimen.spacing_tiny);
        mTrailerAdapter = new TrailerAdapter(this, this);
        trailerList.setNestedScrollingEnabled(false);
        trailerList.setHasFixedSize(true);
        trailerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trailerList.addItemDecoration(new SpaceItemDecoration(space));
        trailerList.setAdapter(mTrailerAdapter);

        mSimilarAdapter = new SimilarMovieAdapter(this, this);
        similarList.setNestedScrollingEnabled(false);
        similarList.setHasFixedSize(true);
        similarList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        similarList.addItemDecoration(new SpaceItemDecoration(space));
        similarList.setAdapter(mSimilarAdapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    }

    private void bindSimilarMovies(PaginatedList<Movie> similarMovies) {
        boolean hasSimilar = !similarMovies.getData().isEmpty();
        similarLabel.setVisibility(hasSimilar ? View.VISIBLE : View.GONE);
        similarList.setVisibility(hasSimilar ? View.VISIBLE : View.GONE);
        if (hasSimilar) mSimilarAdapter.addData(similarMovies, true);
    }

    private void bindViews(Movie movie) {
        toolbar.setTitle(movie.getTitle());
        collapsingToolbarLayout.setTitle(movie.getTitle());
        titleText.setText(movie.getTitle());
        if (TextUtils.isEmpty(movie.getTagline())) taglineText.setVisibility(View.GONE);
        else {
            taglineText.setVisibility(View.VISIBLE);
            taglineText.setText(movie.getTagline());
        }
        overviewText.setText(movie.getOverview());
        ratingText.setText(getString(R.string.format_rating,
                movie.getVoteAverage(), movie.getVoteCount()));
        releasedText.setText(movie.getFormattedReleasedDate());
        runtimeText.setText(getString(R.string.format_runtime, movie.getRuntime()));
        genresText.setText(TextUtils.join(", ", movie.getGenres()));

        boolean hasTrailers = !movie.getTrailers().isEmpty();
        trailersLabel.setVisibility(hasTrailers? View.VISIBLE : View.GONE);
        trailerList.setVisibility(hasTrailers ? View.VISIBLE : View.GONE);
        if (hasTrailers) mTrailerAdapter.setVideos(movie.getTrailers());

        // Set AppBarLayout height manually since the child height is dynamic and set the height
        //  to wrap_content may cut off the height if the AppBarLayout's height is greater than
        //  the screen's height
        appBarLayout.getLayoutParams().height = backdropImage.getLayoutParams().height +
                titleContainer.getLayoutParams().height;

        Picasso.with(this)
                .load(movie.getBackdropUrl())
                .placeholder(R.color.gray)
                .error(R.drawable.bg_broken_image)
                .into(backdropImage);

        Picasso.with(this)
                .load(movie.getPosterUrl())
                .placeholder(R.color.gray)
                .error(R.drawable.bg_broken_image)
                .into(posterImage);
    }

    private void updateFab() {
        mIsFavourite = mMovieService.isFavourite(mMovie);
        fab.setImageResource(mIsFavourite ?
                R.drawable.ic_favorite_white_36dp : R.drawable.ic_favorite_border_white_36dp);
    }

    private void showMessage(String text) {
        Snackbar.make(swipeRefreshLayout, text, Snackbar.LENGTH_LONG).show();
    }
}
