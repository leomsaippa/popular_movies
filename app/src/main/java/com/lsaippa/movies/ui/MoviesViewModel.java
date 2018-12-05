package com.lsaippa.movies.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.lsaippa.movies.database.AppDatabase;
import com.lsaippa.movies.model.MovieResult;

import java.util.List;


public class MoviesViewModel extends AndroidViewModel {

    final LiveData<List<MovieResult>> mMovies;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        mMovies = AppDatabase.getInstance(application.getApplicationContext()).movieDao().loadAllMovies();
    }

    public LiveData<List<MovieResult>> getMovies() {
        return mMovies;
    }
}
