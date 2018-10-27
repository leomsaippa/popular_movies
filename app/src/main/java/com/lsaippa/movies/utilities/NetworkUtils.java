package com.lsaippa.movies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import com.lsaippa.movies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

import static com.lsaippa.movies.utilities.Constants.API_KEY_PARAM;
import static com.lsaippa.movies.utilities.Constants.ENDPOINT_POPULAR_MOVIES;
import static com.lsaippa.movies.utilities.Constants.ENDPOINT_TOP_RATED_MOVIES;
import static com.lsaippa.movies.utilities.Constants.IMAGE_BASE_URL;
import static com.lsaippa.movies.utilities.Constants.MOVIES_BASE_URL;
import static com.lsaippa.movies.utilities.Constants.PAGE;


public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static URL buildURL(String type, int page){
        String endpoint = MOVIES_BASE_URL;
        if (type.equals(ENDPOINT_POPULAR_MOVIES)) {
            endpoint += ENDPOINT_POPULAR_MOVIES;
        }else if(type.equals(ENDPOINT_TOP_RATED_MOVIES)){
            endpoint += ENDPOINT_TOP_RATED_MOVIES;
        }
        Uri builtUri = Uri.parse(endpoint).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .appendQueryParameter(PAGE,String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        Log.d(TAG,"URI "+  builtUri.toString());

        return url;
    }


    public static URL buildImageURL(String path){

        String endPoint = IMAGE_BASE_URL + path;
        Uri builtUri = Uri.parse(endPoint).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d(TAG,"Image  "+  builtUri.toString());

        return url;
    }

}
