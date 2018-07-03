package com.example.felip.smartbanho;


import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ScanIpAddress {

    Context mContext;
    WifiManager mWifiManager;
    WifiInfo mWifiInfo;
    String subnet;

    public ScanIpAddress(Context mContext) {
        this.mContext = mContext;
        Log.d("ScanIpAddress Class", "Started ScanIpAddress class");
    }

    public void setSubnet() {
        Log.d("setSubnet()", "Getting the default gateway of the local network");
        WifiManager mWifiManager = (WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        this.subnet = getSubnetAddress(mWifiManager.getDhcpInfo().gateway);

    }

    private String getSubnetAddress(int address) {
        Log.d("getSubnetAddress()", "Casting subnet to string");
        String ipString = String.format(
                "%d.%d.%d",
                (address & 0xff),
                (address >> 8 & 0xff),
                (address >> 16 & 0xff));

        return ipString;
    }

    public void checkHosts(String subnet) {
        Log.d("checkHosts()", "Scanning all Ip Address of the local network");
        try {
            int timeout = 5;
            for (int i = 1; i < 255; i++) {
                String host = subnet + "." + i;
                if (InetAddress.getByName(host).isReachable(timeout)) {
                    Log.d("checkHosts()", host + " is reachable");
                }
            }
        } catch (UnknownHostException e) {
            Log.d("checkHosts()", " UnknownHostException e : " + e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("checkHosts()", "checkHosts() :: IOException e : " + e);
            e.printStackTrace();
        }

    }
}
