package com.mjohnston.githubbrowser.model;

import junit.framework.Assert;

import org.junit.Test;


public class RepositoryTest {

    @Test
    public void sizeFormatting() {

        Repository repo = new Repository();

        // if size is null
        Assert.assertEquals("0", repo.getFormattedSize());

        repo.setSize(500);
        Assert.assertEquals("500 KB", repo.getFormattedSize());


        repo.setSize(5000);
        Assert.assertEquals("5 MB", repo.getFormattedSize());


        repo.setSize(5000000);
        Assert.assertEquals("5 GB", repo.getFormattedSize());

    }
}