package com.example.felip.smartbanho.Activities.ShowerIO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.support.v7.widget.CardView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.felip.smartbanho.Activities.Forms.NameDeviceActivity;
import com.example.felip.smartbanho.Activities.LoginActivity;
import com.example.felip.smartbanho.Activities.Search.SearchForDevices;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

public class ShowerDetailActivity extends AppCompatActivity {

    private GridLayout mainGrid;
    private ShowerDevice device;
    private TextView deviceTitle;
    public static String selectedShower;
    private SharedPreferences sharedPreferences;
    private final String SHOWERIO = "ShowerIO";
    private CardView cardViewPlay;
    private Boolean nameFlag;
    private ScrollView scrollView;
    private ColorStateList defaultColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower_detail);
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        deviceTitle = findViewById(R.id.textGrid);
        cardViewPlay = findViewById(R.id.cardViewPlay);
        scrollView = findViewById(R.id.scrollView);
        nameFlag = false;
        defaultColor = cardViewPlay.getCardBackgroundColor();

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);

        selectedShower = getIntent().getExtras().getString("device");
        device = new Gson().fromJson(selectedShower, ShowerDevice.class);
        if (device.getName().isEmpty() || device.getName().equals("UNAMED")) {
            deviceTitle.setText(R.string.noName);
            cardViewPlay.setCardBackgroundColor(Color.GRAY);
            nameFlag = true;
            helpUserSetName();
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
                            if (!nameFlag) {
                                Log.i("ShowerDetailActivity", "case 1, opening ShowerIOActivity");
                                Intent showerIO = new Intent(ShowerDetailActivity.this, ShowerIO.class);
                                showerIO.putExtra("device", ShowerDetailActivity.selectedShower);
                                startActivity(showerIO);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                            break;
                        case 1:
                            Log.i("ShowerDetailActivity", "case 2, opening NameDeviceActivity");
                            Intent nameDeviceActivity = new Intent(ShowerDetailActivity.this, NameDeviceActivity.class);
                            nameDeviceActivity.putExtra("device", ShowerDetailActivity.selectedShower);
                            startActivity(nameDeviceActivity);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            break;
                        case 2:
                            Log.i("ShowerDetailActivity", "case 2, opening LoginActivity");
                            Intent loginActivity = new Intent(ShowerDetailActivity.this, LoginActivity.class);
                            loginActivity.putExtra("device", ShowerDetailActivity.selectedShower);
                            startActivity(loginActivity);
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

        Intent showerListActivity = new Intent(ShowerDetailActivity.this, ShowerListActivity.class);
        showerListActivity.putExtra("showerDevices", showersArrayAsString);
        startActivity(showerListActivity);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void helpUserSetName() {
        // showing snack bar to help user to use the application
        Snackbar snackbar = Snackbar
                .make(scrollView, "Nomeie seu dispositivo antes de comerçar!", Snackbar.LENGTH_LONG)
                .setDuration(5000);
        snackbar.show();
    }
}
