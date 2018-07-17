package com.example.felip.smartbanho.Process;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CheckHostsTask extends AsyncTask<Void, Void, Void> {

    private Exception exception;
    private ScanIpAddressImpl scanIpAddress;
    private String fixedUrl = "http://";
    private OkHttpClient client;

    public CheckHostsTask(ScanIpAddressImpl scanIpAddress) {
        this.scanIpAddress = scanIpAddress;
        this.scanIpAddress.ipAddresses = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... records) {
        try {
            int timeout = 5;
            for (int i = 2; i < 255; i++) {
                String host = "";
                host = this.scanIpAddress.subnet + "." + i;

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
                            scanIpAddress.espIpAddress = resultIp;
                            Log.d("doInBackground()", "Found a responding device!");
                            scanIpAddress.foundEspIp = true;
                        }
                    });

                }
                if (scanIpAddress.foundEspIp) {
                    break;
                }
            }
            this.scanIpAddress.scanComplete = true;

        } catch (UnknownHostException e) {
            Log.d("doInBackground()", " UnknownHostException e : " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("doInBackground()", "checkHosts() :: IOException e : " + e);
            e.printStackTrace();
        } finally {
            Log.d("checkHosts()", "All the ip Address where scanned!");
        }
        return null;
    }

}



