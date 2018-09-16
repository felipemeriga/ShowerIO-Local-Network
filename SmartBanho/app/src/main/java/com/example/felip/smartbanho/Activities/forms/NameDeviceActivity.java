package com.example.felip.smartbanho.Activities.forms;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.Rest.NameService;
import com.example.felip.smartbanho.Utils.ServerCallback;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NameDeviceActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @BindView(R.id.form_name)
    EditText _nameText;
    @BindView(R.id.btn_name)
    Button nameButton;
    private ShowerDevice device;
    private NameService service;
    private RequestQueue requestQueue;
    private final String SHOWERIO = "ShowerIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_name_device);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        sharedPreferences = getSharedPreferences(SHOWERIO, MODE_PRIVATE);
        String deviceAsString = sharedPreferences.getString("actualDevice", null);
        device = new Gson().fromJson(deviceAsString, ShowerDevice.class);

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setName();
            }
        });
    }

    private void setName() {

        String name = _nameText.getText().toString();
        Log.d("NameDeviceActivity ", " setName() - Setting the device name: " + name);
        if(validate()){
            String serverBasePath = "http://" + device.getIp();
            this.service.setDeviceName(serverBasePath, name, this.requestQueue, new ServerCallback() {
                @Override
                public void onServerCallback(Boolean status, String response) {
                    if(status==true){
                        Log.d("NameDeviceActivity ", " setName() - Successfully named device ");

                    }

                }
            });
        }

    }

    private Boolean validate() {
        String name = _nameText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            return false;
        } else {
            _nameText.setError(null);
            return true;
        }
    }


}
