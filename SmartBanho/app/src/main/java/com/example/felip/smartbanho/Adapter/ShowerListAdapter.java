package com.example.felip.smartbanho.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.felip.smartbanho.R;
import com.example.felip.smartbanho.model.ShowerDevice;

import java.util.List;

public class ShowerListAdapter extends RecyclerView.Adapter<ShowerListAdapter.MyViewHolder> {
    private Context context;
    private List<ShowerDevice> showerDevices;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, ip;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            ip = view.findViewById(R.id.ip);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public ShowerListAdapter(Context context, List<ShowerDevice> showerDevices) {
        this.context = context;
        this.showerDevices = showerDevices;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //When ViewHolder is created the shower_list_item.xml layout will be inflated to the current UI
        View showerItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shower_list_item, parent, false);

        return new MyViewHolder(showerItemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //Which showerDevice of the showerDevices will be bound setting the TextView from its current POJO attribute
        final ShowerDevice showerDevice = showerDevices.get(position);
        String showerName;
        if (showerDevice.getName().equals("UNAMED")) {
            holder.name.setText("Dispositivo sem Nome");
        } else {
            holder.name.setText(showerDevice.getName());
        }
        holder.ip.setText(showerDevice.getIp());
    }

    @Override
    public int getItemCount() {
        return showerDevices.size();
    }

    //This method is called by onSwipe in ShowerListActivity that implements this method from RecyclerItemTouchHelperListener interface
    //This method is called to delete items from RecyclerView list
    public void removeShowerDevice(int position) {
        showerDevices.remove(position);
        // notify the showerDevice removed by position
        // to perform recycler view delete animations
        notifyItemRemoved(position);
    }

    //PLUS-This method is called to restore an item that have been deleted that is bound to onSwipe method on ShowerListActivity
    public void restoreShowerDevice(ShowerDevice showerDevice, int position) {
        showerDevices.add(position, showerDevice);
        // notify showerDevice added by position
        notifyItemInserted(position);
    }
}