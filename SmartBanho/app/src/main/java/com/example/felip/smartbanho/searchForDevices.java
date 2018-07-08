package com.example.felip.smartbanho;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.felip.smartbanho.Process.ScanIpAddressImpl;
import com.github.ybq.android.spinkit.style.FadingCircle;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class searchForDevices extends AppCompatActivity {


    private ScanIpAddressImpl scanIpAddress;
    private List<String> ipList;
    private String espIpAddress;
    private SharedPreferences sharedPreferences;
    public static final String ESP8266 = "esp8266";
    public static int RETRY = 3;
    private static int SPLASH_TIME_OUT = 7000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ipList = new ArrayList<>();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_search_for_devices);

        SharedPreferences.Editor editor = getSharedPreferences(ESP8266, MODE_PRIVATE).edit();
        editor.putString("ip", null);
        editor.apply();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        Log.d("searchForDevices Class", "Getting the ip from esp saved in the last session");
        sharedPreferencesRead();

        if (espIpAddress == null) {
            searchForShowerIO();
        } else {
            Intent showerIO = new Intent(searchForDevices.this, ShowerIO.class);
            startActivity(showerIO);
            finish();
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
        scanIpAddress = new ScanIpAddressImpl(this);
        scanIpAddress.setSubnet();
        scanIpAddress.checkHosts(scanIpAddress.subnet);

        while (!scanIpAddress.scanComplete) {

        }
        ipList = scanIpAddress.ipAddresses;

      /*  // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

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
                                queue.stop();
                                onFinishedScan();
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

        }*/
    }

    private void onFinishedScan() {
        if (espIpAddress == null) {
            RETRY = RETRY - 1;
            if (RETRY != 0) {
                Log.d("searchForDevices Class", "Doing the process again to find the device");
                searchForShowerIO();
            } else {
                Intent displayMessage = new Intent(searchForDevices.this, DisplayMessageActivity.class);
                startActivity(displayMessage);
                finish();
            }
        } else {
            Intent showerIO = new Intent(searchForDevices.this, ShowerIO.class);
            startActivity(showerIO);
            finish();
        }
    }
}
