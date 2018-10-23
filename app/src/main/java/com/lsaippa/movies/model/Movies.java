package com.lsaippa.movies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lsaippa on 23/10/18.
 */

public class Movies {


    @SerializedName("page")
    private Integer page;
    @SerializedName("total_results")
    private Integer total_results;
    @SerializedName("total_pages")
    private Integer total_pages;
    @SerializedName("results")
    private List<MovieResult> results = null;

    public Movies(){
        
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public List<MovieResult> getResults() {
        return results;
    }

    public void setResults(List<MovieResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "page=" + page +
                ", total_results=" + total_results +
                ", total_pages=" + total_pages +
                ", results=" + results +
                '}';
    }
}
