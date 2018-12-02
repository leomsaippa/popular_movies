package com.lsaippa.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lsaippa.movies.model.Movies;
import com.lsaippa.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.lsaippa.movies.utilities.Constants.MOVIE_TAG;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView mTitleMovie = findViewById(R.id.tv_title);
        TextView mDateMovie = findViewById(R.id.tv_date);
        TextView mVoteMovie = findViewById(R.id.tv_votes);
        TextView mSynopsisMovie = findViewById(R.id.tv_synopsis);
        ImageView mPosterMovie = findViewById(R.id.iv_poster);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null || getIntent().hasExtra(MOVIE_TAG)){
            Movies movies = getIntent().getParcelableExtra(MOVIE_TAG);

            URL posterUrl = NetworkUtils.buildImageURL(movies.getPosterPath());
            Picasso.get().load(posterUrl.toString()).into(mPosterMovie);

            mTitleMovie.setText(movies.getTitle());
            mVoteMovie.setText(String.valueOf(movies.getVoteAverage()));
            mDateMovie.setText(movies.getReleaseDate());
            mSynopsisMovie.setText(movies.getOverview());

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    boolean isFavorite = false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.favorite_img){
            if(isFavorite){
                item.setIcon(android.R.drawable.btn_star_big_off);
                isFavorite = false;
                Toast.makeText(this, "Removes to favorite successfully!", Toast.LENGTH_SHORT).show();
            }else{
                item.setIcon(android.R.drawable.btn_star_big_on);
                isFavorite = true;
                Toast.makeText(this, "Add to favorite successfully!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
