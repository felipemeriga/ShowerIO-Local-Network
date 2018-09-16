package com.example.felip.smartbanho.Activities.ShowerIO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.example.felip.smartbanho.Activities.forms.NameDeviceActivity;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class ShowerDetailActivity extends AppCompatActivity {

    private GridLayout mainGrid;
    private ShowerDevice device;
    private TextView deviceTitle;
    public static String selectedShower;
    private SharedPreferences sharedPreferences;
    private final String SHOWERIO = "ShowerIO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower_detail);
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        deviceTitle = findViewById(R.id.textGrid);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);

        selectedShower = getIntent().getExtras().getString("device");
        device = new Gson().fromJson(selectedShower, ShowerDevice.class);
        if (device.getName().isEmpty()) {
            deviceTitle.setText(R.string.noName);
        } else {
            deviceTitle.setText(device.getName());
        }
    }


    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (finalI) {
                        case 0:
                            Intent showerIO = new Intent(ShowerDetailActivity.this, ShowerIO.class);
                            showerIO.putExtra("device", ShowerDetailActivity.selectedShower);
                            startActivity(showerIO);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case 1:
                            Intent nameDeviceActivity = new Intent(ShowerDetailActivity.this, NameDeviceActivity.class);
                            nameDeviceActivity.putExtra("device", ShowerDetailActivity.selectedShower);
                            startActivity(nameDeviceActivity);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        sharedPreferences = getSharedPreferences(SHOWERIO, MODE_PRIVATE);
        String showersArrayAsString = sharedPreferences.getString("listOfDevices", null);

        String selectedDevice = new Gson().toJson(device);
        Intent showerDetailActivity = new Intent(ShowerDetailActivity.this, ShowerDetailActivity.class);
        showerDetailActivity.putExtra("showerDevices", showersArrayAsString);
        startActivity(showerDetailActivity);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
