package com.lsaippa.movies.utilities;

/**
 * Created by lsaippa on 24/10/18.
 */

public interface Constants {

    //Ex: https://api.themoviedb.org/3/movie/popular?api_key


    final static String MOVIES_BASE_URL = "https://api.themoviedb.org/3";

    final static String API_KEY_PARAM = "api_key";
    final static String PAGE = "page";

    public static final String ENDPOINT_POPULAR_MOVIES = "/movie/popular";

    public static final String ENDPOINT_TOP_RATED_MOVIES = "/movie/top_rated";

    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185/";
}
