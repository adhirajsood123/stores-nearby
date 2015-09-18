package com.task.phone.loyltytask;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by adhiraj on 18/9/15.
 */
public class StoreDetails extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);

        Intent intent = getIntent();
        String storeDetails = intent.getStringExtra("Store");
        displayStoreDetails(storeDetails);
    }

    private void displayStoreDetails(String storeDetails) {
        try {
            JSONObject jsonObj = new JSONObject(storeDetails);
            ImageView brandLogo = (ImageView) findViewById(R.id.brandLogo);
            TextView brandName = (TextView) findViewById(R.id.brandName);
            TextView brandAddress = (TextView) findViewById(R.id.brandAddress);
            TextView brandCityState = (TextView) findViewById(R.id.brandCityState);
            TextView brandNumber = (TextView) findViewById(R.id.brandNumber);
            String logoURL = jsonObj.getString("BrandURL");
            String address = jsonObj.getString("Address1")+" "+jsonObj.getString("Address2");
            String cityState = jsonObj.getString("City")+" "+jsonObj.getString("State")+" "+jsonObj.getString("Pin");
            final String phone = jsonObj.getString("ContactNo");
            brandName.setText(jsonObj.getString("BrandName"));
            brandAddress.setText(address);
            brandCityState.setText(cityState);
            brandNumber.setText(phone);

            brandNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+" + phone.trim()));
                    startActivity(intent);
                }
            });
            Picasso.with(StoreDetails.this)
                    .load(logoURL)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(brandLogo);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
