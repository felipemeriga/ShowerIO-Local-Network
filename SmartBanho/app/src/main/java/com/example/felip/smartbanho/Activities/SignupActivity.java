package com.example.felip.smartbanho.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felip.smartbanho.Activities.ShowerIO.ShowerIO;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private SharedPreferences sharedPreferences;
    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    String espIpAddress;
    private final String SHOWERIO = "ShowerIO";
    private final String CREDENTIALS_URL = "/createCredentials?email=";
    private Boolean createCredentialsFlag = false;
    private ProgressDialog progressDialog;
    private ShowerDevice device;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        //getting actual selected device
        String selectedShower = getIntent().getExtras().getString("device");
        device = new Gson().fromJson(selectedShower, ShowerDevice.class);

        espIpAddress = sharedPreferences.getString("ip", null);
        espIpAddress = device.getIp();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Criando uma conta...");
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        progressDialog.show();

        _signupButton.setEnabled(false);
        new CreatedCredentials(this).execute();

    }

    public void onAuthorizedSignup() {

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        //Saving on shared preferences to further authentication
        SharedPreferences.Editor editor = getSharedPreferences(SHOWERIO, MODE_PRIVATE).edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();

        // TODO: Implement your own signup logic here.
        progressDialog.dismiss();
        _signupButton.setEnabled(true);
        onSignupSuccess();

    }


    public void onSignupSuccess() {
        Intent showerIO = new Intent(SignupActivity.this, ShowerIO.class);
        startActivity(showerIO);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Erro de Login", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        progressDialog.dismiss();
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("Pelo menos 3 dígitos");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Entre um endereço de email válido");
            valid = false;
        } else {
            _emailText.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("entre uma senha entre 4 e 10 dígitos");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("As senhas inválida");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    private class CreatedCredentials extends AsyncTask<Void, Void, Void> {

        SignupActivity signupActivity;

        public CreatedCredentials(SignupActivity signupActivity) {
            super();
            this.signupActivity = signupActivity;
        }


        protected void onRequestFinished() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (signupActivity.createCredentialsFlag == true) {
                        signupActivity.onAuthorizedSignup();
                    } else {
                        signupActivity.onSignupFailed();
                    }
                }
            });
        }

        @Override
        protected Void doInBackground(Void... records) {

            Log.d("SignupActivity Class", "Sending credentials to ESP8266");
            String fixedUrl = "http://";
            fixedUrl = fixedUrl + signupActivity.espIpAddress + CREDENTIALS_URL + signupActivity._emailText.getText().toString() + "&password=" + signupActivity._passwordText.getText().toString();
            Log.d("SignupActivity Class", fixedUrl);

            try {
                OkHttpClient client = new OkHttpClient();
                final Request request = new Request.Builder()
                        .url(fixedUrl)
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        signupActivity.createCredentialsFlag = false;
                        Log.d("SignupActivity Class", "The server could not be contacted");
                        onRequestFinished();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("SignupActivity Class", "The request to create credentials went on success!");
                        signupActivity.createCredentialsFlag = true;
                        onRequestFinished();
                    }
                });
            } catch (Exception e) {
                Log.d("SignupActivity Class", e.getMessage());
                throw e;
            }

            return null;
        }
    }
}