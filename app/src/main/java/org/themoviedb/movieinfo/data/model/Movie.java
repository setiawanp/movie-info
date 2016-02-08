package org.themoviedb.movieinfo.data.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Movie implements Parcelable {

    private long mId;
    private String mTitle;
    private String mPosterUrl;
    private String mBackdropUrl;
    private String mOverview;
    private String mReleaseDate;
    private long mVoteCount;
    private double mVoteAverage;
    private int mRuntime;
    private String mTagline;
    private List<String> mGenres;
    private List<YoutubeVideo> mTrailers;

    public Movie(Builder builder) {
        mId = builder.mId;
        mTitle = builder.mTitle;
        mPosterUrl = builder.mPosterUrl;
        mBackdropUrl = builder.mBackdropUrl;
        mOverview = builder.mOverview;
        mReleaseDate = builder.mReleaseDate;
        mVoteCount = builder.mVoteCount;
        mVoteAverage = builder.mVoteAverage;
        mRuntime = builder.mRuntime;
        mTagline = builder.mTagline;
        mGenres = builder.mGenres;
        mTrailers = builder.mTrailers;
    }

    protected Movie(Parcel in) {
        this.mId = in.readLong();
        this.mTitle = in.readString();
        this.mPosterUrl = in.readString();
        this.mBackdropUrl = in.readString();
        this.mOverview = in.readString();
        this.mReleaseDate = in.readString();
        this.mVoteCount = in.readLong();
        this.mVoteAverage = in.readDouble();
        this.mRuntime = in.readInt();
        this.mTagline = in.readString();
        this.mGenres = in.createStringArrayList();
        this.mTrailers = new ArrayList<>();
        in.readTypedList(this.mTrailers, YoutubeVideo.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mPosterUrl);
        dest.writeString(this.mBackdropUrl);
        dest.writeString(this.mOverview);
        dest.writeString(this.mReleaseDate);
        dest.writeLong(this.mVoteCount);
        dest.writeDouble(this.mVoteAverage);
        dest.writeInt(this.mRuntime);
        dest.writeString(this.mTagline);
        dest.writeStringList(this.mGenres);
        dest.writeTypedList(this.mTrailers);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (mId != movie.mId) return false;
        if (mVoteCount != movie.mVoteCount) return false;
        if (Double.compare(movie.mVoteAverage, mVoteAverage) != 0) return false;
        if (mRuntime != movie.mRuntime) return false;
        if (mTitle != null ? !mTitle.equals(movie.mTitle) : movie.mTitle != null) return false;
        if (mPosterUrl != null ? !mPosterUrl.equals(movie.mPosterUrl) : movie.mPosterUrl != null) {
            return false;
        }
        if (mBackdropUrl != null ? !mBackdropUrl.equals(movie.mBackdropUrl) :
                movie.mBackdropUrl != null) { return false; }
        if (mOverview != null ? !mOverview.equals(movie.mOverview) : movie.mOverview != null) {
            return false;
        }
        if (mReleaseDate != null ? !mReleaseDate.equals(movie.mReleaseDate) :
                movie.mReleaseDate != null) { return false; }
        if (mTagline != null ? !mTagline.equals(movie.mTagline) : movie.mTagline != null)
            return false;
        if (mGenres != null ? !mGenres.equals(movie.mGenres) : movie.mGenres != null) return false;
        return !(mTrailers != null ? !mTrailers.equals(movie.mTrailers) : movie.mTrailers != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (mId ^ (mId >>> 32));
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mPosterUrl != null ? mPosterUrl.hashCode() : 0);
        result = 31 * result + (mBackdropUrl != null ? mBackdropUrl.hashCode() : 0);
        result = 31 * result + (mOverview != null ? mOverview.hashCode() : 0);
        result = 31 * result + (mReleaseDate != null ? mReleaseDate.hashCode() : 0);
        result = 31 * result + (int) (mVoteCount ^ (mVoteCount >>> 32));
        temp = Double.doubleToLongBits(mVoteAverage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + mRuntime;
        result = 31 * result + (mTagline != null ? mTagline.hashCode() : 0);
        result = 31 * result + (mGenres != null ? mGenres.hashCode() : 0);
        result = 31 * result + (mTrailers != null ? mTrailers.hashCode() : 0);
        return result;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getPosterUrl() {
        return mPosterUrl;
    }

    public String getBackdropUrl() {
        return mBackdropUrl;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    @SuppressLint("SimpleDateFormat")
    public String getFormattedReleasedDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(mReleaseDate);
            return new SimpleDateFormat("dd MMMM yyyy").format(date);
        } catch (ParseException e) {
            return mReleaseDate;
        }
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

    public List<String> getGenres() {
        return new ArrayList<>(mGenres);
    }

    public List<YoutubeVideo> getTrailers() {
        return new ArrayList<>(mTrailers);
    }

    public static Builder newBuilder(long id) {
        return new Builder(id);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {return new Movie(source);}

        public Movie[] newArray(int size) {return new Movie[size];}
    };

    public static class Builder {

        private long mId;
        private String mTitle;
        private String mPosterUrl;
        private String mBackdropUrl;
        private String mOverview;
        private String mReleaseDate;
        private long mVoteCount;
        private double mVoteAverage;
        private int mRuntime;
        private String mTagline;
        private List<String> mGenres;
        private List<YoutubeVideo> mTrailers;

        public Builder(long mId) {
            this.mId = mId;
            this.mGenres = new ArrayList<>();
            this.mTrailers = new ArrayList<>();
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setPosterUrl(String posterUrl) {
            mPosterUrl = posterUrl;
            return this;
        }

        public Builder setBackdropUrl(String backdropUrl) {
            mBackdropUrl = backdropUrl;
            return this;
        }

        public Builder setOverview(String overview) {
            mOverview = overview;
            return this;
        }

        public Builder setReleaseDate(String releaseDate) {
            mReleaseDate = releaseDate;
            return this;
        }

        public Builder setVoteCount(long voteCount) {
            mVoteCount = voteCount;
            return this;
        }

        public Builder setVoteAverage(double voteAverage) {
            mVoteAverage = voteAverage;
            return this;
        }

        public Builder setRuntime(int runtime) {
            mRuntime = runtime;
            return this;
        }

        public Builder setTagline(String tagline) {
            mTagline = tagline;
            return this;
        }

        public Builder setGenres(List<String> genres) {
            mGenres.clear();
            if (genres != null) mGenres.addAll(genres);
            return this;
        }

        public Builder setTrailers(List<YoutubeVideo> trailers) {
            mTrailers.clear();
            if (trailers != null) mTrailers.addAll(trailers);
            return this;
        }

        public Movie build() {
            return new Movie(this);
        }
    }
}
