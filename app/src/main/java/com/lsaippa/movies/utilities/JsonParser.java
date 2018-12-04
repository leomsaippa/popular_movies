package com.lsaippa.movies.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsaippa.movies.model.MovieResponse;

import static com.lsaippa.movies.utilities.Constants.DATE_FORMAT;


public class JsonParser {

    public static MovieResponse getMoviesFromJson(String response) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(DATE_FORMAT);
        Gson gson = gsonBuilder.create();

        return gson.fromJson(response, MovieResponse.class);
    }
}
