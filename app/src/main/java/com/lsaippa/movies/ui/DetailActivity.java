package com.lsaippa.movies.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
import com.lsaippa.movies.database.AppDatabase;
import com.lsaippa.movies.model.MovieResult;
import com.lsaippa.movies.model.MovieReviewResponse;
import com.lsaippa.movies.model.MovieTrailerResponse;
import com.lsaippa.movies.utilities.AppExecutors;
import com.lsaippa.movies.utilities.JsonParser;
import com.lsaippa.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static android.content.Intent.ACTION_VIEW;
import static com.lsaippa.movies.utilities.Constants.ENDPOINT_REVIEWS;
import static com.lsaippa.movies.utilities.Constants.ENDPOINT_TRAILERS;
import static com.lsaippa.movies.utilities.Constants.MOVIE_TAG;


public class DetailActivity extends AppCompatActivity implements MoviesTrailerAdapter.MoviesTrailerAdapterOnClickHandler{


    private RecyclerView mRvTrailers;

    private MoviesReviewAdapter mReviewAdapter;
    private MoviesTrailerAdapter mTrailerAdapter;


    AppDatabase mDb;
    MovieResult movie;
    Menu menu;
    boolean isFavorite = false;
    public static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView mTitleMovie = findViewById(R.id.tv_title);
        TextView mDateMovie = findViewById(R.id.tv_date);
        TextView mVoteMovie = findViewById(R.id.tv_votes);
        TextView mSynopsisMovie = findViewById(R.id.tv_synopsis);
        ImageView mPosterMovie = findViewById(R.id.iv_poster);

        RecyclerView mRvReviews = findViewById(R.id.rv_movieReviews);
        mRvTrailers = findViewById(R.id.rv_movieTrailers);

        mDb = AppDatabase.getInstance(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null || getIntent().hasExtra(MOVIE_TAG)){
            movie = getIntent().getParcelableExtra(MOVIE_TAG);

            URL posterUrl = NetworkUtils.buildImageURL(movie.getPosterPath());
            Picasso.get().load(posterUrl.toString()).into(mPosterMovie);

            mTitleMovie.setText(movie.getTitle());
            mVoteMovie.setText(String.valueOf(movie.getVoteAverage()));
            mDateMovie.setText(movie.getReleaseDate());
            mSynopsisMovie.setText(movie.getOverview());
            checkFavoriteMovie(movie.getId());

            loadReviewsMovie(movie.getId().toString());
            loadTrailersMovie(movie.getId().toString());

            GridLayoutManager layoutManager = new GridLayoutManager(this,1);
            GridLayoutManager lm = new GridLayoutManager(this,1);
            mRvReviews.setHasFixedSize(true);
            mRvTrailers.setHasFixedSize(true);

            mRvReviews.setLayoutManager(layoutManager);
            mRvTrailers.setLayoutManager(lm);
        }

        mReviewAdapter = new MoviesReviewAdapter();
        mTrailerAdapter = new MoviesTrailerAdapter(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRvTrailers.getContext(),
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable((ContextCompat.getDrawable(this, R.drawable.item_divider)));

        mRvTrailers.addItemDecoration(dividerItemDecoration);

        mRvReviews.setAdapter(mReviewAdapter);
        mRvTrailers.setAdapter(mTrailerAdapter);

    }

    private void checkFavoriteMovie(Integer id) {


        final LiveData<MovieResult> movie = mDb.movieDao().getMovies(id);
        movie.observe(this, new Observer<MovieResult>() {
            @Override
            public void onChanged(@Nullable MovieResult movieResult) {
                movie.removeObserver(this);
                isFavorite = movieResult != null;
                Log.d(TAG,"CheckFavorite " + isFavorite);
                if(menu !=null){
                    updateFavorite(menu.findItem(R.id.favorite_img));
                }else{
                    Log.d(TAG,"Update not call, menu null");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Log.d(TAG,"onCreateOptionsMenu " + isFavorite);
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        updateFavorite(menu.findItem(R.id.favorite_img));
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.favorite_img){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {

                    if(isFavorite){
                        mDb.movieDao().deleteMovie(movie);
                        isFavorite = false;
                        Log.d(TAG,"Deleting from DAO");
                    }else{
                        isFavorite = true;
                        mDb.movieDao().insertMovie(movie);
                        Log.d(TAG,"Inserting in DAO");
                    }
                    updateFavorite(item);
                }

            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateFavorite(final MenuItem item) {
        Log.d(TAG,"Calling update Favorite " + isFavorite);
        final Drawable  drawable;
        if(isFavorite){
            drawable = ResourcesCompat.getDrawable(getResources(),android.R.drawable.btn_star_big_on,null);

        }else{
            drawable = ResourcesCompat.getDrawable(getResources(),android.R.drawable.btn_star_big_off, null);

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                item.setIcon(drawable);

            }
        });
    }

    private void loadReviewsMovie(String id) {

        if(NetworkUtils.isOnline(getApplicationContext())){

            URL moviesRequestUrl = NetworkUtils.buildIdMovieURL(ENDPOINT_REVIEWS, id);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, moviesRequestUrl.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d(TAG,"onResponse");
                    MovieReviewResponse movies = JsonParser.getMovieReviewResponse(response);

                    Log.d(TAG,"Movies response " + movies.getTotalResults());


                    mReviewAdapter.notifyDataSetChanged();
                    mReviewAdapter.setMoviesReviewResult(movies.getResults());

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
                        Toast.makeText(DetailActivity.this, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            queue.add(stringRequest);
        }else{
            showError();
        }

    }

    private void loadTrailersMovie(String id) {

        if(NetworkUtils.isOnline(getApplicationContext())){


            URL moviesRequestUrl = NetworkUtils.buildIdMovieURL(ENDPOINT_TRAILERS, id);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, moviesRequestUrl.toString(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.d(TAG,"onResponse trailer " + response );

                    MovieTrailerResponse movies = JsonParser.getMovieTrailerResponse(response);
                    mTrailerAdapter.notifyDataSetChanged();
                    mTrailerAdapter.setMoviesTrailerResult(movies.getResults());

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
                        Toast.makeText(DetailActivity.this, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            queue.add(stringRequest);
        }else{
            showError();
        }

    }


    private void showError(){
        Toast.makeText(this, R.string.something_wrong, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(String source) {

        Intent videoIntent = new Intent();
        videoIntent.setAction(ACTION_VIEW);
        videoIntent.setData(NetworkUtils.buildYoutubeURI(source));

        if(videoIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(videoIntent);
        }
    }

}
