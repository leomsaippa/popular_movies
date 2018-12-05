package com.lsaippa.movies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.lsaippa.movies.model.MovieResult;

import java.util.List;

/**
 * Created by lsaippa on 02/12/18.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    LiveData<List<MovieResult>> loadAllMovies();

    @Query("SELECT * FROM movie WHERE id = :movieId")
    LiveData<MovieResult> getMovies(int movieId);

    @Insert
    void insertMovie(MovieResult movie);

    @Delete
    void deleteMovie(MovieResult movie);
}

