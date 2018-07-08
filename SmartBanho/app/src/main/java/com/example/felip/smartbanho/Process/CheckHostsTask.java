package com.example.felip.smartbanho.Process;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

class CheckHostsTask extends AsyncTask<Void, Void, Void> {

    private Exception exception;
    private ScanIpAddress scanIpAddress;

    public CheckHostsTask(ScanIpAddress scanIpAddress) {
        this.scanIpAddress = scanIpAddress;
        this.scanIpAddress.ipAddresses = new ArrayList<>();
    }


    protected Void doInBackground(Void... records) {
        try {
            int timeout = 5;
            for (int i = 2; i < 255; i++) {
                String host = this.scanIpAddress.subnet + "." + i;

                if (InetAddress.getByName(host).isReachable(timeout)) {
                    Log.d("doInBackground()", host + " is reachable");
                    this.scanIpAddress.ipAddresses.add(host);
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

    protected void onPostExecute() {
        Log.d("onPostExecute()", "Scan Completed!");
    }


}
