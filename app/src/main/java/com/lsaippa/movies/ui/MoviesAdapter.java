package com.lsaippa.movies.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lsaippa.movies.R;
import com.lsaippa.movies.model.MovieResult;
import com.lsaippa.movies.utilities.NetworkUtils;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private final MoviesAdapterOnClickHandler mClickHandler;

    private List<MovieResult> movieResultList;

    void setMoviesResult(List<MovieResult> movieResultResult) {
        if(movieResultList != null){
            this.movieResultList.addAll(movieResultResult);

        }else{
            this.movieResultList = movieResultResult;
        }
        notifyDataSetChanged();
    }

    void clear(){
        if(movieResultList !=null){
            movieResultList.clear();
        }else{
            Log.d(TAG,"Can't clear. MovieResult list is null!");
        }
    }

    public interface MoviesAdapterOnClickHandler{
        void onClick (MovieResult movieResult);
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
        MovieResult movieResult = movieResultList.get(position);
        holder.bind(movieResult.getPosterPath());
    }

    @Override
    public int getItemCount() {
        if(movieResultList ==null){
            return 0;
        }
        return movieResultList.size();
    }


    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mIvMovie;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mIvMovie = itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG,"onClick " + getAdapterPosition());
            mClickHandler.onClick(movieResultList.get((getAdapterPosition())));
        }

        void bind(String poster_path) {
            URL posterUrl = NetworkUtils.buildImageURL(poster_path);
            Picasso.get().load(posterUrl.toString()).into(mIvMovie);
        }
    }
}
