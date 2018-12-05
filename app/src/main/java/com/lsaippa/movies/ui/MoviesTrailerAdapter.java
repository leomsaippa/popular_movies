package com.lsaippa.movies.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsaippa.movies.R;
import com.lsaippa.movies.model.MovieTrailerResult;

import java.util.List;


public class MoviesTrailerAdapter extends RecyclerView.Adapter<MoviesTrailerAdapter.MoviesTrailerAdapterViewHolder> {

    private static final String TAG = MoviesReviewAdapter.class.getSimpleName();

    private final MoviesTrailerAdapterOnClickHandler mClickHandler;

    private List<MovieTrailerResult> movieTrailerResults;

    void setMoviesTrailerResult(List<MovieTrailerResult> movieTrailerResults) {
        if(this.movieTrailerResults != null){
            this.movieTrailerResults.addAll(movieTrailerResults);

        }else{
            this.movieTrailerResults = movieTrailerResults;
        }
        notifyDataSetChanged();
    }

    void clear(){
        if(movieTrailerResults !=null){
            movieTrailerResults.clear();
        }else{
            Log.d(TAG,"Can't clear. MovieResult list is null!");
        }
    }


    public MoviesTrailerAdapter(MoviesTrailerAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public interface MoviesTrailerAdapterOnClickHandler{
        void onClick (String source);
    }

    public class MoviesTrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        final TextView mTrailer;

        public MoviesTrailerAdapterViewHolder(View itemView) {
            super(itemView);
            mTrailer = itemView.findViewById(R.id.tv_trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG,"onClick " + getAdapterPosition());
            mClickHandler.onClick(movieTrailerResults.get(getAdapterPosition()).getSource());
        }

        void bind(String name) {
            mTrailer.setText(name);
        }
    }
    @NonNull
    @Override
    public MoviesTrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_trailer_card_item,parent, false);
        return new MoviesTrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesTrailerAdapterViewHolder holder, int position) {
        MovieTrailerResult movieTrailerResult = movieTrailerResults.get(position);
        holder.bind(movieTrailerResult.getName());
    }

    @Override
    public int getItemCount() {
        if(movieTrailerResults == null){
            return 0;
        }
        return movieTrailerResults.size();
    }

}
