package com.lsaippa.movies.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsaippa.movies.R;
import com.lsaippa.movies.model.MovieReviewResult;
import java.util.List;


public class MoviesReviewAdapter extends RecyclerView.Adapter<MoviesReviewAdapter.MoviesReviewAdapterViewHolder>{

    private static final String TAG = MoviesReviewAdapter.class.getSimpleName();

    private List<MovieReviewResult> movieReviewResults;

    void setMoviesReviewResult(List<MovieReviewResult> movieReviewResult) {
        if(movieReviewResults != null){
            this.movieReviewResults.addAll(movieReviewResult);

        }else{
            this.movieReviewResults = movieReviewResult;
        }
        notifyDataSetChanged();
    }

    void clear(){
        if(movieReviewResults !=null){
            movieReviewResults.clear();
        }else{
            Log.d(TAG,"Can't clear. MovieResult list is null!");
        }
    }


    public MoviesReviewAdapter() {
    }


    public class MoviesReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView mReviewAuthor;
        public final TextView mReviewContent;

        public MoviesReviewAdapterViewHolder(View itemView) {
            super(itemView);
            mReviewAuthor = itemView.findViewById(R.id.tv_authors);
            mReviewContent = itemView.findViewById(R.id.tv_contents);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG,"onClick " + getAdapterPosition());
        }

        void bind(String author, String content) {
            mReviewAuthor.setText(author);
            mReviewContent.setText(content);
        }
    }
    @NonNull
    @Override
    public MoviesReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_review_card_item,parent, false);
        return new MoviesReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesReviewAdapterViewHolder holder, int position) {
        MovieReviewResult movieReviewResult = movieReviewResults.get(position);
        holder.bind(movieReviewResult.getAuthor(), movieReviewResult.getContent());
    }

    @Override
    public int getItemCount() {
        if(movieReviewResults == null){
            return 0;
        }
        return movieReviewResults.size();
    }


}


