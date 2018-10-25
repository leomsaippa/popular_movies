package com.lsaippa.movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import com.lsaippa.movies.model.MovieResult;
import com.lsaippa.movies.model.Movies;
import com.lsaippa.movies.utilities.EndlessRecyclerViewScrollListener;
import com.lsaippa.movies.utilities.NetworkUtils;

import java.net.URL;

import static com.lsaippa.movies.utilities.Constants.ENDPOINT_POPULAR_MOVIES;
import static com.lsaippa.movies.utilities.Constants.ENDPOINT_TOP_RATED_MOVIES;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    public static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView rv_mListMovies;
    private TextView tv_mError;
    private Context mContext;
    private int DEFAULT_SPAN_SIZE = 2;
    private int INITIAL_PAGE = 1;
    private String DEFAULT_ORDER_BY_MODE = ENDPOINT_TOP_RATED_MOVIES;

    private MoviesAdapter moviesAdapter;

    private EndlessRecyclerViewScrollListener scrollListener;

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

        GridLayoutManager layoutManager = new GridLayoutManager(mContext,DEFAULT_SPAN_SIZE);
        rv_mListMovies.setHasFixedSize(true);

        rv_mListMovies.setLayoutManager(layoutManager);

        moviesAdapter = new MoviesAdapter(this);
        rv_mListMovies.setAdapter(moviesAdapter);

        loadMovies(DEFAULT_ORDER_BY_MODE, INITIAL_PAGE);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG,"onLoad More "+page+"\n Items: "+totalItemsCount);
                loadMovies(DEFAULT_ORDER_BY_MODE,page);
            }
        };

        rv_mListMovies.addOnScrollListener(scrollListener);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_popular){
            loadMovies(ENDPOINT_POPULAR_MOVIES,INITIAL_PAGE);
        }else if(id == R.id.item_rated){
            loadMovies(ENDPOINT_TOP_RATED_MOVIES,INITIAL_PAGE);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovies(String type, int page) {

        URL moviesRequestUrl = NetworkUtils.buildURL(type, page);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, moviesRequestUrl.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Movies movies = gson.fromJson(response, Movies.class);

                Log.i(TAG, movies + " posts loaded.");
                moviesAdapter.setMoviesResult(movies.getResults());
                moviesAdapter.notifyDataSetChanged();
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
    public void onClick(MovieResult movieResult) {

        String MOVIETAG = MovieResult.class.getSimpleName();
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra(MOVIETAG, movieResult);

        startActivity(intent);
    }


}
