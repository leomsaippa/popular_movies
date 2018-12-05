package com.lsaippa.movies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("quicktime")
    @Expose
    private List<String> quicktime;
    @SerializedName("youtube")
    private List<MovieTrailerResult> youtube = null;
    @Expose
    @SerializedName("total_results")
    private Integer totalResults;

    public List<MovieTrailerResult> getResults() {
        return youtube;
    }

    public void setResults(List<MovieTrailerResult> results) {
        this.youtube = results;
    }

    @Override
    public String toString() {
        return "MovieTrailerResponse{" +
                ", results=" + youtube +
                '}';
    }
}
