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

import com.example.cmps121.slugsocial.data.model.EventInfo;
import com.example.cmps121.slugsocial.data.model.ResultList;
import com.example.cmps121.slugsocial.data.remote.SlugAPI;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class SingleEventActivity extends Activity {


    ListView listView;
    List<ListElement> event_list = new ArrayList<ListElement>();
    List<String> event_info = new ArrayList<String>();
    String eventType;
    int[] images = {R.drawable.allevents, R.drawable.bars, R.drawable.allevents,R.drawable.allevents, R.drawable.bars, R.drawable.allevents, R.drawable.bars,  R.drawable.clubs, R.drawable.food, R.drawable.bars, R.drawable.clubs, R.drawable.food, R.drawable.outdoors, R.drawable.games, R.drawable.ride, R.drawable.other,R.drawable.clubs,};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        // Shared prefferences - maybe not here
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        eventType = settings.getString("eventType", null);

        refresh(findViewById(R.id.lst_SingleEvent));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    protected void onResume() {
        //Adapter eventsAdapter = new Adapter(SingleEventActivity.this, event_list, images);
        //listView.setAdapter(eventsAdapter);
        refresh(findViewById(R.id.lst_SingleEvent));
        super.onResume();
    }

    public void refresh(View v) {
        //--------- Get data Code -----------
        SlugAPI.Factory.getInstance().getEvent(eventType).enqueue(new Callback<EventInfo>() {
            @Override
            public void onResponse(Response<EventInfo> response) {
                final List<ResultList> events = response.body().getResultList();
                if (response.code() == 500) {
                    Toast error = Toast.makeText(getApplicationContext(), "Error 500! Request Failure!", Toast.LENGTH_SHORT);
                    error.show();
                } else if (response.code() == 200) {
                    String http_response = response.body().getResponse();
                    if (http_response.equals("ok")) {
                        event_list.clear();
                        for (int i = 0; i < events.size(); i++) {
                            // My image shit
                            byte[] decodedString = Base64.decode(events.get(i).getEventImage(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            //Round image
                            decodedByte = getRoundedShape(decodedByte);

                            String details = "Hosted by: " + events.get(i).getUserId() + "\nLocation: " + events.get(i).getPlace() + "\nDate: " + events.get(i).getTime();
                            event_list.add(new ListElement(images[i], events.get(i).getTitle(), details, decodedByte));
                            event_info.add("Hosted by: " + events.get(i).getUserId() + ", Location: " + events.get(i).getPlace() + ", Date: " + events.get(i).getTime());
                        }
//                        Toast saved = Toast.makeText(getApplicationContext(), eventType, Toast.LENGTH_SHORT);
//                        saved.show();

                        listView = (ListView) findViewById(R.id.lst_SingleEvent);

                        Adapter eventsAdapter = new Adapter(SingleEventActivity.this, event_list, images);
                        listView.setAdapter(eventsAdapter);

                        ListView eventView = (ListView) findViewById(R.id.lst_SingleEvent);
                        //When clicked on event goes to page of event
                        eventView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String string = parent.getItemAtPosition(position).toString();
                                string = events.get(position).getTitle();
                                // Shared preferences -- Possible problem with context
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("eventTitle", string);
                                editor.putString("eventId", events.get(position).getEventId());
                                editor.putString("eventImage", events.get(position).getEventImage());
                                editor.putString("eventPlace", events.get(position).getPlace());
                                editor.putString("eventDescription", events.get(position).getDescription());
                                editor.putString("eventTime", events.get(position).getTime());
                                editor.commit();

                                Intent intent = new Intent(SingleEventActivity.this, JoinActivity.class);
                                startActivity(intent);

                                Toast saved = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT);
                                saved.show();
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
        //--------- End Get Data Code -------
    }

    public void newEvent(View v) {
        Intent intent = new Intent(this, CreateActivity.class);
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


class Adapter extends ArrayAdapter<ListElement>{

    Context context;
    int[] images;
    List<ListElement> titles;


    Adapter(Context c, List<ListElement> events, int images[]){
        super(c, R.layout.singlelist, R.id.textView3, events);
        this.context = c;
        this.images = images;
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


        //myImage.setImageResource(images[position]);
        //myImage.setImageResource(w.image);
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