package com.example.felip.smartbanho.Activities.ShowerIO;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class ShowerDetailActivity extends AppCompatActivity {

    GridLayout mainGrid;
    ShowerDevice device;
    TextView deviceTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower_detail);
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        deviceTitle = findViewById(R.id.textGrid);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);

/*        String selectedShower = getIntent().getExtras().getString("device");
        ShowerDevice device = new Gson().fromJson(selectedShower, ShowerDevice.class);
        if (device.getName().isEmpty()) {
            deviceTitle.setText(R.string.noName);
        } else {
            deviceTitle.setText(device.getName());
        }*/
    }


    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            final String selectedDeviceAsArray = new Gson().toJson(device);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (finalI) {
                        case 0:
                            Intent showerIO = new Intent(ShowerDetailActivity.this, ShowerIO.class);
                            showerIO.putExtra("device", selectedDeviceAsArray);
                            startActivity(showerIO);
                            finish();
                            break;
                    }
                }
            });
        }
    }
}
