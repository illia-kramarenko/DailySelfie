package com.example.justify.dailyselfie;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DailySelfieMain extends ListActivity {

    public static final int TAKE_PHOTO_REQUEST_CODE = 10;
    private static final String TAG = "DAILY_SELFIE_MAIN";
    private final String filesPath = "/storage/sdcard/Android/data/com.example.justify.dailyselfie/files/Pictures";
    private static final long ALARM_DELAY_TWO_MINS = 2 * 60 * 1000L;

    //string for path of the photo
    private String mCurrentPhotoPath;

    private File photoFile = null;

    private SelfieAdapter mAdapter;

    private ImageViewFragment imageViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creating new Selfie adapter and getting List View
        mAdapter = new SelfieAdapter(getApplicationContext());
        ListView listView = getListView();
        
        //Checking default folder for images and adding them to List Adapter
        File[] files = new File(filesPath).listFiles();
        if(files != null) {
            if (files.length > 0) {
                for (File f : files) {
                    mAdapter.add(new SelfieItem(f));
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "First launch or can't read files", Toast.LENGTH_LONG).show();
        }
        //On Click listener for list view element
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Transferring filepath of selected item to Image View fragment
                Bundle bundle = new Bundle();
                bundle.putString("filepath", mAdapter.getFilePath(position));
                imageViewFragment = new ImageViewFragment();
                imageViewFragment.setArguments(bundle);
                //Calling Image View fragment for showing picture
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, imageViewFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //Set List Adapter
        listView.setAdapter(mAdapter);

        // Get the AlarmManager Service
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        Intent mNotificationReceiverIntent = new Intent(DailySelfieMain.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        PendingIntent mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                DailySelfieMain.this, 0, mNotificationReceiverIntent, 0);

        mAlarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + ALARM_DELAY_TWO_MINS,
                ALARM_DELAY_TWO_MINS,
                mNotificationReceiverPendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_daily_selfie_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        //Camera Activation
        if (id == R.id.camera_activation) {
            Log.i(TAG, "CAMERA_ACTIVATED");
            Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(camIntent.resolveActivity(getPackageManager()) != null){
                photoFile = null;
                Log.i(TAG, "INTENT_OK, PHOTOFILE=null");
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Log.i(TAG, "START FOR RESULT");
                    camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(camIntent, TAKE_PHOTO_REQUEST_CODE);
                }
            }
            return true;
        }
        //Delete all images
        if (id == R.id.delete_all){
            File[] files = new File(filesPath).listFiles();
            if(files.length>0) {
                for (File f : files) {
                    f.delete();
                }
                mAdapter.clear();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK){
           if(photoFile.exists()){
               Log.i(TAG, "ON_ACTIVITY_RESULT");
               Toast.makeText(DailySelfieMain.this, "Selfie Added", Toast.LENGTH_SHORT).show();
               mAdapter.add(new SelfieItem(photoFile));
           }
        }

    }


    //Private helper method: Create an image file
    private File createImageFile() throws IOException {
        Log.i(TAG, "CREATE_IMAGE_FILE");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

}
