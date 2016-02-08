package org.themoviedb.movieinfo.data;

import org.themoviedb.movieinfo.data.response.MovieResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class MockMovieResponses {

    public static final MovieResponse.PairIdNameResponse GENRE_THRILLER = new MovieResponse.PairIdNameResponse(1, "Thriller");
    public static final MovieResponse.PairIdNameResponse GENRE_DRAMA = new MovieResponse.PairIdNameResponse(2, "Drama");
    public static final MovieResponse.PairIdNameResponse GENRE_ROMANCE = new MovieResponse.PairIdNameResponse(3, "Romance");

    public static final MovieResponse.VideoResponse.ResultResponse TRAILER_YOUTUBE1 = new MovieResponse.VideoResponse.ResultResponse("a", "Youtube", "Trailer");
    public static final MovieResponse.VideoResponse.ResultResponse TRAILER_YOUTUBE2 = new MovieResponse.VideoResponse.ResultResponse("b", "Youtube", "Trailer");
    public static final MovieResponse.VideoResponse.ResultResponse TRAILER_YOUTUBE3 = new MovieResponse.VideoResponse.ResultResponse("c", "Youtube", "Trailer");
    public static final MovieResponse.VideoResponse.ResultResponse TRAILER_VIMEO1 = new MovieResponse.VideoResponse.ResultResponse("d", "Vimeo", "Trailer");
    public static final MovieResponse.VideoResponse.ResultResponse NON_TRAILER_YOUTUBE1 = new MovieResponse.VideoResponse.ResultResponse("c", "Youtube", "Non-Trailer");

    public static final MovieResponse MOVIE1 = build(1, "title1", "/poster1.jpg", "/backdrop1.jpg",
            "overview1", "2016-01-01", 101, 1.1, 11, "tagline1",
            Arrays.asList(GENRE_THRILLER, GENRE_DRAMA), Arrays.asList(TRAILER_YOUTUBE1, TRAILER_YOUTUBE2));

    public static final MovieResponse MOVIE2 = build(2, "title2", "/poster2.jpg", "/backdrop2.jpg",
            "overview2", "2016-02-02", 202, 2.2, 22, "tagline2",
            Arrays.asList(GENRE_THRILLER, GENRE_ROMANCE), Arrays.asList(TRAILER_YOUTUBE2, TRAILER_VIMEO1));

    public static final MovieResponse MOVIE3 = build(3, "title3", "/poster3.jpg", "/backdrop3.jpg",
            "overview3", "2016-03-03", 303, 3.3, 33, "tagline3",
            Arrays.asList(GENRE_THRILLER, GENRE_ROMANCE), Arrays.asList(NON_TRAILER_YOUTUBE1, TRAILER_YOUTUBE3, TRAILER_VIMEO1));

    public static final MovieResponse MOVIE_WITHOUT_TRAILER = build(4, "title4", "/poster4.jpg", "/backdrop4.jpg",
            "overview4", "2016-04-04", 404, 4.4, 44, "tagline4",
            Arrays.asList(GENRE_THRILLER, GENRE_ROMANCE), new ArrayList<MovieResponse.VideoResponse.ResultResponse>());

    public static final MovieResponse MOVIE_WITHOUT_TAGLINE = build(5, "title5", "/poster5.jpg", "/backdrop5.jpg",
            "overview5", "2016-05-05", 505, 5.5, 55, "",
            Arrays.asList(GENRE_THRILLER, GENRE_ROMANCE), Arrays.asList(TRAILER_YOUTUBE2, TRAILER_VIMEO1));

    public static final MovieResponse MOVIE_WITHOUT_YOUTUBE_TRAILER = build(6, "title6", "/poster6.jpg", "/backdrop6.jpg",
            "overview6", "2016-06-06", 606, 6.6, 66, "tagline6",
            Arrays.asList(GENRE_THRILLER, GENRE_ROMANCE), Arrays.asList(TRAILER_VIMEO1));

    public static List<String> convertToGenres(List<MovieResponse.PairIdNameResponse> genreResponses) {
        List<String> genres = new ArrayList<>();
        for (MovieResponse.PairIdNameResponse response : genreResponses) {
            genres.add(response.getName());
        }
        return genres;
    }

    private static MovieResponse build(long id, String title, String posterUrl, String backdropUrl,
                                       String overview, String releaseDate, long voteCount,
                                       double voteAverage, int runtime, String tagline,
                                       List<MovieResponse.PairIdNameResponse> genres,
                                       List<MovieResponse.VideoResponse.ResultResponse> videos) {
        MovieResponse response = new MovieResponse();
        response.setId(id);
        response.setTitle(title);
        response.setPosterUrl(posterUrl);
        response.setBackdropUrl(backdropUrl);
        response.setOverview(overview);
        response.setReleaseDate(releaseDate);
        response.setVoteCount(voteCount);
        response.setVoteAverage(voteAverage);
        response.setRuntime(runtime);
        response.setTagline(tagline);
        response.setGenres(genres);
        response.setVideos(new MovieResponse.VideoResponse(videos));
        return response;
    }
}
