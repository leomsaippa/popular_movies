package com.lsaippa.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsaippa.movies.model.Movies;

/**
 * Created by lsaippa on 22/10/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private final MoviesAdapterOnClickHandler mClickHandler;

    private Movies moviesDate;

    public void setMoviesDate(Movies moviesDate) {
        this.moviesDate = moviesDate;
        notifyDataSetChanged();
    }

    public interface MoviesAdapterOnClickHandler{
        void onClick ();
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

        if(moviesDate !=null){
             holder.tv_text.setText(
                     moviesDate.getResults().get(position).getTitle());

        }
    }

    @Override
    public int getItemCount() {
        if(moviesDate==null){
            return 0;
        }
        return moviesDate.getTotal_results();
    }


    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public  final TextView tv_text;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            tv_text = itemView.findViewById(R.id.tv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick();
        }
    }
}
