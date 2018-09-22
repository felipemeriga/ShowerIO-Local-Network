package com.example.felip.smartbanho.Activities.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.felip.smartbanho.Activities.Search.SearchForDevices;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3500;
    private SharedPreferences sharedPreferences;
    private final String SHOWERIO = "ShowerIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent searchForDevices = new Intent(HomeActivity.this, SearchForDevices.class);
/*                SharedPreferences.Editor editor = getSharedPreferences(SHOWERIO, MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();*/
                sharedPreferences = getSharedPreferences(SHOWERIO, MODE_PRIVATE);
                String showersArrayAsString = sharedPreferences.getString("listOfDevices", null);
                List<ShowerDevice> showers = new ArrayList<>();
                if (showersArrayAsString != null) {
                    showers = Arrays.asList(new Gson().fromJson(showersArrayAsString, ShowerDevice[].class));
                }
                if (showers.size() == 0) {
                    searchForDevices.putExtra("scanType", "FULLSCAN");
                } else {
                    searchForDevices.putExtra("scanType", "PARTIAL");
                }
                startActivity(searchForDevices);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
