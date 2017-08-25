package com.example.gcgol.popularmoviesv1.utilities;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by gcgol on 24/08/2017.
 */

public final class OpenMoviesJsonUtils {


    public static String[] getSimplePosterStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */
        final String OWM_RESULTS = "results";

        final String OWM_MESSAGE_CODE = "cod";

        /* String array to hold each day's weather String */
        String[] parsedMovieData = null;

        JSONObject moviesJson = new JSONObject(forecastJsonStr);

        /* Is there an error? */
        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray moviesArray = moviesJson.getJSONArray(OWM_RESULTS);

        parsedMovieData = new String[moviesArray.length()];


        for (int i = 0; i < moviesArray.length(); i++) {

            String movieInfo = moviesArray.getJSONObject(i).toString();

            parsedMovieData[i] = movieInfo;
        }

        return parsedMovieData;
    }


}