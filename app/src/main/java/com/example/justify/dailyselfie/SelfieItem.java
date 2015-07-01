package com.example.justify.dailyselfie;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by justify on 25.04.2015.
 */
public class SelfieItem {

    private File photoFile;
    private String title;

    public SelfieItem(File photoFile){
        this.photoFile = photoFile;
        title = photoFile.getName();
    }

    public File getPhotoFile(){
        return photoFile;
    }

    public String getTitle(){
        return title;
    }

}
