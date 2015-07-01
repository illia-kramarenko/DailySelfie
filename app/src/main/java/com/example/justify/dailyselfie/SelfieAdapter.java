package com.example.justify.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justify on 25.04.2015.
 */
public class SelfieAdapter extends BaseAdapter {

    public enum Direction { VERTICAL, HORIZONTAL };

    private static final String TAG = "SELFIE_ADAPTER";

    private List<SelfieItem> items = new ArrayList<SelfieItem>();
    private final Context context;


    public SelfieAdapter(Context c){
        context = c;
    }

    public void add(SelfieItem item) {
        Log.i(TAG, "ADD");
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        Log.i(TAG, "CLEAR");
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        Log.i(TAG, "GET COUNT");
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i(TAG, "GET ITEM");
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, "GET ITEM ID");
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "GET VIEW");
        final SelfieItem selfieItem = items.get(position);

        RelativeLayout selfieLayout;

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            selfieLayout = (RelativeLayout) layoutInflater.inflate(R.layout.selfie_item, null, false);
        } else {
            selfieLayout = (RelativeLayout) convertView;
        }

        final ImageView selfieView = (ImageView) selfieLayout.findViewById(R.id.selfieView);
        Bitmap sourceBitmap = BitmapFactory.decodeFile(selfieItem.getPhotoFile().getAbsolutePath());
        Bitmap fixedBitmap = rotateBitmap(sourceBitmap, 90.0F);
        fixedBitmap = flip(fixedBitmap, Direction.HORIZONTAL);
        selfieView.setImageBitmap(fixedBitmap);


        final TextView selfieTitle = (TextView) selfieLayout.findViewById(R.id.selfieTitle);
        selfieTitle.setText(selfieItem.getTitle());

        return selfieLayout;
    }

    //Return filepath for selected item. Used to pass filepath to Fragment
    public String getFilePath(int position){
        SelfieItem sItem = items.get(position);
        return sItem.getPhotoFile().getAbsolutePath();
    }





    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

    }


    public static Bitmap flip(Bitmap src, Direction type) {

        Matrix matrix = new Matrix();

        if(type == Direction.VERTICAL) {
            matrix.preScale(1.0f, -1.0f);
        }
        else if(type == Direction.HORIZONTAL) {
            matrix.preScale(-1.0f, 1.0f);
        } else {
            return src;
        }

        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


}
