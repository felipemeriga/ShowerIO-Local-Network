package com.example.felip.smartbanho.Process;

public interface ScanIpAddress {

    public void setSubnet();

    public String getSubnetAddress(int address);

    public void checkHosts(String subnet);

}
