package com.task.phone.loyltytask;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by adhiraj on 18/9/15.
 */
public class APIManager implements IAsyncCallback{

    private int timeout = 15000;
    private static APIManager manager;
    String baseUrl;
    private String lat,lng;

    private APIManager(){}


    public static APIManager getInstance(){
        if (manager == null){
            manager=new APIManager();
        }
        return manager;
    }

    public void sendAsyncCall(String requestMethod, IAsyncCallback callback,Location location){

        AsyncCallHandler handler = new AsyncCallHandler(requestMethod,callback,location);
        handler.execute();
    }

    @Override
    public void onSuccessResponse(String successResponse) {

    }

    @Override
    public void onErrorResponse(String errorResponse) {

    }


    class AsyncCallHandler extends AsyncTask<Void,Void,String> {
        private String requestMethod;
        private IAsyncCallback callback;
        private Location location;
        private boolean initialized = false;
        private String reply = null;


        private int responseCode = 0;

        public AsyncCallHandler(String requestMethod, IAsyncCallback callback,Location location){

            this.requestMethod = requestMethod;
            this.callback = callback;
            this.location = location;
            initialized = true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {
            if(!initialized){
                return null;
            }
            try {
                sendJSONRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            callback.onSuccessResponse(reply);

        }


        private void sendJSONRequest() throws Exception {

            lat = String.valueOf(location.getLatitude());
            lng = String.valueOf(location.getLongitude());

            baseUrl = "https://api.maxgetmore.com/V5/Corporate/GetStoresByLocation?Latitude="+lat+"&Longitude="+lng+"&PageNo=1&PageSize=5";

            URL obj = new URL(baseUrl);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setUseCaches(false);
            connection.setAllowUserInteraction(false);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            connection.setRequestMethod(requestMethod);

            connection.connect();

            responseCode = connection.getResponseCode();

            BufferedReader bufferedReader = null;

            InputStream errorStream = connection.getErrorStream();

            if(errorStream==null){
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream));
            }else{
                bufferedReader = new BufferedReader(
                        new InputStreamReader(errorStream));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            if(response!=null){
                reply = response.toString();
            }
            return;

        }

    }
}
