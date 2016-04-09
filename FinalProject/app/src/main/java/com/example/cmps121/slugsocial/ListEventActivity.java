package com.example.cmps121.slugsocial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListEventActivity extends Activity {

    ListView listView;
    String[] events;
    int[] images = {R.drawable.allevents, R.drawable.bars, R.drawable.clubs, R.drawable.food, R.drawable.outdoors, R.drawable.games, R.drawable.ride, R.drawable.other};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listevent);

        Resources res = getResources();
        events = res.getStringArray(R.array.events);

        listView = (ListView) findViewById(R.id.lst_listEvent);

        EventsAdapter eventsAdapter = new EventsAdapter(this, events, images);
        listView.setAdapter(eventsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = (String) parent.getItemAtPosition(position);

                // Shared preferences -- Possible problem with context
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("eventType", string);
                editor.commit();

                Intent intent = new Intent(ListEventActivity.this, SingleEventActivity.class);
                startActivity(intent);

                Toast saved = Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT);
                saved.show();
            }
        });

    }
}

class EventsAdapter extends ArrayAdapter<String>{

    Context context;
    int[] images;
    String[] titles;

    EventsAdapter(Context c, String[] events, int images[]){
        super(c, R.layout.listevents, R.id.txt_title_allevents, events);
        this.context = c;
        this.images = images;
        this.titles = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.listevents, parent, false);

        ImageView myImage = (ImageView) row.findViewById(R.id.img_eventbackground);
        TextView myText = (TextView) row.findViewById(R.id.txt_title_allevents);

        myImage.setImageResource(images[position]);
        myText.setText(titles[position]);

        // custom font
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/abc_bold.ttf");
        myText.setTypeface(custom_font);

        return row;
    }
}
