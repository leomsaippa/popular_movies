package com.lsaippa.movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lsaippa.movies.model.Movies;
import com.lsaippa.movies.utilities.NetworkUtils;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private final MoviesAdapterOnClickHandler mClickHandler;

    private List<Movies> moviesList;

    void setMoviesResult(List<Movies> moviesResult) {
        if(moviesList != null){
            this.moviesList.addAll(moviesResult);

        }else{
            this.moviesList = moviesResult;
        }
        notifyDataSetChanged();
    }

    void clear(){
        if(moviesList!=null){
            moviesList.clear();
        }else{
            Log.d(TAG,"Can't clear. Movies list is null!");
        }
    }

    public interface MoviesAdapterOnClickHandler{
        void onClick (Movies movies);
    }

    MoviesAdapter(MoviesAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }


    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_list_item,parent, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder holder, int position) {
        Movies movies = moviesList.get(position);
        holder.bind(movies.getPosterPath());
    }

    @Override
    public int getItemCount() {
        if(moviesList==null){
            return 0;
        }
        return moviesList.size();
    }


    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mIvMovie;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mIvMovie = itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG,"onClick " + getAdapterPosition());
            mClickHandler.onClick(moviesList.get((getAdapterPosition())));
        }

        void bind(String poster_path) {
            URL posterUrl = NetworkUtils.buildImageURL(poster_path);
            Picasso.get().load(posterUrl.toString()).into(mIvMovie);
        }
    }
}
