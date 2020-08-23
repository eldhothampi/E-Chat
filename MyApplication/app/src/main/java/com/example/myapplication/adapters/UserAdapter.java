package com.example.myapplication.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.UserData;

import java.util.ArrayList;

public class UserAdapter extends ArrayAdapter<UserData> {
        public UserAdapter(Context context, ArrayList<UserData> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position

            UserData user = getItem(position);
            Log.e("User Data",user.name);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_row, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.userid);
            TextView tvHome = (TextView) convertView.findViewById(R.id.username);
            // Populate the data into the template view using the data object
            tvName.setText(user.name);
            tvHome.setText(user.id);
            // Return the completed view to render on screen
            return convertView;
        }

}
