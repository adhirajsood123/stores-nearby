package com.task.phone.loyltytask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Home extends Activity implements LocationListener,IAsyncCallback {
    //The minimum distance to change updates in metters
    private static final long GPS_MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 metters
    //The minimum time beetwen updates in milliseconds
    private static final long GPS_MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    Location location;
    ListView lv;
    JSONObject data;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lv = (ListView) findViewById(R.id.listStores);

        location = getLocation(this,this);
        if (location!=null){
            APIManager.getInstance().sendAsyncCall("GET", Home.this,location);

        }else {
            Toast.makeText(this, "Location not enabled!", Toast.LENGTH_SHORT).show();
            return;
        }


    }




    public static Location getLocation(Context mContext, LocationListener listener)
    {
        LocationManager locationManager;
        Location location = null;

        try
        {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if(locationManager==null){
                return null;

            }

            //getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled)
            {
                // no network provider is enabled

                return null;
            }
            else
            {
                if ( isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            GPS_MIN_TIME_BW_UPDATES,
                            GPS_MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);

                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                if (isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                GPS_MIN_TIME_BW_UPDATES,
                                GPS_MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
                        if (locationManager != null)
                        {
                            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onSuccessResponse(String successResponse) {
        if(successResponse!=null) {
            try {
                JSONObject jsonObj = new JSONObject(successResponse);
                int responseStatus = jsonObj.getInt("Succeeded");
                if (responseStatus == 1) {

                    data = jsonObj.getJSONObject("Data");
                    if (data.has("Stores")) {

                        CustomListViewStores adapter = new CustomListViewStores(Home.this, data.getJSONArray("Stores"));
                        lv.setAdapter(adapter);
                        setClick();
                    }
                } else {
                    Toast.makeText(Home.this, "Stores not found!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(Home.this,"Not able to fetch stores!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorResponse( String errorResponse) {

    }

    public void setClick(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    JSONObject storeDetails = data.getJSONArray("Stores").getJSONObject(position);

                    Intent intent = new Intent(Home.this,StoreDetails.class);
                    intent.putExtra("Store",storeDetails.toString());
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
