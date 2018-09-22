package com.example.felip.smartbanho.Process.Search;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.example.felip.smartbanho.Activities.Home.HomeActivity;
import com.example.felip.smartbanho.Activities.Search.SearchForDevices;
import com.example.felip.smartbanho.Utils.SeekDevicesCallback;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SeekDevices extends AsyncTask<Void, String, String> {

    public SeekDevicesCallback callback;
    public static int RETRY = 0;
    public String esp8266RestUrl = "/check";
    public Gson gson;
    public List<ShowerDevice> showers;
    public String subnet;
    public RequestQueue requestQueue;

    public SeekDevices(String subnet, List<ShowerDevice> devices, RequestQueue requestQueue, SeekDevicesCallback callback){
        super();
        this.subnet = subnet;
        this.requestQueue = requestQueue;
        this.callback = callback;
        this.showers = devices;
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.create();
    }


    @Override
    protected void onPostExecute(String result) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (RETRY != 0) {
                    RETRY--;
                    execute();
                } else {
                    callback.onServerCallback(true,showers);
                }
            }
        }, 6000);

    }

    @Override
    protected abstract String doInBackground(Void... records);


}
