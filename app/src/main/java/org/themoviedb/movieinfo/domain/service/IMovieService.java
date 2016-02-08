package org.themoviedb.movieinfo.domain.service;

import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;

import rx.Observable;

public interface IMovieService {

    Observable<Movie> getMovieDetail(long movieId);

    Observable<PaginatedList<Movie>> getSimilarMovies(long movieId);

    Observable<PaginatedList<Movie>> getNowPlayingMovies();

    Observable<PaginatedList<Movie>> getNowPlayingMovies(int page);

    Observable<PaginatedList<Movie>> getFavouriteMovies();

    Observable<PaginatedList<Movie>> getFavouriteMovies(int page);

    Observable<Boolean> favouriteMovie(Movie movie);

    Observable<Boolean> unfavouriteMovie(Movie movie);

    boolean isFavourite(Movie movie);
}
