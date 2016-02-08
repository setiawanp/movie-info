package org.themoviedb.movieinfo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;

import org.themoviedb.movieinfo.Constants;
import org.themoviedb.movieinfo.R;
import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;
import org.themoviedb.movieinfo.domain.service.IMovieService;
import org.themoviedb.movieinfo.ui.activity.MovieDetailActivity;
import org.themoviedb.movieinfo.ui.adapter.MovieAdapter;
import org.themoviedb.movieinfo.ui.widget.EndlessRecyclerView;
import org.themoviedb.movieinfo.ui.widget.SpaceItemDecoration;
import org.themoviedb.movieinfo.util.RxUtils;
import org.themoviedb.movieinfo.util.ViewUtils;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MovieListFragment extends BaseFragment
        implements  SwipeRefreshLayout.OnRefreshListener,
                    EndlessRecyclerView.OnLoadMoreListener,
                    MovieAdapter.OnClickListener {

    @IntDef({TYPE_NOW_PLAYING, TYPE_FAVOURITES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MovieType {}

    public static final int TYPE_NOW_PLAYING = 1;
    public static final int TYPE_FAVOURITES = 2;

    private static final String BUNDLE_COLUMN_COUNT = "column_count";
    private static final String BUNDLE_MOVIE_TYPE = "movie_type";
    private static final String BUNDLE_ADAPTER_LIST = "adapter_list";
    private static final String BUNDLE_ADAPTER_CURRENT_PAGE = "adapter_current_page";
    private static final String BUNDLE_ADAPTER_TOTAL_PAGES = "adapter_total_page";
    private static final int DEFAULT_COLUMN_COUNT = 3;

    @Inject
    IMovieService mMovieService;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recycler_view)
    EndlessRecyclerView recyclerView;

    private int mColumnCount = DEFAULT_COLUMN_COUNT;
    private @MovieType int mMovieType;
    private MovieAdapter mMovieAdapter;
    private Subscription mSubscriptionLoad;

    public static MovieListFragment newInstance(int columnCount, @MovieType int movieType) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_COLUMN_COUNT, columnCount);
        args.putInt(BUNDLE_MOVIE_TYPE, movieType);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
        setRetainInstance(true);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(BUNDLE_COLUMN_COUNT);
            //noinspection ResourceType
            mMovieType = getArguments().getInt(BUNDLE_MOVIE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);

        Context context = view.getContext();
        mMovieAdapter = new MovieAdapter(getActivity(), this);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, mColumnCount);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (mMovieAdapter.isFooterVisible() &&
                            position == gridLayoutManager.getItemCount() - 1)? mColumnCount : 1;
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        int space = context.getResources().getDimensionPixelSize(R.dimen.spacing_tiny);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpaceItemDecoration(space));
        recyclerView.setAdapter(mMovieAdapter);
        recyclerView.setOnLoadMoreListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (savedInstanceState == null) {
            loadData(true);
        } else {
            List<Movie> list = savedInstanceState.getParcelableArrayList(BUNDLE_ADAPTER_LIST);
            int currentPage = savedInstanceState.getInt(BUNDLE_ADAPTER_CURRENT_PAGE, 0);
            int totalPages = savedInstanceState.getInt(BUNDLE_ADAPTER_TOTAL_PAGES, 0);
            mMovieAdapter.addData(new PaginatedList<>(list, currentPage, totalPages), true);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMovieType == TYPE_FAVOURITES) loadData(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PaginatedList<Movie> paginatedList = mMovieAdapter.getData();
        outState.putParcelableArrayList(BUNDLE_ADAPTER_LIST,
                new ArrayList<Parcelable>(paginatedList.getData()));
        outState.putInt(BUNDLE_ADAPTER_CURRENT_PAGE, paginatedList.getCurrentPage());
        outState.putInt(BUNDLE_ADAPTER_TOTAL_PAGES, paginatedList.getTotalPages());
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onLoadMore() {
        loadData(false);
    }

    @Override
    public void onThumbnailClicked(Movie movie) {
        startActivity(MovieDetailActivity.newIntent(getActivity(), movie));
    }

    public void loadData(final boolean refresh) {
        if (RxUtils.isSubscriptionActive(mSubscriptionLoad)) return;

        int page = refresh? Constants.FIRST_PAGE : mMovieAdapter.getData().getCurrentPage() + 1;
        showLoading(true, refresh);
        mSubscriptionLoad = createObservable(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<PaginatedList<Movie>>() {
                    @Override
                    public void onCompleted() {
                        showLoading(false, refresh);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e.getMessage(), refresh);
                        showLoading(false, refresh);
                    }

                    @Override
                    public void onNext(PaginatedList<Movie> moviePaginatedList) {
                        mMovieAdapter.addData(moviePaginatedList, refresh);
                    }
                });
    }

    private Observable<PaginatedList<Movie>> createObservable(int page) {
        switch (mMovieType) {
            case TYPE_FAVOURITES:
                return mMovieService.getFavouriteMovies(page);
            case TYPE_NOW_PLAYING:
            default:
                return mMovieService.getNowPlayingMovies(page);
        }
    }

    private void showError(final String message, final boolean refresh) {
        Snackbar.make(swipeRefreshLayout, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadData(refresh);
                    }
                })
                .show();
    }

    private void showLoading(boolean show, boolean refresh) {
        if (refresh) {
            ViewUtils.setSwipeRefreshing(swipeRefreshLayout, show);
        } else {
            mMovieAdapter.setFooterVisible(show);
        }
    }
}
