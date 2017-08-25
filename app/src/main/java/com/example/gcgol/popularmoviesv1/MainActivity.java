package com.example.gcgol.popularmoviesv1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gcgol.popularmoviesv1.adapters.RecyclerAdapter;
import com.example.gcgol.popularmoviesv1.utilities.GlobalVariables;
import com.example.gcgol.popularmoviesv1.utilities.NetworkUtils;
import com.example.gcgol.popularmoviesv1.utilities.OpenMoviesJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.RecyclerAdapterOnClickHandler {

    private RecyclerView mRecyclerView;

    private RecyclerAdapter mRecyclerAdapter;

    private ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgressBar = (ProgressBar) findViewById(R.id.pb_loading);

        mLoadingProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView =(RecyclerView) findViewById(R.id.recyclerview_movies);

        // Create GridLayoutManager for RecyclerView
        LinearLayoutManager layoutManager
                = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        // Set RecyclerView to fixed size to improve performance
        mRecyclerView.setHasFixedSize(true);

        // Instantiate and set RecyclerAdapter
        mRecyclerAdapter = new RecyclerAdapter(this);

        mRecyclerView.setAdapter(mRecyclerAdapter);

        if (isOnline()) {
            if (GlobalVariables.LAST_SORT == null || GlobalVariables.LAST_SORT.equals("POPULARITY")) {
                loadByPopularity();
            } else {
                loadByRating();
            }
        } else {
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(String movieItemData) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieItemData);
        startActivity(intentToStartDetailActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.rating_sort) {
            // sort by rating
            loadByRating();

            return true;
        }

        if (itemThatWasClickedId == R.id.popularity_sort) {
            // sort by popularity
            loadByPopularity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class GetMoviesTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            try {
                String movieSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

                String[] jsonSearchResults = OpenMoviesJsonUtils
                        .getSimplePosterStringsFromJson(MainActivity.this, movieSearchResults);

                return jsonSearchResults;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String[] s) {
            if (s != null && !s.equals("")) {

                mLoadingProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerAdapter.setMovieData(s);

            }
        }
    }

    private void loadByPopularity() {
        new GetMoviesTask().execute(NetworkUtils.buildUrl("/popular?"));
        GlobalVariables.LAST_SORT = "POPULARITY";
    }


    private void loadByRating() {
        new GetMoviesTask().execute(NetworkUtils.buildUrl("/top_rated?"));
        GlobalVariables.LAST_SORT = "RATING";
    }


    // Check for connectivity
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
