package com.example.felip.smartbanho.Utils;

import com.example.felip.smartbanho.model.ShowerDevice;

import java.util.List;

public interface SeekDevicesCallback {

    void onServerCallback(Boolean status, List<ShowerDevice> showers);

}