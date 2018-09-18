package com.example.felip.smartbanho.Rest;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.felip.smartbanho.Utils.ServerCallback;

public class NameService {

    public NameService() {
        super();
    }

    private ServerCallback callback;

    public void setDeviceName(String serverBasePath, String deviceName, RequestQueue requestQueue, final ServerCallback callback) {
        final String ENDPOINT = serverBasePath + "/createName?name=" + deviceName;
        Log.i("NameService", "setDeviceName() Doing the HTTP GET request on ENDPOINT: " + ENDPOINT);
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
