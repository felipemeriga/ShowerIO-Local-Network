package com.example.felip.smartbanho.Utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Arrays;

public class CredentialsUtils {

    public ServerCallback callback;


    public void hasAnyCredentials(String serverBasePath, RequestQueue requestQueue, final ServerCallback callback) {
        final String ENDPOINT = serverBasePath + "/verifyAccountExistance";
        Log.i("hasAnyCredentials", "fetchPosts() Doing the HTTP GET request on ENDPOINT: " + ENDPOINT);
        requestQueue.add(new StringRequest(Request.Method.GET, ENDPOINT, onPostsLoaded, onPostsError));
        this.callback = callback;

    }

    public Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            callback.onServerCallback(true, response);
        }
    };

    public Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            callback.onServerCallback(false, error.getMessage());
        }
    };

}
