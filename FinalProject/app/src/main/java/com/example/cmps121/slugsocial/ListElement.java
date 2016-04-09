package com.example.cmps121.slugsocial;

import android.graphics.Bitmap;

public class ListElement {
    ListElement() {};

    ListElement(int img, String tit, String det, Bitmap b) {
        title = tit;
        details = det;
        image = img;
        bit = b;
    }

    public String title;
    public String details;
    public int image;
    public Bitmap bit;//Restaurant details button. Currently not implemented


}
