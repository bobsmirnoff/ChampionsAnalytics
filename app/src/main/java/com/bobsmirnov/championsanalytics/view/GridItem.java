package com.bobsmirnov.championsanalytics.view;

import android.graphics.Bitmap;

/**
 * Created by bobsmirnov on 14.11.15
 */

public class GridItem {
    private Bitmap bitmap;
    private String title;

    public GridItem(Bitmap bitmap, String title) {
        super();
        this.bitmap = bitmap;
        this.title = title;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
