package com.example.felip.smartbanho.Process;


import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

public class ScanIpAddressImpl implements ScanIpAddress {

    private Context mContext;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    public String subnet;
    public List<String> ipAddresses;
    public Boolean scanComplete = false;

    public ScanIpAddressImpl(Context mContext) {
        this.mContext = mContext;
        Log.d("ScanIpAddress Class", "Started ScanIpAddress class");
    }

    public void setSubnet() {
        Log.d("setSubnet()", "Getting the default gateway of the local network");
        WifiManager mWifiManager = (WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        this.subnet = getSubnetAddress(mWifiManager.getDhcpInfo().gateway);

    }

    public String getSubnetAddress(int address) {
        Log.d("getSubnetAddress()", "Casting subnet to string");
        String ipString = String.format(
                "%d.%d.%d",
                (address & 0xff),
                (address >> 8 & 0xff),
                (address >> 16 & 0xff));

        return ipString;
    }

    public void checkHosts(String subnet) {
        String foundEspIp = "";
        Log.d("checkHosts()", "Scanning all Ip Address of the local network");
        try {
            new CheckHostsTask(this).execute();

        } catch (Exception e) {
            Log.d("checkHosts()", " UnknownHostException e : " + e);
        }
    }
}
