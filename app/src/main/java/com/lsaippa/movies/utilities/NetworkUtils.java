package com.lsaippa.movies.utilities;

import android.net.Uri;
import android.util.Log;

import com.lsaippa.movies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lsaippa on 22/10/18.
 */

public class NetworkUtils {

    //Ex: https://api.themoviedb.org/3/movie/popular?api_key


    final static String MOVIES_BASE_URL = "https://api.themoviedb.org/3";

    final static String API_KEY_PARAM = "api_key";

    public static final String ENDPOINT_POPULAR_MOVIES = "/movie/popular";

    public static final String ENDPOINT_TOP_RATED_MOVIES = "/movie/top_rated";



    public static URL buildURL(String type){
        String endpoint = MOVIES_BASE_URL;
        if (type.equals(ENDPOINT_POPULAR_MOVIES)) {
            endpoint += ENDPOINT_POPULAR_MOVIES;
        }else if(type.equals(ENDPOINT_TOP_RATED_MOVIES)){
            endpoint += ENDPOINT_TOP_RATED_MOVIES;
        }
        Uri builtUri = Uri.parse(endpoint).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        Log.d("TESTE URL ","Teste "+  builtUri.toString());

        return url;
    }
}
