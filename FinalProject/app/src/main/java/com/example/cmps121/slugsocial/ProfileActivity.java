package com.example.cmps121.slugsocial;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmps121.slugsocial.data.model.UserInfo;
import com.example.cmps121.slugsocial.data.remote.SlugAPI;

import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String user_id = settings.getString("attending_user_id", null);

        SlugAPI.Factory.getInstance().getUserInfo(user_id).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Response<UserInfo> response) {
                if (response.code() == 500) {
                    Toast error = Toast.makeText(getApplicationContext(), "Error 500! Request Failure!", Toast.LENGTH_SHORT);
                    error.show();
                } else if (response.code() == 200) {
                    String http_response = response.body().getResponse();
                    if (http_response.equals("ok")) {

                        //Populate the Page
                        byte[] decodedString = Base64.decode(response.body().getProfilePicture(), Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        //Round image
                        decodedByte = getRoundedShape(decodedByte);

                        ImageView myImage = (ImageView) findViewById(R.id.imageView6);
                        //myImage.setImageBitmap(decodedByte);
                        myImage.setImageBitmap(decodedByte);

                        TextView title = (TextView) findViewById(R.id.textView9);
                        title.setText(response.body().getUsername());

                        title = (TextView) findViewById(R.id.txt_profile_name);
                        title.setText(response.body().getName());

                        title = (TextView) findViewById(R.id.textView11);
                        title.setText(response.body().getAge());

                        title = (TextView) findViewById(R.id.textView12);
                        title.setText(response.body().getBio());

                        Toast.makeText(getApplicationContext(), response.body().getName(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast error = Toast.makeText(getApplicationContext(), "Username or password are incorrect!", Toast.LENGTH_SHORT);
                        error.show();
                    }
                } else {
                    Toast error = Toast.makeText(getApplicationContext(), "Error! nok!", Toast.LENGTH_SHORT);
                    error.show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast error = Toast.makeText(getApplicationContext(), "Error! Request Failure! \nCheck your internet connection...", Toast.LENGTH_SHORT);
                error.show();
            }
        });
    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 1000;
        int targetHeight = 1000;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
}
