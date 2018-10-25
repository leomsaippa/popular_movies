package com.lsaippa.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsaippa.movies.model.MovieResult;
import com.lsaippa.movies.model.Movies;
import com.lsaippa.movies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by lsaippa on 22/10/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    public static final String TAG = MoviesAdapter.class.getSimpleName();

    private final MoviesAdapterOnClickHandler mClickHandler;


    private List<MovieResult> moviesList;


    public void setMoviesResult(List<MovieResult> moviesResult) {
        this.moviesList = moviesResult;
        notifyDataSetChanged();
    }

    public interface MoviesAdapterOnClickHandler{
        void onClick (MovieResult movieResult);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }


    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {

            MovieResult movie = moviesList.get(position);

            holder.bind(movie.getPosterPath());




    }

    @Override
    public int getItemCount() {
        if(moviesList==null){
            return 0;
        }
        return moviesList.size();
    }


    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView iv_movie;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            iv_movie = itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mClickHandler.onClick(moviesList.get((getAdapterPosition())));
        }

        public void bind(String poster_path) {
            URL posterUrl = NetworkUtils.buildImageURL(poster_path);


            Picasso.get().load(posterUrl.toString()).into(iv_movie);
        }
    }
}
