package com.example.cmps121.slugsocial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cmps121.slugsocial.data.model.AttendingInfo;
import com.example.cmps121.slugsocial.data.model.EventInfo;
import com.example.cmps121.slugsocial.data.remote.SlugAPI;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends Activity {

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView image;
    SharedPreferences settings;
    String event_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        image = (ImageView) findViewById(R.id.img_eventpicture);

    }

    public void createEvent(View v){

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String user_id  = settings.getString("user_id", null);

        Random rand = new Random();
        int  n = rand.nextInt(100000);
        event_id= Integer.toString(n);

        //Convert image to string and upload it
        Bitmap imageBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte [] b = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        String event_image = encodedImage;
        if(event_image == null){
            event_image = "noPicture" ;
        }

        // Now I'm going to pass it as description

        EditText et = (EditText) this.findViewById(R.id.txt_create_title);
        String title = et.getText().toString();

        et = (EditText) this.findViewById(R.id.txt_create_loc);
        String place = et.getText().toString();

        et = (EditText) this.findViewById(R.id.txt_create_desc);
        String description = et.getText().toString();

        et  = (EditText) this.findViewById(R.id.txt_create_date);
        String time = et.getText().toString();

        //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String event = settings.getString("eventType", null);


        SlugAPI.Factory.getInstance().setEvent(event_id,event_image,event,user_id,title,description,time,place).enqueue(new Callback<EventInfo>() {
            @Override
            public void onResponse(Response<EventInfo> response) {
                //response.body().setResult("ok");
                Toast saved = Toast.makeText(getApplicationContext(), "The Event was Created!", Toast.LENGTH_SHORT);
                saved.show();

                // Join as Hostess
                String user_id = settings.getString("user_id",null);
                String user_name = settings.getString("user_name",null);
                String user_image = settings.getString("user_image", null);

                SlugAPI.Factory.getInstance().setAttending(event_id,user_id,user_name,user_image).enqueue(new Callback<AttendingInfo>() {
                    @Override
                    public void onResponse(Response<AttendingInfo> response) {
                        if (response.body().getResponse().equals("ok")) {
                            Toast saved = Toast.makeText(getApplicationContext(), "Added as hostess!", Toast.LENGTH_SHORT);
                            saved.show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast error = Toast.makeText(getApplicationContext(), "Error! Post Failure! \nCheck your internet connection...", Toast.LENGTH_SHORT);
                        error.show();
                    }
                });
                //-----------------

            }

            @Override
            public void onFailure(Throwable t) {
                Toast error = Toast.makeText(getApplicationContext(), "Error! Post Failure! \nCheck your internet connection...", Toast.LENGTH_SHORT);
                error.show();
            }
        });
        finish();
    }

    public void loadPictureFromGallery(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            image.setImageURI(selectedImage);
        }
    }
}
