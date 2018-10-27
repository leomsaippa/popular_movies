package com.lsaippa.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

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


}
