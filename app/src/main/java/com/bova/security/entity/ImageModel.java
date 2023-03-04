package com.bova.security.entity;

import com.atech.staggedrv.model.StaggedModel;

public class ImageModel implements StaggedModel {
    private int width;
    private int height;
    private int resourceId;
    private String pictureUrl;
    private String title;
    private String author;

    public ImageModel(int width, int height, int resourceId, String title, String author) {
        this.width = width;
        this.height = height;
        this.resourceId = resourceId;
        this.title = title;
        this.author = author;
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getThumb() {
        return null;
    }

    @Override
    public int localResorce() {
        return resourceId;
    }


    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
