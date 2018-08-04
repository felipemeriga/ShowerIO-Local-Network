package com.example.felip.smartbanho.Activities.LoginService;

import android.os.AsyncTask;
import android.util.Log;

import com.example.felip.smartbanho.Activities.LoginActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginService {

    public class CheckExistanceAccounts extends AsyncTask<Void, Void, Void> {

        LoginActivity loginActivity;
        private final String VERIFICATION_URL = "/verifyAccountExistance";

        public CheckExistanceAccounts(LoginActivity loginActivity) {
            super();
            this.loginActivity = loginActivity;
        }


        protected void onRequestFinished() {
            loginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginActivity.accountExistance();
                }
            });
        }


        @Override
        protected Void doInBackground(Void... records) {
            String fixedUrl = "http://";
            fixedUrl = fixedUrl + loginActivity.espIpAddress + VERIFICATION_URL;
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(fixedUrl)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    if (result.equals("Y")) {
                        loginActivity.existingAccount = true;
                    } else {
                        loginActivity.existingAccount = false;
                    }
                    onRequestFinished();
                }
            });
            return null;
        }
    }

    public class ValidateCredentials extends AsyncTask<Void, Void, Void> {

        LoginActivity loginActivity;
        private final String AUTHENTICATION_URL = "/verifyCredentials?email=";

        public ValidateCredentials(LoginActivity loginActivity) {
            super();
            this.loginActivity = loginActivity;
        }


        protected void onRequestFinished(String res) {
            if (loginActivity.authenticate_result == true) {
                if (res.equals("VALIDATED")) {
                    Log.d("LoginActivity Class", "Valid, opening ShowerIO activity");
                    loginActivity.onPostAuthenticate();
                } else {
                    loginActivity.failedAuthResult = res;

                    loginActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginActivity.onFailedPostAuthenticate();
                        }
                    });


                }

            } else {
                loginActivity.onLoginFailed();
            }
        }

        @Override
        protected Void doInBackground(Void... records) {
            Log.d("LoginActivity Class", "Sending credentials to ESP8266");
            String fixedUrl = "http://";
            fixedUrl = fixedUrl + loginActivity.espIpAddress + AUTHENTICATION_URL + loginActivity._emailText.getText().toString() + "&password=" + loginActivity._passwordText.getText().toString();
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(fixedUrl)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    loginActivity.authenticate_result = false;
                    onRequestFinished("ERROR");
                    Log.d("LoginActivity Class", "Error occurred on matching the credentials");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d("LoginActivity Class", "Got response from ESP8266!");
                    String result = response.body().string();
                    if (!result.equals("ERROR")) {
                        loginActivity.authenticate_result = true;
                        onRequestFinished(result);
                    } else {
                        onRequestFinished("ERROR");
                        loginActivity.authenticate_result = false;
                    }
                }
            });
            return null;
        }
    }

}



