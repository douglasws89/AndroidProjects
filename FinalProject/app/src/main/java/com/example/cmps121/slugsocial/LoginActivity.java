package com.example.cmps121.slugsocial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cmps121.slugsocial.data.model.UserInfo;
import com.example.cmps121.slugsocial.data.remote.SlugAPI;

import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View v){

        EditText et = (EditText) this.findViewById(R.id.et_Username);
        String username = et.getText().toString();

        et = (EditText) this.findViewById(R.id.et_Password);
        String password = et.getText().toString();

        SlugAPI.Factory.getInstance().getInfo(username, password).enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Response<UserInfo> response) {
                if (response.code() == 500) {
                    Toast error = Toast.makeText(getApplicationContext(), "Error 500! Request Failure!", Toast.LENGTH_SHORT);
                    error.show();
                } else if (response.code() == 200) {
                    String http_response = response.body().getResponse();
                    if (http_response.equals("ok")) {
                        String nome = response.body().getName();
                        String user_id = response.body().getUserId();

//                        Toast saved = Toast.makeText(getApplicationContext(), "Hello " + nome + user_id, Toast.LENGTH_SHORT);
//                        saved.show();

                        // Shared preferences -- Possible problem with context Here specially!!
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("user_id", user_id);
                        editor.putString("user_name",response.body().getName());
                        editor.putString("user_image", response.body().getProfilePicture());
                        editor.apply();

                        goToApp(findViewById(R.id.btn_Login));
                    }
                    else{
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

    public void register(View v){
        changeSetting(findViewById(R.id.txt_Register));
    }

    // Switches to another activity
    public void changeSetting(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        //finish();
    }

    public void goToApp(View v){
        Intent intent = new Intent(this, ListEventActivity.class);
        startActivity(intent);
    }
}
