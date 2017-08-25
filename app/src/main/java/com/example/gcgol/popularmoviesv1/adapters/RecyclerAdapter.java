package com.example.gcgol.popularmoviesv1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gcgol.popularmoviesv1.MainActivity;
import com.example.gcgol.popularmoviesv1.R;
import com.example.gcgol.popularmoviesv1.utilities.GlobalVariables;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gcgol on 24/08/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerAdapterViewHolder> {

    private String[] mMovieData;

    private final RecyclerAdapterOnClickHandler mClickHandler;

    // COMPLETED (1) Add an interface called RecyclerAdapterOnClickHandler
    // COMPLETED (2) Within that interface, define a void method that access a String as a parameter
    /**
     * The interface that receives onClick messages.
     */
    public interface RecyclerAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    public RecyclerAdapter(RecyclerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }



    public class RecyclerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public final ImageView mPosterImageView;

        public RecyclerAdapterViewHolder(View view) {
            super(view);
            mPosterImageView = (ImageView) view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }

        // COMPLETED (6) Override onClick, passing the clicked day's data to mClickHandler via its onClick method

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String movieData = mMovieData[adapterPosition];
            mClickHandler.onClick(movieData);
        }
    }



    @Override
    public RecyclerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.listitem_movie;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecyclerAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerAdapterViewHolder posterAdapterViewHolder, int position) {

        String movieItemData = mMovieData[position];

        JSONObject movieItemJson = null;
        try {
            movieItemJson = new JSONObject(movieItemData);

            String posterString = movieItemJson.getString("poster_path");

            String posterPathString = GlobalVariables.BASE_POSTER_PATH + posterString;

            Picasso.with(posterAdapterViewHolder.itemView.getContext()).load(posterPathString)
                    .into(posterAdapterViewHolder.mPosterImageView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }


    public void setMovieData(String[] posterData) {
        mMovieData = posterData;
        notifyDataSetChanged();
    }


}