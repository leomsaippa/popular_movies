package com.lsaippa.movies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsaippa.movies.model.Movies;
import com.lsaippa.movies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    public static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rv_mListMovies;
    private TextView tv_mError;
    private Context mContext;
    private int DEFAULT_SPAN_SIZE = 2;

    private MoviesAdapter moviesAdapter;

    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();


        mContext = this;
        rv_mListMovies = findViewById(R.id.rv_moviesList);
        tv_mError = findViewById(R.id.tv_error);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext,DEFAULT_SPAN_SIZE);
        rv_mListMovies.setHasFixedSize(true);

        rv_mListMovies.setLayoutManager(layoutManager);

        moviesAdapter = new MoviesAdapter(this);
        rv_mListMovies.setAdapter(moviesAdapter);
        loadMovies();

    }

    private void loadMovies() {

        URL moviesRequestUrl = NetworkUtils.buildURL(NetworkUtils.ENDPOINT_TOP_RATED_MOVIES);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, moviesRequestUrl.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Movies movies = gson.fromJson(response, Movies.class);

                Log.i(TAG, movies + " posts loaded.");
                moviesAdapter.setMoviesDate(movies);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                //Todo adds treatment for missing network connection
                String message = null;

                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }

                Log.d(TAG,"onError " + message);
            }
        });


        queue.add(stringRequest);



    }

    @Override
    public void onClick() {
        //Todo add new activity
        Toast.makeText(mContext, "Go new activity! ", Toast.LENGTH_SHORT).show();
    }


}
