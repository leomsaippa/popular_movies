package com.lsaippa.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsaippa.movies.database.AppDatabase;
import com.lsaippa.movies.model.Movies;
import com.lsaippa.movies.utilities.AppExecutors;
import com.lsaippa.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.lsaippa.movies.utilities.Constants.MOVIE_TAG;


public class DetailActivity extends AppCompatActivity {

    AppDatabase mDb;
    Movies movie;
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

        }

    }

    private void checkFavoriteMovie(Integer id) {


        final LiveData<Movies> movie = mDb.movieDao().getMovies(id);
        movie.observe(this, new Observer<Movies>() {
            @Override
            public void onChanged(@Nullable Movies movieResult) {
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


}
