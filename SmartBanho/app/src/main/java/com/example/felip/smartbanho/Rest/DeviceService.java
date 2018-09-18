package com.example.felip.smartbanho.Rest;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.felip.smartbanho.Activities.Search.SearchForDevices;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DeviceService extends AsyncTask<Void, String, String> {

    SearchForDevices searchForDevices;
    public static int RETRY = 0;
    private String fixedUrl = "http://";
    private Gson gson;

    public DeviceService(SearchForDevices searchForDevices) {
        super();
        this.searchForDevices = searchForDevices;
        //Instanciating the gson build to serialize json from HTTP request
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
    }

    @Override
    protected void onPostExecute(String result) {
        if (RETRY != 0) {
            RETRY--;
            new DeviceService(this.searchForDevices).execute();
        } else {
            this.searchForDevices.onFinishedScan();
        }

    }

    @Override
    protected String doInBackground(Void... records) {
        try {
            int timeout = 100;
            for (int i = 2; i < 255; i++) {
                String host = "";
                host = this.searchForDevices.scanIpAddress.subnet + "." + i;
                fixedUrl = "http://";
                if (InetAddress.getByName(host).isReachable(timeout)) {
                    Log.d("doInBackground()", host + " is reachable");
                    String esp8266RestUrl = "/check";
                    fixedUrl = fixedUrl + host + esp8266RestUrl;

                    try {
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, fixedUrl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("doInBackground()", "restHttpRequest(): The HTTP request was done successfully");
                                        Log.d("doInBackground()", "Found a responding device!");
                                        ShowerDevice foundShower;
                                        foundShower = gson.fromJson(response.toString(), ShowerDevice.class);
                                        Boolean hasAlreadyFound = false;
                                        for (ShowerDevice shower : searchForDevices.showers) {
                                            if (foundShower.getIp().equals(shower.getIp())) {
                                                hasAlreadyFound = true;
                                            }
                                        }
                                        if (!hasAlreadyFound) {
                                            searchForDevices.showers.add(foundShower);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("doInBackground()", "restHttpRequest(): There was an error in the HTTP request");
                                Log.d("doInBackground()", "restHttpRequest(): Error: " + error.getMessage());


                            }
                        });

                        this.searchForDevices.requestQueue.add(stringRequest);

                    } catch (Exception e) {
                        Log.d("WaferRestService Class", "restHttpRequest(): Error: " + e.getMessage());
                        throw e;
                    }

                }
            }
            searchForDevices.scanIpAddress.scanComplete = true;

        } catch (UnknownHostException e) {
            Log.d("doInBackground()", " UnknownHostException e : " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("doInBackground()", "checkHosts() :: IOException e : " + e);
            e.printStackTrace();
        } finally {
            Log.d("checkHosts()", "All the ip Address where scanned!");
        }
        return "finished";
    }
}
