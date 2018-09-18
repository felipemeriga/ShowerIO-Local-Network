package com.example.felip.smartbanho.Process.Search;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.felip.smartbanho.Process.subnet.SeekDevicesCallback;
import com.example.felip.smartbanho.model.ShowerDevice;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class FullScan extends SeekDevices {

    public FullScan(String subnet, RequestQueue requestQueue, SeekDevicesCallback callback) {
        this.subnet = subnet;
        requestQueue = requestQueue;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Void... records) {
        try {
            int timeout = 100;
            for (int i = 2; i < 255; i++) {
                String host = "";
                host = this.subnet + "." + i;
                String fixedUrl = "http://";
                if (InetAddress.getByName(host).isReachable(timeout)) {
                    Log.d("doInBackground()", host + " is reachable");
                    fixedUrl = fixedUrl + host + this.esp8266RestUrl;

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
                                        for (ShowerDevice shower : showers) {
                                            if (foundShower.getIp().equals(shower.getIp())) {
                                                hasAlreadyFound = true;
                                            }
                                        }
                                        if (!hasAlreadyFound) {
                                            showers.add(foundShower);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("doInBackground()", "restHttpRequest(): There was an error in the HTTP request");
                                Log.d("doInBackground()", "restHttpRequest(): Error: " + error.getMessage());


                            }
                        });

                        this.requestQueue.add(stringRequest);

                    } catch (Exception e) {
                        Log.d("WaferRestService Class", "restHttpRequest(): Error: " + e.getMessage());
                        throw e;
                    }
                }
            }
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
