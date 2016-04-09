package com.example.cmps121.slugsocial;

import android.app.Activity;
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

import com.example.cmps121.slugsocial.R;
import com.example.cmps121.slugsocial.data.model.MessageInfo;
import com.example.cmps121.slugsocial.data.model.ResultMessage;
import com.example.cmps121.slugsocial.data.remote.SlugAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends Activity {

    SharedPreferences settings;
    String user_id;

    private MyChatAdapter aa;

    private ArrayList<SingleRow> aList;
    String eventId;

    //private List<ResultList> messagesPlustLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        // Sared Preferences
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        eventId = settings.getString("eventId", null);

        //refresh(findViewById(R.id.textView3));
        refresh(findViewById(R.id.scrollViewChat));

        //Setting fontawesome
        //Typeface fontFamily = Typeface.createFromAsset(getAssets(),"font/fontawesome.ttf");
        ImageButton refresh = (ImageButton) findViewById(R.id.buttonChatRefresh);
        //refresh.setTypeface(fontFamily);
        //refresh.setText("\uf021");
    }

    @Override
    protected void onResume() {
        aList = new ArrayList<SingleRow>();
        aa = new MyChatAdapter(this, R.layout.chat_list, aList);
        ListView myListView = (ListView) findViewById(R.id.scrollViewChat);
        myListView.setAdapter(aa);
        aa.notifyDataSetChanged();
        super.onResume();
    }

    public void displayText(String s) {
//        TextView tv = (TextView) findViewById(R.id.textView3);
//        tv.setText(s);
    }

    public void refresh(View v){

        //Get data to pass
        eventId = settings.getString("eventId", null);

        SlugAPI.Factory.getInstance().getChat(eventId).enqueue(new Callback<MessageInfo>() {
            @Override
            public void onResponse(Response<MessageInfo> response) {
                List<ResultMessage> messages = response.body().getResultMessages();
                //messagesPlustLast = messages;
                if (response.code() == 500) {
                    Toast error = Toast.makeText(getApplicationContext(), "Error 500! Request Failure!", Toast.LENGTH_SHORT);
                    error.show();
                } else if (response.code() == 200) {
                    String http_response = response.body().getResponse();
                    if (http_response.equals("ok")) {
                        if (messages.size() == 0) {
                            displayText("No messages...");
                        } else {
                            aList.clear();
                            for (int i = messages.size() - 1; i >= 0; i--) {
                                aList.add(new SingleRow(messages.get(i).getMessage(), messages.get(i).getUserName(),messages.get(i).getUserId(),R.drawable.rounded_blue));
                            }
                            final ListView list = (ListView)findViewById(R.id.scrollViewChat);
                            aa = new MyChatAdapter(list.getContext(), R.layout.chat_list, aList);
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
        String nickname = "";
        String message = "";
        String message_id = "938626";
        final View z = v;

        // Sets the name
        user_id = settings.getString("user_id", null);
        String user_name = settings.getString("user_name", null);

        Random rand = new Random();
        int  n = rand.nextInt(1000000);
        message_id = Integer.toString(n);

        // Sets the message
        EditText et = (EditText) this.findViewById(R.id.editTextChat);
        message = et.getText().toString();
        et.setText("");

        String event_id = settings.getString("eventId", null);


        //Posts message on list before sending it to the server
        aList.add(new SingleRow(message, "sending...",user_id,R.drawable.rounded_blue));
        final ListView list = (ListView)findViewById(R.id.scrollViewChat);
        aa = new MyChatAdapter(list.getContext(), R.layout.chat_list, aList);
        list.setAdapter(aa);
        aa.notifyDataSetChanged();
        // End of modification--------------------------------

        SlugAPI.Factory.getInstance().setChat( user_id, user_name, message, message_id, event_id).enqueue(new Callback<MessageInfo>() {
            @Override
            public void onResponse(Response<MessageInfo> response) {
                //response.body().setResult("ok");
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
