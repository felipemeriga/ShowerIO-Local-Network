package com.example.felip.smartbanho.Activities.ShowerIO;

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
import android.widget.Toast;

import com.example.felip.smartbanho.Adapter.ShowerListAdapter;
import com.example.felip.smartbanho.Helper.RecyclerItemTouchHelper;
import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.model.ShowerDevice;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowerListActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    public List<ShowerDevice> showerDevicesList;
    private ShowerListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shower_list);

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

        fetchDevicesInUI();
    }

    public void fetchDevicesInUI(){
/*        if(showerDevicesList.size()==0){
            Log.d("ShowerListAcitivty", "fetchDevicesInUI(): Any devices were found, calling toast to alert user");
            Toast.makeText(getApplicationContext(), "Nenhum dispositivo foi adicionado, efetue uma nova busca", Toast.LENGTH_LONG).show();
            return;
        }*/
        showerDevicesList.clear();

        String showersArrayAsString = getIntent().getExtras().getString("showerDevices");
        List<ShowerDevice> list = Arrays.asList(new Gson().fromJson(showersArrayAsString, ShowerDevice[].class));
        showerDevicesList.addAll(list);
        Log.i("ShowerListAcitivty", "fetchDevicesInUI(): Adding found devices List<ShowerDevice> and notifying Adapter that new data was inserted");
        mAdapter.notifyDataSetChanged();
    }





    //As ShowerListActivity implements RecyclerItemTouchHelperListener, the method onSwiped is being Overrided
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof ShowerListAdapter.MyViewHolder) {
            // get the removed shower name to display it in snack bar
            String name = showerDevicesList.get(viewHolder.getAdapterPosition()).getName();

            // TODO - Create the transition between the detail Activity
/*            // backup of removed shower for undo purpose
            final ShowerDevice deletedShower = showerDevicesList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the wafer from recycler view
            mAdapter.removeWafer(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed from wafer list!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreWafer(deletedWafer, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/
        }
    }

}
