package com.example.felip.smartbanho.Activities.ShowerIO;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.felip.smartbanho.Activities.LoginActivity;
import com.example.felip.smartbanho.Activities.Search.SearchForDevices;
import com.example.felip.smartbanho.Adapter.ShowerListAdapter;
import com.example.felip.smartbanho.Helper.RecyclerItemTouchHelper;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.Utils.CredentialsUtils;
import com.example.felip.smartbanho.Utils.ServerCallback;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowerListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    public List<ShowerDevice> showerDevicesList;
    private ShowerListAdapter mAdapter;
    private CredentialsUtils credentialsUtils;
    private RequestQueue requestQueue;
    private final String SHOWERIO = "ShowerIO";
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_shower_list);

        //Instanciating progressBar when a user selects an device
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        WanderingCubes wanderingCubes = new WanderingCubes();
        progressBar.setIndeterminateDrawable(wanderingCubes);
        progressBar.setVisibility(View.GONE);
        toolbar = findViewById(R.id.toolbar);

        Log.d("ShowerListAcitivty", "onCreate(): Defining and setting Toolbar title and configurations");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.list_showers));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("ShowerListAcitivty", "onCreate(): Referencing recyclerView and coordinatorLayout by their xml layout id's");
        recyclerView = findViewById(R.id.recyclerShower);
        coordinatorLayout = findViewById(R.id.coordinator_layout);

        Log.d("ShowerListAcitivty", "onCreate(): Instanciating an Array list of the java POJO ShowerDevice");
        showerDevicesList = new ArrayList<>();

        Log.d("ShowerListAcitivty", "onCreate(): Instanciating ShowerListAdapter which will iterate the showerDevicesList through UI");
        mAdapter = new ShowerListAdapter(this, showerDevicesList);

        Log.d("ShowerListAcitivty", "onCreate(): Instanciating RecyclerView and adding the decorators");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        //Where a new instance RecyclerItemTouchHelper is created, as the ShowerListAcitivty implements the interface RecyclerItemTouchHelperListener, it can be passed as a parameter
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        Log.i("ShowerListAcitivty", "onCreate(): Calling fetchDevicesInUI, to populate RecyclerView ");
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        credentialsUtils = new CredentialsUtils();
        helpUser();
        fetchDevicesInUI();
    }

    public void fetchDevicesInUI() {
        showerDevicesList.clear();

        String showersArrayAsString = getIntent().getExtras().getString("showerDevices");
        List<ShowerDevice> list = Arrays.asList(new Gson().fromJson(showersArrayAsString, ShowerDevice[].class));
        showerDevicesList.addAll(list);
        if (list.size() == 0) {
            sharedPreferences = getSharedPreferences(SHOWERIO, MODE_PRIVATE);
            showersArrayAsString = sharedPreferences.getString("listOfDevices", null);
            list = Arrays.asList(new Gson().fromJson(showersArrayAsString, ShowerDevice[].class));
            showerDevicesList.addAll(list);
            if (list.size() == 0) {
                Log.d("ShowerListAcitivty", "fetchDevicesInUI(): Any devices were found, calling toast to alert user");
                Toast.makeText(getApplicationContext(), "Nenhum dispositivo foi adicionado, efetue uma nova busca", Toast.LENGTH_LONG).show();
            } else {
                Log.i("ShowerListAcitivty", "fetchDevicesInUI(): Adding found devices List<ShowerDevice> and notifying Adapter that new data was inserted");
                mAdapter.notifyDataSetChanged();
            }

        } else {
            Log.i("ShowerListAcitivty", "fetchDevicesInUI(): Adding found devices List<ShowerDevice> and notifying Adapter that new data was inserted");
            mAdapter.notifyDataSetChanged();
        }
    }


    //As ShowerListActivity implements RecyclerItemTouchHelperListener, the method onSwiped is being Overrided
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ShowerListAdapter.MyViewHolder) {
            // get the removed shower name to display it in snack bar
            final ShowerDevice selectedDevice = showerDevicesList.get(viewHolder.getAdapterPosition());
            String name = showerDevicesList.get(viewHolder.getAdapterPosition()).getName();

            String deviceBasePath = "http://" + selectedDevice.getIp();

            //Adding the selected device IP to sharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences(SHOWERIO, MODE_PRIVATE).edit();
            editor.putString("actualDeviceIp", selectedDevice.getIp());
            editor.apply();
            if (selectedDevice.getStatus().equals("ONLINE")) {
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                credentialsUtils.hasAnyCredentials(deviceBasePath, requestQueue, new ServerCallback() {
                    @Override
                    public void onServerCallback(Boolean status, String response) {
                        if (status == true) {
                            String selectedDeviceAsString = new Gson().toJson(selectedDevice);
                            Log.i("ShowerListActivity", "onServerCallback(), request to credentials went successful");
                            if (response.equals("Y")) {
                                Log.i("ShowerListActivity", "onServerCallback(), server has credentials, opening LoginActivity");
                                Intent loginActivity = new Intent(ShowerListActivity.this, LoginActivity.class);
                                loginActivity.putExtra("device", selectedDeviceAsString);
                                startActivity(loginActivity);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            } else if (response.equals("N")) {
                                Log.i("ShowerListActivity", "onServerCallback(), server has no credentials, opening ShowerDetailActivity");

                                Intent showerDetailActivity = new Intent(ShowerListActivity.this, ShowerDetailActivity.class);
                                showerDetailActivity.putExtra("device", selectedDeviceAsString);
                                startActivity(showerDetailActivity);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Não foi possível efetuar uma comunicação com o servidor, reinicie o aplicativo", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.VISIBLE);
                        }
                    }
                });

            } else {
                // showing snack bar to help user to use the application
                recyclerView.startNestedScroll(1,1);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Ops! Este dispositivo parece estar desconectado! Efetue um escaneamento da rede", Snackbar.LENGTH_LONG)
                        .setDuration(8000);
                snackbar.setAction("Escanear", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    int index = showerDevicesList.indexOf(selectedDevice);
                    showerDevicesList.remove(index);
                    SharedPreferences.Editor editor = getSharedPreferences(SHOWERIO, MODE_PRIVATE).edit();
                    Intent seachForDevices = new Intent(ShowerListActivity.this, SearchForDevices.class);
                    String showerArrayAsString = new Gson().toJson(showerDevicesList);
                    editor.putString("listOfDevices", showerArrayAsString);
                    editor.commit();
                    seachForDevices.putExtra("scanType", "FULLSCAN");
                    startActivity(seachForDevices);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        }
    }

    private void helpUser() {
        // showing snack bar to help user to use the application
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Deslize para esquerda para escolher um chuveiro!", Snackbar.LENGTH_LONG)
                .setDuration(8000);
        snackbar.show();
    }

}
