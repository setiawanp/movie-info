package org.themoviedb.movieinfo.util;

import junit.framework.Assert;

import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.PaginatedList;
import org.themoviedb.movieinfo.data.response.MovieResponse;

import java.util.List;

public final class AssertMovie {

    public static void assertEquals(MovieResponse expectedResponse, Movie actualMovie) {
        Assert.assertEquals(expectedResponse.getId(), actualMovie.getId());
        Assert.assertEquals(expectedResponse.getTitle(), actualMovie.getTitle());
        Assert.assertEquals(expectedResponse.getPosterUrl(), actualMovie.getPosterUrl());
        Assert.assertEquals(expectedResponse.getBackdropUrl(), actualMovie.getBackdropUrl());
        Assert.assertEquals(expectedResponse.getOverview(), actualMovie.getOverview());
        Assert.assertEquals(expectedResponse.getReleaseDate(), actualMovie.getReleaseDate());
        Assert.assertEquals(expectedResponse.getVoteCount(), actualMovie.getVoteCount());
        Assert.assertEquals(expectedResponse.getVoteAverage(), actualMovie.getVoteAverage());
        Assert.assertEquals(expectedResponse.getRuntime(), actualMovie.getRuntime());
        Assert.assertEquals(expectedResponse.getTagline(), actualMovie.getTagline());
        Assert.assertEquals(expectedResponse.getGenres(), actualMovie.getGenres());
        Assert.assertEquals(expectedResponse.getTrailersFromYoutube(), actualMovie.getTrailers());
    }

    public static void assertEquals(PaginatedList<MovieResponse> expectedPaginatedResponses,
                                    PaginatedList<Movie> actualPaginatedMovies) {
        Assert.assertEquals(expectedPaginatedResponses.getData().size(), actualPaginatedMovies.getData().size());
        Assert.assertEquals(expectedPaginatedResponses.getCurrentPage(), actualPaginatedMovies.getCurrentPage());
        Assert.assertEquals(expectedPaginatedResponses.getTotalPages(), actualPaginatedMovies.getTotalPages());
        List<Movie> actualMovies = actualPaginatedMovies.getData();
        List<MovieResponse> expectedResponses = expectedPaginatedResponses.getData();
        for (int i = 0, size = actualPaginatedMovies.getData().size(); i < size; i++) {
            assertEquals(expectedResponses.get(i), actualMovies.get(i));
        }
    }
}
