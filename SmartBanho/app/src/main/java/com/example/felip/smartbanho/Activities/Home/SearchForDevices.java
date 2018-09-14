package com.example.felip.smartbanho.Activities.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.felip.smartbanho.Activities.Error.DisplayMessageActivity;
import com.example.felip.smartbanho.Activities.LoginActivity;
import com.example.felip.smartbanho.Activities.ShowerIO.ShowerListActivity;
import com.example.felip.smartbanho.Process.ScanIpAddressImpl;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.Rest.DeviceService;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.gson.Gson;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;


public class SearchForDevices extends AppCompatActivity {


    public ScanIpAddressImpl scanIpAddress;
    private List<String> ipList;
    private String espIpAddress;
    private SharedPreferences sharedPreferences;
    public static final String ESP8266 = "esp8266";
    public static int RETRY = 0;
    private static int SPLASH_TIME_OUT = 4000;
    private String fixedUrl = "http://";
    private Gson gson;
    public List<ShowerDevice> showers;
    public RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ipList = new ArrayList<>();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_search_for_devices);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        WanderingCubes wanderingCubes = new WanderingCubes();
        progressBar.setIndeterminateDrawable(wanderingCubes);

        Log.d("searchForDevices Class", "Getting the ip from esp saved in the last session");
//        sharedPreferencesRead();
        ScanIpAddressImpl scanIpAddress = new ScanIpAddressImpl(this);
        this.scanIpAddress = scanIpAddress;

        this.scanIpAddress.setSubnet();

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        showers = new ArrayList<>();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runAsyncTask();
            }
        }, SPLASH_TIME_OUT);


    }

    private void runAsyncTask() {
        new DeviceService(this).execute();
    }


    public void onFinishedScan() {

        if (showers.size() == 0) {
            Intent displayMessage = new Intent(SearchForDevices.this, DisplayMessageActivity.class);
            startActivity(displayMessage);
            finish();
        } else {
            Intent showerListActivity = new Intent(SearchForDevices.this, ShowerListActivity.class);
            //Serializing the object to json, to pass between the activities
            String showerArrayAsString = new Gson().toJson(showers);
            showerListActivity.putExtra("showerDevices", showerArrayAsString);
            startActivity(showerListActivity);
            finish();

        }
    }
}
