package org.themoviedb.movieinfo.domain.api;

import org.themoviedb.movieinfo.data.model.PaginatedList;
import org.themoviedb.movieinfo.data.response.MovieResponse;

import rx.Observable;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("movie/now_playing")
    Observable<PaginatedList<MovieResponse>> getNowPlayingMovies(@Query("page") int page);

    @GET("movie/{movieId}?append_to_response=videos")
    Observable<MovieResponse> getMovieDetail(@Path("movieId") long movieId);

    @GET("movie/{movieId}/similar")
    Observable<PaginatedList<MovieResponse>> getSimilarMovies(@Path("movieId") long movieId);
}
