package com.example.felip.smartbanho.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.felip.smartbanho.Process.ScanIpAddressImpl;
import com.example.felip.smartbanho.R;
import com.github.ybq.android.spinkit.style.WanderingCubes;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchForDevices extends AppCompatActivity {


    private ScanIpAddressImpl scanIpAddress;
    private List<String> ipList;
    private String espIpAddress;
    private SharedPreferences sharedPreferences;
    public static final String ESP8266 = "esp8266";
    public static int RETRY = 3;
    private static int SPLASH_TIME_OUT = 4000;
    private String fixedUrl = "http://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ipList = new ArrayList<>();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_search_for_devices);

        //Use this for debugging to clear the SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(ESP8266, MODE_PRIVATE).edit();
        editor.putString("ip", null);
        editor.apply();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        WanderingCubes wanderingCubes = new WanderingCubes();
        progressBar.setIndeterminateDrawable(wanderingCubes);

        Log.d("searchForDevices Class", "Getting the ip from esp saved in the last session");
        sharedPreferencesRead();
        ScanIpAddressImpl scanIpAddress = new ScanIpAddressImpl(this);
        this.scanIpAddress = scanIpAddress;

        this.scanIpAddress.setSubnet();

        if (espIpAddress == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runAsyncTask();
                }
            }, SPLASH_TIME_OUT);
        } else {
            onFinishedScan();
        }

    }

    private void runAsyncTask() {
        new CheckHostsTask(this).execute();
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

    private void onFinishedScan() {

        if (espIpAddress == null) {
            Intent displayMessage = new Intent(SearchForDevices.this, DisplayMessageActivity.class);
            startActivity(displayMessage);
            finish();
        } else {
//            Intent showerIO = new Intent(SearchForDevices.this, ShowerIO.class);
//            startActivity(showerIO);
            Intent loginActivity = new Intent(SearchForDevices.this, LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }
    }

    private class CheckHostsTask extends AsyncTask<Void, String, String> {

        SearchForDevices searchForDevices;

        public CheckHostsTask(SearchForDevices searchForDevices) {
            super();
            this.searchForDevices = searchForDevices;
        }

        @Override
        protected void onPostExecute(String result) {
            if (searchForDevices.scanIpAddress.foundEspIp) {
                Log.d("searchForShowerIO Class", "Found a device, saving it on shared preferences");
                sharedPreferencesWrite(searchForDevices.scanIpAddress.espIpAddress);
                espIpAddress = searchForDevices.scanIpAddress.espIpAddress;
                onFinishedScan();
            } else {
                if (RETRY != 0) {
                    RETRY--;
                    new CheckHostsTask(searchForDevices).execute();
                } else {
                    onFinishedScan();
                }
            }
        }

        @Override
        protected String doInBackground(Void... records) {
            try {
                int timeout = 100;
                for (int i = 2; i < 255; i++) {
                    String host = "";
                    host = searchForDevices.scanIpAddress.subnet + "." + i;
                    fixedUrl = "http://";
                    if (InetAddress.getByName(host).isReachable(timeout)) {
                        Log.d("doInBackground()", host + " is reachable");
                        String esp8266RestUrl = "/check";
                        fixedUrl = fixedUrl + host + esp8266RestUrl;
                        OkHttpClient client = new OkHttpClient();

                        final Request request = new Request.Builder()
                                .url(fixedUrl)
                                .build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String resultIp = request.url().host();
                                searchForDevices.scanIpAddress.espIpAddress = resultIp;
                                Log.d("doInBackground()", "Found a responding device!");
                                searchForDevices.scanIpAddress.foundEspIp = true;
                            }
                        });

                    }
                    if (searchForDevices.scanIpAddress.foundEspIp) {
                        break;
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
}
