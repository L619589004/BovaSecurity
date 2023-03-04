package com.bova.security.entity;

import com.atech.staggedrv.model.StaggedModel;

public class StyleModel{
    private int imageResId;
    private int imageUrl;
    private String styleName;
    private boolean isSelected;

    public StyleModel(int imageResId, String styleName, boolean isSelected) {
        this.imageResId = imageResId;
        this.styleName = styleName;
        this.isSelected = isSelected;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
