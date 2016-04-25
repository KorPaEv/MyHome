package com.korpaev.myhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceInfoAdapter extends BaseAdapter
{
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<DeviceInfoRow> objects;

    DeviceInfoAdapter(Context context, ArrayList<DeviceInfoRow> deviceInfoRow)
    {
        ctx = context;
        objects = deviceInfoRow;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return objects.size();
    }

    @Override
    public Object getItem(int position)
    {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null)
        {
            view = lInflater.inflate(R.layout.activity_devices_row, parent, false);
        }

        DeviceInfoRow dir = getDeviceInfoRow(position);

        // заполняем View в пункте списка данными
        ((TextView) view.findViewById(R.id.deviceInfoRowName)).setText(dir.getNameDevice());
        return view;
    }

    // товар по позиции
    DeviceInfoRow getDeviceInfoRow(int position)
    {
        return ((DeviceInfoRow)getItem(position));
    }
}
