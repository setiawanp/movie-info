package org.themoviedb.movieinfo.data.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.themoviedb.movieinfo.data.model.Movie;

@Table(database = AppDatabase.class)
public class MovieTable extends BaseModel {

    @PrimaryKey
    @Column
    long id;

    @Column
    String title;

    @Column
    String posterUrl;

    @Column
    String backdropUrl;

    @Column
    String overview;

    @Column
    String releaseDate;

    @Column
    long voteCount;

    @Column
    double voteAverage;

    public static void save(Movie movie) {
        MovieTable table = new MovieTable();
        table.id = movie.getId();
        table.title = movie.getTitle();
        table.posterUrl = movie.getPosterUrl();
        table.backdropUrl = movie.getBackdropUrl();
        table.overview = movie.getOverview();
        table.releaseDate = movie.getReleaseDate();
        table.voteCount = movie.getVoteCount();
        table.voteAverage = movie.getVoteAverage();
        table.save();
    }

    public MovieTable() {
    }

    public Movie toMovie() {
        return Movie.newBuilder(id)
                .setTitle(title)
                .setPosterUrl(posterUrl)
                .setBackdropUrl(backdropUrl)
                .setOverview(overview)
                .setReleaseDate(releaseDate)
                .setVoteCount(voteCount)
                .setVoteAverage(voteAverage)
                .build();
    }
}
