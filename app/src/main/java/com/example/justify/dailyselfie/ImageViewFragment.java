package com.example.justify.dailyselfie;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by justify on 26.04.2015.
 */
public class ImageViewFragment extends Fragment {
    private static final String TAG = "IMAGE_VIEW_FRAGMENT";

    private ImageView imageView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.image_view_fragment, container, false);


        imageView = (ImageView) view.findViewById(R.id.imageView);
        //Read Filepath, rotate and flip bitmap, display image in ImageView
        Bundle bundle = getArguments();
        String filePath = bundle.getString("filepath");
        Bitmap sourceBitmap = BitmapFactory.decodeFile(filePath);
        Bitmap fixedBitmap = SelfieAdapter.rotateBitmap(sourceBitmap, 90.0F);
        fixedBitmap = SelfieAdapter.flip(fixedBitmap, SelfieAdapter.Direction.HORIZONTAL);

        imageView.setImageBitmap(fixedBitmap);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(container.getContext(), "Click :)", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
