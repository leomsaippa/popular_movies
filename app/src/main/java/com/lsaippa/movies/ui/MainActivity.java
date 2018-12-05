package com.lsaippa.movies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.lsaippa.movies.R;
import com.lsaippa.movies.model.MovieResponse;
import com.lsaippa.movies.model.MovieResult;

import com.lsaippa.movies.utilities.EndlessRecyclerViewScrollListener;
import com.lsaippa.movies.utilities.JsonParser;
import com.lsaippa.movies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import static com.lsaippa.movies.utilities.Constants.DEFAULT_SPAN_SIZE;
import static com.lsaippa.movies.utilities.Constants.ENDPOINT_POPULAR_MOVIES;
import static com.lsaippa.movies.utilities.Constants.ENDPOINT_TOP_RATED_MOVIES;
import static com.lsaippa.movies.utilities.Constants.FAVORITE;
import static com.lsaippa.movies.utilities.Constants.INITIAL_PAGE;
import static com.lsaippa.movies.utilities.Constants.MOVIE_TAG;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final String DEFAULT_ORDER_BY_MODE = ENDPOINT_TOP_RATED_MOVIES;

    private MoviesAdapter moviesAdapter;

    private String currentMovieType;
    private int currentPage;

    private RecyclerView mRecyclerView;
    private TextView mError;
    private ProgressBar mProgressBar;
    private Button mButtonTryAgain;

    private EndlessRecyclerViewScrollListener scrollListener;
    private MoviesViewModel moviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();

        loadMovies(currentMovieType, currentPage);
    }

    private void setup() {

        currentMovieType = DEFAULT_ORDER_BY_MODE;
        currentPage = INITIAL_PAGE;

        mRecyclerView = findViewById(R.id.rv_moviesList);
        mError = findViewById(R.id.tv_error);
        mProgressBar = findViewById(R.id.pb_loading);
        mButtonTryAgain = findViewById(R.id.btn_try_again);

        GridLayoutManager layoutManager = new GridLayoutManager(this,DEFAULT_SPAN_SIZE);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(layoutManager);

        moviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(moviesAdapter);

        setupViewModel();
        mButtonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovies(currentMovieType,currentPage);
            }
        });


        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.d(TAG, "onLoad More " + page + "\n Total items: " + totalItemsCount);
                currentPage = page;
                if (!currentMovieType.equals(FAVORITE)){
                    loadMovies(currentMovieType, currentPage);
                }
            }
        };

        mRecyclerView.addOnScrollListener(scrollListener);
    }

    private void setupViewModel() {

        moviesViewModel = ViewModelProviders.of(this)
                .get(MoviesViewModel.class);
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
        }else if(id == R.id.item_favorite){
            loadFavoriteMovies(FAVORITE);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFavoriteMovies(final String type) {
        moviesViewModel.getMovies().observe(this, new Observer<List<MovieResult>>() {
            @Override
            public void onChanged(@Nullable List<MovieResult> movies) {
                verifyCurrentType(type);
                Log.d(TAG,"MovieResult loadFav " + movies.toString());
                scrollListener.resetState();
                moviesAdapter.clear();
                moviesAdapter.notifyDataSetChanged();
                moviesAdapter.setMoviesResult(movies);
                showList();
                moviesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadMovies(String type, int page) {

        if(NetworkUtils.isOnline(getApplicationContext())){

            showLoading();

            verifyCurrentType(type);
            URL moviesRequestUrl = NetworkUtils.buildURL(type, page);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, moviesRequestUrl.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d(TAG,"onResponse");
                    MovieResponse movies = JsonParser.getMoviesFromJson(response);

                    moviesAdapter.setMoviesResult(movies.getResults());
                    showList();
                    moviesAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d(TAG,"onErrorResponse");

                    if (volleyError instanceof NetworkError) {
                        showError();
                    } else if (volleyError instanceof ServerError) {
                        showError();
                    } else if (volleyError instanceof AuthFailureError) {
                        showError();
                    } else if (volleyError instanceof ParseError) {
                        showError();
                    } else if (volleyError instanceof TimeoutError) {
                        showError();
                    }else{
                        Toast.makeText(MainActivity.this, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            queue.add(stringRequest);
        }else{
            verifyCurrentType(type);
            showError();
        }

    }


    private void verifyCurrentType(String type) {
        if(!currentMovieType.equals(type)){
            currentMovieType = type;
            scrollListener.resetState();
            moviesAdapter.clear();
            moviesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(MovieResult movieResult) {
        Intent intent = new Intent(MainActivity.this,DetailActivity.class);
        intent.putExtra(MOVIE_TAG, movieResult);

        startActivity(intent);
    }

    private void showError(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mButtonTryAgain.setVisibility(View.VISIBLE);
    }

    private void showLoading(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mError.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mButtonTryAgain.setVisibility(View.INVISIBLE);
    }

    private void showList(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mError.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mButtonTryAgain.setVisibility(View.INVISIBLE);
    }

    private void removeObservers() {
        if ( moviesViewModel != null && moviesViewModel.getMovies().hasObservers()){
            moviesViewModel.getMovies().removeObservers(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeObservers();
    }
}
