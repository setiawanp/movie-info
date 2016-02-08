package org.themoviedb.movieinfo.data.response;

import com.google.gson.annotations.SerializedName;

import org.themoviedb.movieinfo.BuildConfig;
import org.themoviedb.movieinfo.data.model.Movie;
import org.themoviedb.movieinfo.data.model.YoutubeVideo;

import java.util.ArrayList;
import java.util.List;

public class MovieResponse {

    @SerializedName("id")
    private long mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("poster_path")
    private String mPosterUrl;

    @SerializedName("backdrop_path")
    private String mBackdropUrl;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("release_date")
    private String mReleaseDate;

    @SerializedName("vote_count")
    private long mVoteCount;

    @SerializedName("vote_average")
    private double mVoteAverage;

    @SerializedName("runtime")
    private int mRuntime;

    @SerializedName("tagline")
    private String mTagline;

    @SerializedName("genres")
    private List<PairIdNameResponse> mGenres;

    @SerializedName("videos")
    private VideoResponse mVideos;

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterUrl() {
        return BuildConfig.BASE_URL_IMAGE + mPosterUrl;
    }

    public String getBackdropUrl() {
        return BuildConfig.BASE_URL_IMAGE + mBackdropUrl;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public long getVoteCount() {
        return mVoteCount;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public int getRuntime() {
        return mRuntime;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setPosterUrl(String posterUrl) {
        mPosterUrl = posterUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        mBackdropUrl = backdropUrl;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public void setVoteCount(long voteCount) {
        mVoteCount = voteCount;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public void setRuntime(int runtime) {
        mRuntime = runtime;
    }

    public void setTagline(String tagline) {
        mTagline = tagline;
    }

    public void setGenres(List<PairIdNameResponse> genres) {
        mGenres = genres;
    }

    public void setVideos(VideoResponse videos) {
        mVideos = videos;
    }

    public Movie toMovie() {
        return Movie.newBuilder(getId())
                .setTitle(getTitle())
                .setPosterUrl(getPosterUrl())
                .setBackdropUrl(getBackdropUrl())
                .setOverview(getOverview())
                .setReleaseDate(getReleaseDate())
                .setVoteCount(getVoteCount())
                .setVoteAverage(getVoteAverage())
                .setRuntime(getRuntime())
                .setTagline(getTagline())
                .setGenres(getGenres())
                .setTrailers(getTrailersFromYoutube())
                .build();
    }

    public List<String> getGenres() {
        List<String> genres = new ArrayList<>();
        if (mGenres == null) return genres;

        for (PairIdNameResponse genreResponse : mGenres) {
            genres.add(genreResponse.mName);
        }
        return genres;
    }

    public List<YoutubeVideo> getTrailersFromYoutube() {
        List<YoutubeVideo> videos = new ArrayList<>();
        if (mVideos == null || mVideos.mResults == null) return videos;

        for (VideoResponse.ResultResponse videoResponse : mVideos.mResults) {
            if (videoResponse.isYoutubeTrailer()) {
                videos.add(new YoutubeVideo(videoResponse.mKey));
            }
        }
        return videos;
    }

    public static class PairIdNameResponse {

        @SerializedName("id")
        private int mId;

        @SerializedName("name")
        private String mName;

        public PairIdNameResponse(int id, String name) {
            mId = id;
            mName = name;
        }

        public int getId() {
            return mId;
        }

        public String getName() {
            return mName;
        }
    }

    public static class VideoResponse {

        @SerializedName("results")
        private List<ResultResponse> mResults;

        public VideoResponse(List<ResultResponse> results) {
            mResults = results;
        }

        public List<ResultResponse> getResults() {
            return mResults;
        }

        public static class ResultResponse {

            private static final String VIDEO_SITE_YOUTUBE = "Youtube";
            private static final String VIDEO_TYPE_TRAILER = "Trailer";

            @SerializedName("key")
            private String mKey;

            @SerializedName("site")
            private String mSite;

            @SerializedName("type")
            private String mTrailer;

            public ResultResponse(String key, String site, String trailer) {
                mKey = key;
                mSite = site;
                mTrailer = trailer;
            }

            public String getKey() {
                return mKey;
            }

            public String getSite() {
                return mSite;
            }

            public String getTrailer() {
                return mTrailer;
            }

            private boolean isYoutubeTrailer() {
                return VIDEO_SITE_YOUTUBE.equalsIgnoreCase(mSite) &&
                        VIDEO_TYPE_TRAILER.equalsIgnoreCase(mTrailer);
            }
        }
    }
}
