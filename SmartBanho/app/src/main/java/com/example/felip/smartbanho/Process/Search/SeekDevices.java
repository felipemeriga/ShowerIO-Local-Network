package com.example.felip.smartbanho.Process.Search;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.example.felip.smartbanho.Utils.SeekDevicesCallback;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        if (RETRY != 0) {
            RETRY--;
            this.execute();
        } else {
           callback.onServerCallback(true,this.showers);
        }
    }

    @Override
    protected abstract String doInBackground(Void... records);


}
