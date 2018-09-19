package com.example.felip.smartbanho.Process.Search;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.felip.smartbanho.Utils.SeekDevicesCallback;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.GsonBuilder;

import java.util.List;

public class PartialScan extends SeekDevices {


    public PartialScan(String subnet, List<ShowerDevice> devices, RequestQueue requestQueue, SeekDevicesCallback callback) {
        super(subnet, devices, requestQueue, callback);
    }

    @Override
    protected String doInBackground(Void... records) {
        for(final ShowerDevice device: showers){
            Log.d("doInBackground()", "scanning device with ip: " + device.getIp());
            String fixedUrl = "http://";
            fixedUrl = fixedUrl + device.getIp() + this.esp8266RestUrl;

            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, fixedUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("doInBackground()", "restHttpRequest(): The HTTP request was done successfully");
                                Log.d("doInBackground()", "Device is currently online");
                                int index = showers.indexOf(device);
                                showers.get(index).setStatus("ONLINE");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("doInBackground()", "restHttpRequest(): There was an error in the HTTP request");
                        Log.d("doInBackground()", "restHttpRequest(): Error: " + error.getMessage());
                        int index = showers.indexOf(device);
                        showers.get(index).setStatus("OFFLINE");

                    }
                });

                this.requestQueue.add(stringRequest);

            } catch (Exception e) {
                Log.d("WaferRestService Class", "restHttpRequest(): Error: " + e.getMessage());
                throw e;
            }
        }


        return "finished";
    }
}
