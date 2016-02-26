package com.example.cmps121.hw3;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.*;
import android.graphics.*;
import android.content.*;
import android.view.*;


import com.example.cmps121.hw3.data.model.Chat;
import com.example.cmps121.hw3.data.model.ResultList;
import com.example.cmps121.hw3.data.remote.ChatAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Main2Activity extends AppCompatActivity {

    SharedPreferences settings;
    private LocationData locationData = LocationData.getLocationData();
    String user_id;

    private MyAdapter aa;

    private ArrayList<SingleRow> aList;

    //private List<ResultList> messagesPlustLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Sared Preferences
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        // Shared Preferences
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        user_id = settings.getString("user_id", null);

        displayText(user_id);
        //refresh(findViewById(R.id.textView3));
        refresh(findViewById(R.id.scrollView));

        //Setting fontawesome
        Typeface fontFamily = Typeface.createFromAsset(getAssets(),"font/fontawesome.ttf");
        Button refresh = (Button) findViewById(R.id.button3);
        refresh.setTypeface(fontFamily);
        refresh.setText("\uf021");
    }

    @Override
    protected void onResume() {
        aList = new ArrayList<SingleRow>();
        aa = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.scrollView);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
        super.onResume();
    }

    public void displayText(String s) {
//        TextView tv = (TextView) findViewById(R.id.textView3);
//        tv.setText(s);
    }

    public void refresh(View v){
        float lat = (float)37.422005;
        float lon = (float) -122.08409;
        String id = "31";

        // Commented this to help out the guy
        lat = (float) locationData.getLocation().getLatitude();
        lon = (float) locationData.getLocation().getLongitude();

        ChatAPI.Factory.getInstance().getChat(lat, lon, user_id).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Response<Chat> response) {
                List<ResultList> messages = response.body().getResultList();
                //messagesPlustLast = messages;
                if (response.code() == 500) {
                    Toast error = Toast.makeText(getApplicationContext(), "Error 500! Request Failure!", Toast.LENGTH_SHORT);
                    error.show();
                } else if (response.code() == 200) {
                    String http_response = response.body().getResult();
                    if (http_response.equals("ok")) {
                        if (messages.size() == 0) {
                            displayText("No messages...");
                        } else {
                            aList.clear();
                            for (int i = messages.size() - 1; i >= 0; i--) {
                                aList.add(new SingleRow(messages.get(i).getMessage(), messages.get(i).getNickname(),messages.get(i).getUserId(),R.drawable.speech_bold_square_left_filled));
                            }
                            final ListView list = (ListView)findViewById(R.id.scrollView);
                            aa = new MyAdapter(list.getContext(), R.layout.list_element, aList);
                            list.setAdapter(aa);
                            aa.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast error = Toast.makeText(getApplicationContext(), "Error! nok!", Toast.LENGTH_SHORT);
                    error.show();                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast error = Toast.makeText(getApplicationContext(), "Error! Request Failure! \nCheck your internet connection...", Toast.LENGTH_SHORT);
                error.show();
            }
        });
    }

    public void send(View v){
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        float lat = (float)37.422005;
        float lon = (float) -122.08409;
        String nickname = "";
        String message = "";
        String message_id = "938626";
        final View z = v;

        // Sets the name
        nickname = settings.getString("texto", null);
        user_id = settings.getString("user_id", null);

        Random rand = new Random();
        int  n = rand.nextInt(1000000);
        message_id = Integer.toString(n);

        //Commented this to help out the guy
        lat = (float) locationData.getLocation().getLatitude();
        lon = (float) locationData.getLocation().getLongitude();

        // Sets the message
        EditText et = (EditText) this.findViewById(R.id.editText2);
        message = et.getText().toString();
        et.setText("");

        //Posts message on list before sending it to the server
        aList.add(new SingleRow(message, "sending...",user_id,R.drawable.speech_bold_square_left_filled));
        final ListView list = (ListView)findViewById(R.id.scrollView);
        aa = new MyAdapter(list.getContext(), R.layout.list_element, aList);
        list.setAdapter(aa);
        aa.notifyDataSetChanged();
        // End of modification--------------------------------

        ChatAPI.Factory.getInstance().setChat(lat,lon,user_id,nickname,message,message_id).enqueue(new Callback<Chat>() {
            @Override
            public void onResponse(Response<Chat> response) {
                response.body().setResult("ok");
                refresh(z);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast error = Toast.makeText(getApplicationContext(), "Error! Post Failure! \nCheck your internet connection...", Toast.LENGTH_SHORT);
                error.show();
            }
        });
    }
}