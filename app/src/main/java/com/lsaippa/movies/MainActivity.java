package com.lsaippa.movies;

import android.content.Context;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsaippa.movies.model.Movies;
import com.lsaippa.movies.utilities.NetworkUtils;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

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

        new FetchMoviesTask().execute();
    }

    @Override
    public void onClick() {
        //Todo add new activity
        Toast.makeText(mContext, "Go new activity! ", Toast.LENGTH_SHORT).show();
    }


    public class FetchMoviesTask extends AsyncTask<Void,Void,Movies>{


        //Todo modifies to return correctly in a list
        Movies movies = null;
        @Override
        protected Movies doInBackground(Void... voids) {
            URL moviesRequestUrl = NetworkUtils.buildURL(NetworkUtils.ENDPOINT_TOP_RATED_MOVIES);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, moviesRequestUrl.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                   movies = gson.fromJson(response, Movies.class);


                    Log.i("Movies", movies + " posts loaded.");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.d(TAG,"onError " + error);
                }
            });


            queue.add(stringRequest);


            while (movies == null){

            }
            return movies;
        }

        @Override
        protected void onPostExecute(Movies movies) {
            super.onPostExecute(movies);
            moviesAdapter.setMoviesDate(movies);
        }
    }




}
