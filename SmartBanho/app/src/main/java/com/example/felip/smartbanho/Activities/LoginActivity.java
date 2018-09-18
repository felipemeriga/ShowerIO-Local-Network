package com.example.felip.smartbanho.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felip.smartbanho.Activities.ShowerIO.ShowerDetailActivity;
import com.example.felip.smartbanho.Rest.LoginService;
import com.example.felip.smartbanho.Activities.ShowerIO.ShowerIO;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private SharedPreferences sharedPreferences;
    private final String SHOWERIO = "ShowerIO";
    private static String EMAIL;
    private static String PASSWORD;
    public String espIpAddress;
    private String fixedUrl = "http://";
    private final String AUTHENTICATION_URL = "/verifyCredentials?email=";
    private final String VERIFICATION_URL = "/verifyAccountExistance";
    public Boolean authenticate_result = false;
    public String failedAuthResult;
    public Boolean existingAccount = false;
    private LoginService loginService;
    private ShowerDevice device;

    @BindView(R.id.input_email)
    public EditText _emailText;
    @BindView(R.id.input_password)
    public EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginService = new LoginService();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //getting actual selected device
        String selectedShower = getIntent().getExtras().getString("device");
        device = new Gson().fromJson(selectedShower, ShowerDevice.class);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(SHOWERIO, MODE_PRIVATE);
        EMAIL = sharedPreferences.getString("email", null);
        PASSWORD = sharedPreferences.getString("password", null);
        espIpAddress = device.getIp();

        if (EMAIL != null) {
            _emailText.setText(EMAIL);
        }
        if (PASSWORD != null) {
            _passwordText.setText(PASSWORD);
        }


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                String selectedDeviceAsString = new Gson().toJson(device);
                intent.putExtra("device", selectedDeviceAsString);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        _signupLink.setEnabled(false);
        loginService.new CheckExistanceAccounts(this).execute();
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        progressDialog.show();
        _loginButton.setEnabled(false);
        loginService.new ValidateCredentials(this).execute();

    }

    public void onPostAuthenticate() {

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        progressDialog.dismiss();
        onLoginSuccess();
    }

    public void onFailedPostAuthenticate() {
        if (failedAuthResult.equals("EMAIL")) {
            _emailText.setError("enter a valid email address");
            Log.d("LoginActivity Class", "Wrong Email");
            onLoginFailed();
        } else if (failedAuthResult.equals("PASSWORD")) {
            _passwordText.setError("enter a valid password");
            Log.d("LoginActivity Class", "Wrong Password");
            onLoginFailed();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        SharedPreferences.Editor editor = getSharedPreferences(SHOWERIO, MODE_PRIVATE).edit();
        editor.putString("email", _emailText.getText().toString());
        editor.putString("password", _passwordText.getText().toString());
        editor.putBoolean("authorization", true);
        editor.apply();

        Intent showerDetailActivity = new Intent(LoginActivity.this, ShowerDetailActivity.class);
        String deviceAsString = new Gson().toJson(device);
        showerDetailActivity.putExtra("device", deviceAsString);
        startActivity(showerDetailActivity);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
        _loginButton.setEnabled(true);

        SharedPreferences.Editor editor = getSharedPreferences(SHOWERIO, MODE_PRIVATE).edit();
        editor.putBoolean("authorization", true);
        editor.apply();
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void accountExistance() {
        if (existingAccount == true) {
            _signupLink.setEnabled(false);
        } else {
            _signupLink.setEnabled(true);
        }
    }


}
