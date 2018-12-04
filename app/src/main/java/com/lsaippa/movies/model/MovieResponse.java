package com.lsaippa.movies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


@SuppressWarnings("unused")
public class MovieResponse {


    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private List<MovieResult> results = null;

    public MovieResponse(){

    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal_results() {
        return totalResults;
    }

    public void setTotal_results(Integer total_results) {
        this.totalResults = total_results;
    }

    public Integer getTotal_pages() {
        return totalPages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.totalPages = total_pages;
    }

    public List<MovieResult> getResults() {
        return results;
    }

    public void setResults(List<MovieResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "page=" + page +
                ", total_results=" + totalResults +
                ", total_pages=" + totalPages +
                ", results=" + results +
                '}';
    }
}
