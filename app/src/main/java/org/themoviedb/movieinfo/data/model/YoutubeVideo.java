package org.themoviedb.movieinfo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class YoutubeVideo implements Parcelable {

    private static final String VIDEO_URL_FORMAT = "http://www.youtube.com/watch?v=%s";
    private static final String THUMBNAIL_URL_FORMAT = "http://img.youtube.com/vi/%s/0.jpg";

    private String mVideoUrl;
    private String mThumbnailUrl;

    public YoutubeVideo(String key) {
        mVideoUrl = String.format(VIDEO_URL_FORMAT, key);
        mThumbnailUrl = String.format(THUMBNAIL_URL_FORMAT, key);
    }

    protected YoutubeVideo(Parcel in) {
        this.mVideoUrl = in.readString();
        this.mThumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mVideoUrl);
        dest.writeString(this.mThumbnailUrl);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public boolean equals(Object o) {
        return o instanceof YoutubeVideo && mVideoUrl.equals(((YoutubeVideo) o).mVideoUrl);
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public static final Creator<YoutubeVideo> CREATOR = new Creator<YoutubeVideo>() {
        public YoutubeVideo createFromParcel(Parcel source) {return new YoutubeVideo(source);}

        public YoutubeVideo[] newArray(int size) {return new YoutubeVideo[size];}
    };
}
