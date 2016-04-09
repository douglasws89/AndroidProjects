package com.example.cmps121.slugsocial;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class MyChatAdapter extends ArrayAdapter<SingleRow> {

    int resource;
    Context context;

    public MyChatAdapter(Context _context, int _resource, List<SingleRow> items) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;

        SingleRow w = getItem(position);

        // Inflate a new view if necessary.
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource,  newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }

        // Fills in the view.
        TextView tv = (TextView) newView.findViewById(R.id.itemText);
        tv.setText(w.message);


        TextView tv1 = (TextView) newView.findViewById(R.id.itemText1);
        tv1.setText(w.nickname);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String user_id = settings.getString("user_id", null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);

        if(w.user_id.equals(user_id)){
            tv.setBackgroundResource(R.drawable.rounded_blue);
            params.weight = 1.0f;
            params.gravity = Gravity.RIGHT;
            tv.setLayoutParams(params);
            tv1.setLayoutParams(params);
        }else{
            tv.setBackgroundResource(R.drawable.rounded_white);
            params.weight = 1.0f;
            params.gravity = Gravity.LEFT;
            tv.setLayoutParams(params);
            tv1.setLayoutParams(params);
        }

        return newView;
    }
}
