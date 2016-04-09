package com.example.cmps121.slugsocial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cmps121.slugsocial.data.model.AttendingInfo;
import com.example.cmps121.slugsocial.data.model.ResultAttending;
import com.example.cmps121.slugsocial.data.remote.SlugAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends Activity {

    SharedPreferences settings;
    String eventTitle;
    String eventId;
    List<ListElement> attending_list = new ArrayList<ListElement>();
    ListView listView;
    Bitmap roundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        eventTitle = settings.getString("eventTitle", null);
        eventId = settings.getString("eventId", null);
        String eventImage = settings.getString("eventImage", null);
        String eventPlace = settings.getString("eventPlace", null);
        String eventDescription = settings.getString("eventDescription", null);
        String eventTime = settings.getString("eventTime", null);

        byte[] decodedString = Base64.decode(eventImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        //Round image
        roundImage = getRoundedShape(decodedByte);

        ImageView myImage = (ImageView) findViewById(R.id.img_eventpic_join);
        //myImage.setImageBitmap(decodedByte);
        myImage.setImageBitmap(roundImage);

        TextView title = (TextView) findViewById(R.id.txt_title_join);
        title.setText(eventTitle);

        title = (TextView) findViewById(R.id.txt_loc_join);
        title.setText(eventPlace);

        title = (TextView) findViewById(R.id.txt_desc_join);
        title.setText(eventDescription);

        title = (TextView) findViewById(R.id.txt_date_join);
        title.setText(eventTime);

        refreshAttending(findViewById(R.id.lst_attending));

    }

    public void join(View v){
        String user_id = settings.getString("user_id",null);
        String user_name = settings.getString("user_name",null);
        String user_image = settings.getString("user_image",null);

        SlugAPI.Factory.getInstance().setAttending(eventId,user_id,user_name,user_image).enqueue(new Callback<AttendingInfo>() {
            @Override
            public void onResponse(Response<AttendingInfo> response) {
                if (response.body().getResponse().equals("ok")){
                    Toast saved = Toast.makeText(getApplicationContext(), "The data was saved!", Toast.LENGTH_SHORT);
                    saved.show();
                    refreshAttending(findViewById(R.id.lst_attending));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast error = Toast.makeText(getApplicationContext(), "Error! Post Failure! \nCheck your internet connection...", Toast.LENGTH_SHORT);
                error.show();
            }
        });

    }

    public void refreshAttending(View v){
        SlugAPI.Factory.getInstance().getAttending(eventId).enqueue(new Callback<AttendingInfo>() {
            @Override
            public void onResponse(Response<AttendingInfo> response) {
                final List<ResultAttending> attending = response.body().getResultAttending();
                if (response.code() == 500) {
                    Toast error = Toast.makeText(getApplicationContext(), "Error 500! Request Failure!", Toast.LENGTH_SHORT);
                    error.show();
                } else if (response.code() == 200) {
                    String http_response = response.body().getResponse();
                    if (http_response.equals("ok")) {
                        attending_list.clear();
                        for (int i = 0; i < attending.size(); i++) {
                            // My image shit
                            byte[] decodedString = Base64.decode(attending.get(i).getUserImage(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            decodedByte = getRoundedShape(decodedByte);

                            attending_list.add(new ListElement(1, attending.get(i).getUserName(), attending.get(i).getUserId(),decodedByte));
                        }
                        listView = (ListView) findViewById(R.id.lst_attending);

                        AttendingAdapter eventsAdapter = new AttendingAdapter(JoinActivity.this, attending_list);
                        listView.setAdapter(eventsAdapter);

                        ListView eventView = (ListView) findViewById(R.id.lst_SingleEvent);
                        eventView = listView;
                        //When clicked on event goes to page of event
                        eventView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //When Clicked
                                String string = parent.getItemAtPosition(position).toString();
                                string = attending.get(position).getUserId();

                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("attending_user_id", string);
                                editor.commit();

                                Intent intent = new Intent(JoinActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                        });


                    } else {
                        Toast error = Toast.makeText(getApplicationContext(), "No events yet!", Toast.LENGTH_SHORT);
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

    public void goToChat(View v){
        //When clicked on event goes to page of event

        Intent intent = new Intent(JoinActivity.this, ChatActivity.class);
        startActivity(intent);

    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 500;
        int targetHeight = 500;
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



class AttendingAdapter extends ArrayAdapter<ListElement> {

    Context context;
    List<ListElement> titles;


    AttendingAdapter(Context c, List<ListElement> events){
        super(c, R.layout.singlelist, R.id.textView3, events);
        this.context = c;
        this.titles = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.singlelist, parent, false);

        ListElement w = getItem(position);


        ImageView myImage = (ImageView) row.findViewById(R.id.imageView2);
        TextView myText = (TextView) row.findViewById(R.id.textView3);
        TextView mySmallText = (TextView) row.findViewById(R.id.textView4);


        if(w.bit.equals("noImage")) {
            myImage.setImageResource(w.image);
        }
        else{
            myImage.setImageBitmap(w.bit);
        }
        myText.setText(w.title);
        mySmallText.setText(w.details);

        return row;
    }
}
