package com.korpaev.myhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter
{
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<HistoryRow> objects;

    HistoryAdapter(Context context, ArrayList<HistoryRow> historyRow)
    {
        ctx = context;
        objects = historyRow;
        lInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = lInflater.inflate(R.layout.activity_history_row, parent, false);
        }

        HistoryRow historyRow = getHistoryRow(position);

        // заполняем View в пункте списка данными
        ((TextView) view.findViewById(R.id.tvHistoryRowDate)).setText(historyRow.getDateTime());
        ((TextView) view.findViewById(R.id.tvHistoryRowMessage)).setText(historyRow.getSmsBody());
        return view;
    }

    // товар по позиции
    HistoryRow getHistoryRow(int position)
    {
        return ((HistoryRow)getItem(position));
    }
}
