package com.task.phone.loyltytask;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by adhiraj on 18/9/15.
 */
public class CustomListViewStores extends ArrayAdapter<String> {

    private JSONArray jsonArray;
    LayoutInflater inflater;

        public CustomListViewStores(Activity context, JSONArray jsonArray) {
            super(context, R.layout.activity_list_row);
            this.jsonArray = jsonArray;
            this.inflater = context.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return this.jsonArray.length();
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {

            view = inflater.inflate(R.layout.activity_list_row, null);

            TextView txtStoreName = (TextView) view.findViewById(R.id.txtStoreName);
            ImageView logoStore = (ImageView) view.findViewById(R.id.logoStore);
            TextView distanceStore = (TextView) view.findViewById(R.id.distanceStore);
            TextView city = (TextView) view.findViewById(R.id.city);

            try {

            Object str = jsonArray.get(position);
            JSONObject jobj = new JSONObject(str.toString());

            String brandName = jobj.getString("BrandName");
            Double distance = jobj.getDouble("Distance");

            String nearbyCity = jobj.getString("City");
            String logoURL = jobj.getString("BrandURL");

            Picasso.with(getContext())
            .load(logoURL)
            .placeholder(R.drawable.loylty_logo)
            .error(R.drawable.loylty_logo)
            .into(logoStore);


            txtStoreName.setText(brandName);
            distanceStore.setText(distance+" Km away from you.");
            city.setText(nearbyCity);

            } catch (JSONException e) {
            e.printStackTrace();

            }


            return view;
        }
}
