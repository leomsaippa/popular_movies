package com.lsaippa.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsaippa.movies.model.MovieResult;
import com.lsaippa.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

/**
 * Created by lsaippa on 23/10/18.
 */

public class DetailActivity extends AppCompatActivity {

    private ImageView mPosterMovie;

    private TextView mTitleMovie;
    private TextView mDateMovie;
    private TextView mVoteMovie;
    private TextView mSynopsisMovie;

    public static final String MOVIETAG = MovieResult.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleMovie = findViewById(R.id.tv_title);
        mDateMovie = findViewById(R.id.tv_date);
        mVoteMovie = findViewById(R.id.tv_votes);
        mSynopsisMovie = findViewById(R.id.tv_synopsis);
        mPosterMovie = findViewById(R.id.iv_poster);



        Bundle bundle = getIntent().getExtras();
        if(bundle!=null || getIntent().hasExtra(MOVIETAG)){
            MovieResult movieResult = getIntent().getParcelableExtra(MOVIETAG);


            URL posterUrl = NetworkUtils.buildImageURL(movieResult.getPosterPath());


            Picasso.get().load(posterUrl.toString()).into(mPosterMovie);

            mTitleMovie.setText(movieResult.getTitle());
            mVoteMovie.setText(String.valueOf(movieResult.getVoteAverage()));
            mDateMovie.setText(movieResult.getReleaseDate());
            mSynopsisMovie.setText(movieResult.getOverview());

        }
    }


}
