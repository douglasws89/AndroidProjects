package com.example.cmps121.slugsocial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cmps121.slugsocial.data.model.UserInfo;
import com.example.cmps121.slugsocial.data.remote.SlugAPI;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends Activity {

    private static final int RESULT_LOAD_IMAGE = 1;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        image = (ImageView) findViewById(R.id.img_profilepic);

    }

    public void register_user(View v){

        Random rand = new Random();
        int  n = rand.nextInt(100000);
        String user_id = Integer.toString(n);

        EditText et = (EditText) this.findViewById(R.id.et_Name);
        String name = et.getText().toString();

        et = (EditText) this.findViewById(R.id.et_Age);
        String age = et.getText().toString();

        et = (EditText) this.findViewById(R.id.et_Username2);
        String username = et.getText().toString();

        et = (EditText) this.findViewById(R.id.et_Password2);
        String password = et.getText().toString();

        et = (EditText) this.findViewById(R.id.et_Retype);
        String re_password = et.getText().toString();

        et = (EditText) this.findViewById(R.id.et_Bio);
        String bio = et.getText().toString();

        //Convert image to string and upload it
        Bitmap imageBitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte [] b = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        String profile_picture = encodedImage;

        if(password.equals(re_password)) {

            SlugAPI.Factory.getInstance().setInfo(user_id, profile_picture, name, age, bio, username, password).enqueue(new Callback<UserInfo>() {
                @Override
                public void onResponse(Response<UserInfo> response) {
                    //response.body().setResult("ok");
                    Toast saved = Toast.makeText(getApplicationContext(), "The data was saved!", Toast.LENGTH_SHORT);
                    saved.show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast error = Toast.makeText(getApplicationContext(), "Error! Post Failure! \nCheck your internet connection...", Toast.LENGTH_SHORT);
                    error.show();
                }
            });
            finish();
        }
        else{
            Toast error = Toast.makeText(getApplicationContext(), "Password doesn't match.\nPlease re-type password!", Toast.LENGTH_SHORT);
            error.show();
        }
    }
    public void loadPictureFromGallery(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
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
