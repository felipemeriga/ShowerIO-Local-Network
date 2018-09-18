package com.example.felip.smartbanho.Activities.Error;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.felip.smartbanho.Activities.Search.SearchForDevices;
import com.example.felip.smartbanho.R;

public class DisplayMessageActivity extends AppCompatActivity {

    public static final String ESP8266 = "esp8266";
    private Button restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        restart = (Button) findViewById(R.id.button);
        restart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ESP8266, MODE_PRIVATE).edit();
                editor.putString("ip", null);
                editor.apply();
                Intent searchForDevices = new Intent(DisplayMessageActivity.this, SearchForDevices.class);
                startActivity(searchForDevices);
                finish();
            }
        });


    }


}
