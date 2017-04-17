package com.mjohnston.githubbrowser;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.mjohnston.githubbrowser.model.SearchFeed;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mattjohnston on 4/15/17.
 */

public class MockHelper {

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile(Context context, String filePath) throws Exception {
        final InputStream stream = context.getResources().getAssets().open(filePath);

        String ret = convertStreamToString(stream);
        stream.close();
        return ret;
    }


    public static SearchFeed loadSearchFeedFixture(Context context, String file)  {

        try {
            String fileContents = getStringFromFile(context, file);

            Gson gson = new Gson();

            SearchFeed feed = gson.fromJson(fileContents, SearchFeed.class);

            return feed;

        }
        catch (Exception e) {
            Log.e("helper", "error loading file: " + file, e);
            return null;
        }
    }
}
