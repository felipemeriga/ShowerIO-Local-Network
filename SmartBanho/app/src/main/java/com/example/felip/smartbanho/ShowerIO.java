package com.example.felip.smartbanho;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


public class ShowerIO extends AppCompatActivity {

    ProgressBar superProgressBar;
    ImageView superImageView;
    WebView superWebview;
    LinearLayout superLinerLayout;

    // Network Service Discovery related members
    // This allows the app to discover the SmartShower.local
    // "service" on the local network.
    // Reference: http://developer.android.com/training/connect-devices-wirelessly/nsd.html
    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener;
    private NsdManager.ResolveListener mResolveListener;
    private NsdServiceInfo mServiceInfo;
    public String mRPiAddress;
    private String showerIOIpAddres;

    // The NSD service type that the RPi exposes.
    private static final String SERVICE_TYPE = "_http._tcp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_shower);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        superProgressBar = findViewById(R.id.myProgressBar);
        superImageView = findViewById(R.id.myImageView);
        superWebview = findViewById(R.id.myWebView);
        superLinerLayout = findViewById(R.id.myLinearLayout);

        getSupportActionBar().setTitle("ShowerIO");
        getSupportActionBar().hide();


        /*superWebview.loadUrl("http://www.google.com");
        Bitmap bitmapShower = BitmapFactory.decodeResource(getResources(), R.drawable.ic_shower);

        superImageView.setImageBitmap(bitmapShower);
        superProgressBar.setMax(100);
        superWebview.getSettings().setJavaScriptEnabled(true);
        superWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                onNotFoundEsp();
                super.onReceivedError(view, request, error);
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                superLinerLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                superLinerLayout.setVisibility(View.GONE);
                super.onPageFinished(view, url);

            }


        });
        superWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                superProgressBar.setProgress(newProgress);
            }


        });*/


//        mRPiAddress = "";
//        mNsdManager = (NsdManager) (getApplicationContext().getSystemService(Context.NSD_SERVICE));
//        initializeResolveListener();
//        initializeDiscoveryListener();
//        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.super_menu, menu);
        return super.onCreateOptionsMenu(menu);


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                onBackPressed();
                break;
            case R.id.menu_forward:
                onForwardPressed();
                break;
            case R.id.menu_refresh:
                superWebview.reload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed() {
        if (superWebview.canGoForward()) {
            superWebview.goForward();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (superWebview.canGoBack()) {
            superWebview.goBack();
        } else {
            finish();
        }
    }

    public void onNotFoundEsp() {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);

    }*/

/*    private void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!  Do something with it.
                String name = service.getServiceName();
                String type = service.getServiceType();
                Log.d("NSD", "Service Name=" + name);
                Log.d("NSD", "Service Type=" + type);
                if (type.equals(SERVICE_TYPE) && name.contains("garagedoor")) {
                    Log.d("NSD", "Service Found @ '" + name + "'");
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.d("NSD", "Discovery Service Lost");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d("NSD", "Discovery Stopped");
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                Log.d("NSD", "Discovery Start Failed");
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                mNsdManager.stopServiceDiscovery(this);
                Log.d("NSD", "Discovery failed");
            }
        };
    }

    private void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Log.e("NSD", "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                mServiceInfo = serviceInfo;

                // Port is being returned as 9. Not needed.
                //int port = mServiceInfo.getPort();

                InetAddress host = mServiceInfo.getHost();
                String address = host.getHostAddress();
                Log.d("NSD", "Resolved address = " + address);
                mRPiAddress = address;
            }
        };
    }*/
}
