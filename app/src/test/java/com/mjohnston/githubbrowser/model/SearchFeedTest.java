package com.mjohnston.githubbrowser.model;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by mattjohnston on 4/15/17.
 */

public class SearchFeedTest {

    @Test
    public void parseJson() {

        URL fileURL = getClass().getClassLoader().getResource("github_search.json");
        File file = new File(fileURL.getPath());

        String feedString = readFile(file);

        Gson gson = new Gson();

        SearchFeed feed = gson.fromJson(feedString, SearchFeed.class);

        assertEquals(30, feed.getItems().size());

        Repository repository = feed.getItems().get(0);


        assertEquals("chvin", repository.getOwnerName());
        assertEquals("react-tetris", repository.getName());
        assertEquals("https://github.com/chvin/react-tetris", repository.getLink());
        assertEquals(Integer.valueOf(1990), repository.getSize());
        assertEquals(Integer.valueOf(0), repository.getNumberIssues());
        assertEquals(Integer.valueOf(292), repository.getNumberForks());
        assertEquals("Use React, Redux, Immutable to code Tetris. \uD83C\uDFAE", repository.getDescription());
        assertEquals("https://avatars1.githubusercontent.com/u/5383506?v=3", repository.getOwnerAvatarUrl());
        assertEquals(Integer.valueOf(2205), repository.getStars());


    }

    private String readFile(File file) {

        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);

            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            reader.close();
            br.close();

            return sb.toString();

        }
        catch (IOException ioe) {
            fail("error reading file: " + ioe.getMessage());
            return null;
        }

    }
}
