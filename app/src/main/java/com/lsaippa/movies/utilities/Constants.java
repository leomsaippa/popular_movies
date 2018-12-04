package com.lsaippa.movies.utilities;

import com.lsaippa.movies.model.MovieResult;

public interface Constants {


    String MOVIES_BASE_URL = "https://api.themoviedb.org/3";

    String API_KEY_PARAM = "api_key";

    String PAGE = "page";

    String ENDPOINT_POPULAR_MOVIES = "/movie/popular";

    String ENDPOINT_TOP_RATED_MOVIES = "/movie/top_rated";

    String MOVIE = "/movie/";

    String ENDPOINT_REVIEWS = "/reviews";

    String ENDPOINT_TRAILERS = "/trailers";

    String FAVORITE = "favorite";

    String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185/";

    int DEFAULT_SPAN_SIZE = 2;

    int INITIAL_PAGE = 1;

    String DATE_FORMAT = "M/d/yy hh:mm a";

    String MOVIE_TAG = MovieResult.class.getSimpleName();

}
