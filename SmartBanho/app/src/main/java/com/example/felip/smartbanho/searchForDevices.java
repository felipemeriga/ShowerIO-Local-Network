package com.example.felip.smartbanho;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.felip.smartbanho.Process.ScanIpAddress;
import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class searchForDevices extends AppCompatActivity {


    private ScanIpAddress scanIpAddress;
    private List<String> ipList;
    private String espIpAddress;
    private SharedPreferences sharedPreferences;
    public static final String ESP8266 = "esp8266";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ipList = new ArrayList<>();
        setContentView(R.layout.activity_search_for_devices);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        Log.d("searchForDevices Class", "Getting the ip from esp saved in the last session");
        sharedPreferencesRead();

        if (espIpAddress == null) {
            searchForShowerIO();
        }


    }

    private void sharedPreferencesWrite(String espIpAddress) {
        SharedPreferences.Editor editor = getSharedPreferences(ESP8266, MODE_PRIVATE).edit();
        editor.putString("ip", espIpAddress);
        editor.apply();
    }

    private void sharedPreferencesRead() {

        sharedPreferences = getSharedPreferences(ESP8266, MODE_PRIVATE);
        espIpAddress = sharedPreferences.getString("ip", null);

    }

    private void searchForShowerIO() {

        Log.d("searchForDevices Class", "Initializing scanIpAddress class");
        scanIpAddress = new ScanIpAddress(this);
        scanIpAddress.setSubnet();
        scanIpAddress.checkHosts(scanIpAddress.subnet);

        while (!scanIpAddress.scanComplete) {

        }
        ipList = scanIpAddress.ipAddresses;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        for (final String ip : ipList) {
            String url = "http://" + ip + "/check";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("showerIO")) {
                                Log.d("searchForDevices Class", "Found the correspondent device!");
                                espIpAddress = ip;
                                sharedPreferencesWrite(ip);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("searchForDevices Class", "Error in doind the request");
                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }
}
