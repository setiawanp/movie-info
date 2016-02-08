package org.themoviedb.movieinfo.domain.service;

import android.database.sqlite.SQLiteDoneException;
import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.themoviedb.movieinfo.Constants;
import org.themoviedb.movieinfo.data.db.MovieTable;
import org.themoviedb.movieinfo.data.db.MovieTable_Table;
import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;
import org.themoviedb.movieinfo.data.response.MovieResponse;
import org.themoviedb.movieinfo.domain.api.MovieApi;

import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

public class MovieService implements IMovieService {

    private static final int DEFAULT_LIMIT = 20;

    private final MovieApi mMovieApi;

    public MovieService(MovieApi movieApi) {
        mMovieApi = movieApi;
    }

    @Override
    public Observable<Movie> getMovieDetail(long movieId) {
        return mMovieApi.getMovieDetail(movieId)
                .map(new Func1<MovieResponse, Movie>() {
                    @Override
                    public Movie call(MovieResponse movieResponse) {
                        return movieResponse.toMovie();
                    }
                });
    }

    @Override
    public Observable<PaginatedList<Movie>> getSimilarMovies(long movieId) {
        return mMovieApi.getSimilarMovies(movieId)
                .map(new Func1<PaginatedList<MovieResponse>, PaginatedList<Movie>>() {
                    @Override
                    public PaginatedList<Movie> call(
                            PaginatedList<MovieResponse> movieResponses) {
                        return transformToMoviePaginatedList(movieResponses);
                    }
                });
    }

    @Override
    public Observable<PaginatedList<Movie>> getNowPlayingMovies() {
        return getNowPlayingMovies(Constants.FIRST_PAGE);
    }

    @Override
    public Observable<PaginatedList<Movie>> getNowPlayingMovies(int page) {
        return mMovieApi.getNowPlayingMovies(page)
                .map(new Func1<PaginatedList<MovieResponse>, PaginatedList<Movie>>() {
                    @Override
                    public PaginatedList<Movie> call(
                            PaginatedList<MovieResponse> movieResponses) {
                        return transformToMoviePaginatedList(movieResponses);
                    }
                });
    }

    @Override
    public Observable<PaginatedList<Movie>> getFavouriteMovies() {
        return getFavouriteMovies(Constants.FIRST_PAGE);
    }

    @Override
    public Observable<PaginatedList<Movie>> getFavouriteMovies(int page) {
        long count;
        try {
            count = SQLite.select().from(MovieTable.class).count();
        } catch (SQLiteDoneException e) {
            count = 0;
        }

        int totalPage = (int) (count / DEFAULT_LIMIT) + (count % DEFAULT_LIMIT == 0? 0 : 1);

        List<MovieTable> rows = SQLite.select()
                .from(MovieTable.class)
                .limit(DEFAULT_LIMIT)
                .offset((page - 1) * DEFAULT_LIMIT)
                .queryList();

        List<Movie> movies = new ArrayList<>();
        for (MovieTable row : rows) {
            movies.add(row.toMovie());
        }

        return Observable.just(new PaginatedList<>(movies, page, totalPage));
    }

    @Override
    public Observable<Boolean> favouriteMovie(Movie movie) {
        if (isFavourite(movie)) return Observable.just(false);
        else {
            MovieTable.save(movie);
            return Observable.just(true);
        }
    }

    @Override
    public Observable<Boolean> unfavouriteMovie(Movie movie) {
        if (isFavourite(movie)) {
            SQLite.delete(MovieTable.class)
                    .where(MovieTable_Table.id.is(movie.getId()))
                    .query();
            return Observable.just(!isFavourite(movie));
        }
        return Observable.just(false);
    }

    @Override
    public boolean isFavourite(Movie movie) {
        MovieTable row = SQLite.select()
                                .from(MovieTable.class)
                                .where(MovieTable_Table.id.is(movie.getId()))
                                .querySingle();
        return row != null;
    }

    @NonNull
    private PaginatedList<Movie> transformToMoviePaginatedList(
            PaginatedList<MovieResponse> movieResponses) {List<Movie> movies = new ArrayList<>();
        for (MovieResponse response : movieResponses.getData()) {
            movies.add(response.toMovie());
        }

        return new PaginatedList<>(movies,
                movieResponses.getCurrentPage(), movieResponses.getTotalPages());
    }
}
