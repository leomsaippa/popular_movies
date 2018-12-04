package com.lsaippa.movies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerResponse {

    @SerializedName("results")
    @Expose
    private List<MovieTrailerResult> results = null;

    public List<MovieTrailerResult> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailerResult> results) {
        this.results = results;
    }
}
