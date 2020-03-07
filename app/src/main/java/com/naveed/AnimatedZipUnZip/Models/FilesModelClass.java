package com.naveed.AnimatedZipUnZip.Models;

import android.graphics.drawable.Drawable;

public class FilesModelClass {
    String name;
    String location;
    String type;
    Drawable drawable;
    boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public FilesModelClass(String name, String location, String type , Drawable drawable) {
        this.name = name;
        this.location = location;
        this.type = type;
        this.drawable =drawable;

    }

    public Drawable getItemIcon() {
        return drawable;
    }

    public void setItemIcon(Drawable drawable) {
        this.drawable = drawable;
    }

    public FilesModelClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
