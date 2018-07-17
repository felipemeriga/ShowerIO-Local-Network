package com.example.felip.smartbanho;

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
    private static int SPLASH_TIME_OUT = 4000;
    private String fixedUrl = "http://";

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

////        Log.d("searchForDevices Class", "Getting the ip from esp saved in the last session");
////        sharedPreferencesRead();
//        searchForShowerIO();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchForShowerIO();
            }
        }, SPLASH_TIME_OUT);

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
        Boolean lockScan = true;

        if (RETRY == 3) {
            Log.d("searchForDevices Class", "Initializing scanIpAddress class");

            scanIpAddress = new ScanIpAddressImpl(this);
            scanIpAddress.setSubnet();
            scanIpAddress.scanComplete = false;
            new CheckHostsTask().execute();


        } else {
            scanIpAddress.scanComplete = false;
            new CheckHostsTask().execute();
        }

//        while (lockScan) {
//            if (scanIpAddress.foundEspIp) {
//                Log.d("searchForShowerIO Class", "Found a device, saving it on shared preferences");
//                sharedPreferencesWrite(scanIpAddress.espIpAddress);
//                this.espIpAddress = scanIpAddress.espIpAddress;
//                lockScan = false;
//                onFinishedScan();
//            } else {
//                if (scanIpAddress.scanComplete) {
//                    if (RETRY != 0) {
//                        RETRY--;
//                        searchForShowerIO();
//                    } else {
//                        lockScan = false;
//                        onFinishedScan();
//                    }
//                }
//            }
//        }

    }

    private void onFinishedScan() {
        if (espIpAddress == null) {
            Intent displayMessage = new Intent(searchForDevices.this, DisplayMessageActivity.class);
            startActivity(displayMessage);
            finish();
        } else {
            Intent showerIO = new Intent(searchForDevices.this, ShowerIO.class);
            startActivity(showerIO);
            finish();
        }
    }

    private class CheckHostsTask extends AsyncTask<Void, String, String> {

        public CheckHostsTask() {
            super();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            s = s + s;
        }

        @Override
        protected String doInBackground(Void... records) {
//            try {
//                int timeout = 5;
//                for (int i = 2; i < 255; i++) {
//                    String host = "";
//                    host = scanIpAddress.subnet + "." + i;
//
//                    if (InetAddress.getByName(host).isReachable(timeout)) {
//                        Log.d("doInBackground()", host + " is reachable");
//                        String esp8266RestUrl = "/check";
//                        fixedUrl = fixedUrl + host + esp8266RestUrl;
//                        OkHttpClient client = new OkHttpClient();
//
//                        final Request request = new Request.Builder()
//                                .url(fixedUrl)
//                                .build();
//                        client.newCall(request).enqueue(new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                String resultIp = request.url().host();
//                                scanIpAddress.espIpAddress = resultIp;
//                                Log.d("doInBackground()", "Found a responding device!");
//                                scanIpAddress.foundEspIp = true;
//                            }
//                        });
//
//                    }
//                    if (scanIpAddress.foundEspIp) {
//                        break;
//                    }
//                }
//                scanIpAddress.scanComplete = true;
//
//            } catch (UnknownHostException e) {
//                Log.d("doInBackground()", " UnknownHostException e : " + e);
//                e.printStackTrace();
//            } catch (IOException e) {
//                Log.d("doInBackground()", "checkHosts() :: IOException e : " + e);
//                e.printStackTrace();
//            } finally {
//                Log.d("checkHosts()", "All the ip Address where scanned!");
//            }
            return "asdasd";
        }
    }
}
