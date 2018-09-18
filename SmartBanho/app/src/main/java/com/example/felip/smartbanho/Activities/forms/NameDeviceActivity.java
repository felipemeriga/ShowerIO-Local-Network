package com.example.felip.smartbanho.Activities.forms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.felip.smartbanho.Activities.ShowerIO.ShowerDetailActivity;
import com.example.felip.smartbanho.Activities.ShowerIO.ShowerIO;
import com.example.felip.smartbanho.Activities.SignupActivity;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.Rest.NameService;
import com.example.felip.smartbanho.Utils.ServerCallback;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NameDeviceActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @BindView(R.id.form_name)
    EditText _nameText;
    @BindView(R.id.btn_name)
    Button nameButton;
    private ProgressDialog progressDialog;
    private ShowerDevice device;
    private NameService service;
    private RequestQueue requestQueue;
    private final String SHOWERIO = "ShowerIO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Uncomment in order to make this activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_name_device);
        ButterKnife.bind(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        String selectedShower = getIntent().getExtras().getString("device");
        device = new Gson().fromJson(selectedShower, ShowerDevice.class);

        progressDialog = new ProgressDialog(NameDeviceActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Nomeando dispositivo...");

        this.service = new NameService();

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
        nameButton.setEnabled(false);
        device.setName(name);
        progressDialog.show();
        updateDevicesList(name);
        if (validate()) {
            String serverBasePath = "http://" + device.getIp();
            this.service.setDeviceName(serverBasePath, name, this.requestQueue, new ServerCallback() {
                @Override
                public void onServerCallback(Boolean status, String response) {
                    if (status == true) {
                        Log.d("NameDeviceActivity ", " setName() - Successfully naming device ");
                        Toast.makeText(getBaseContext(), "Nome criado com sucesso!", Toast.LENGTH_LONG).show();
                        Intent showerDetailActivity = new Intent(NameDeviceActivity.this, ShowerDetailActivity.class);
                        String deviceAsString = new Gson().toJson(device);
                        showerDetailActivity.putExtra("device", deviceAsString);
                        startActivity(showerDetailActivity);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    } else {
                        Log.d("NameDeviceActivity ", " setName() - error naming device ");
                        Toast.makeText(getBaseContext(), "Erro ao criar o nome", Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            progressDialog.hide();
            nameButton.setEnabled(true);
        }

    }

    private void updateDevicesList(String name) {
        sharedPreferences = getSharedPreferences(SHOWERIO, MODE_PRIVATE);
        String showersArrayAsString = sharedPreferences.getString("listOfDevices", null);
        List<ShowerDevice> showers = Arrays.asList(new Gson().fromJson(showersArrayAsString, ShowerDevice[].class));
        for (ShowerDevice loopDevice : showers) {
            if (loopDevice.getIp().equals(device.getIp())) {
                device.setName(name);
                int index = showers.indexOf(loopDevice);
                showers.get(index).setName(name);
            }
        }
        SharedPreferences.Editor editor = getSharedPreferences(SHOWERIO, MODE_PRIVATE).edit();
        String showerArrayAsString = new Gson().toJson(showers);
        editor.putString("listOfDevices", showerArrayAsString);
        editor.commit();


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent showerDetailActivity = new Intent(NameDeviceActivity.this, ShowerDetailActivity.class);
        String deviceAsString = new Gson().toJson(device);
        showerDetailActivity.putExtra("device", deviceAsString);
        startActivity(showerDetailActivity);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
