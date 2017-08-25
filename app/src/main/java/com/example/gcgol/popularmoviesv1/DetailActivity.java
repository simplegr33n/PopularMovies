package com.example.gcgol.popularmoviesv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gcgol.popularmoviesv1.utilities.GlobalVariables;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private String mMovieData;

    private TextView mTitleTextView;

    private TextView mReleaseTextView;

    private TextView mRuntimeTextView;

    private TextView mRatingTextView;

    private TextView mSynopsisTextView;

    private ImageView mPosterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_title);

        mReleaseTextView = (TextView) findViewById(R.id.tv_release_year);

        mRatingTextView = (TextView) findViewById(R.id.tv_rating);

        mSynopsisTextView = (TextView) findViewById(R.id.tv_synopsis);

        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);

        // Get Extras from intent
        Intent originatingIntent = getIntent();

        if (originatingIntent != null) {

            if (originatingIntent.hasExtra(Intent.EXTRA_TEXT)) {

                mMovieData = originatingIntent.getStringExtra(Intent.EXTRA_TEXT);

                JSONObject movieItemJson = null;
                try {
                    movieItemJson = new JSONObject(mMovieData);

                    // Set TextViews
                    String titleString = movieItemJson.getString("title");
                    mTitleTextView.setText(titleString);

                    String releaseString = movieItemJson.getString("release_date");
                    String[] releaseStringArray = releaseString.split("-");
                    mReleaseTextView.setText(releaseStringArray[0]);

                    String ratingString = movieItemJson.getString("vote_average");
                    mRatingTextView.setText(ratingString + " / 10");

                    String synopsisString = movieItemJson.getString("overview");
                    mSynopsisTextView.setText(synopsisString);


                    // Build poster path and set ImageView with Picasso
                    String posterString = movieItemJson.getString("poster_path");
                    String posterPathString = GlobalVariables.BASE_POSTER_PATH + posterString;
                    Picasso.with(this).load(posterPathString)
                            .into(mPosterImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }



    }
}
