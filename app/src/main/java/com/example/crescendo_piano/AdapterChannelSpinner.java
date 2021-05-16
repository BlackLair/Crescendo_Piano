package com.example.crescendo_piano;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterChannelSpinner extends BaseAdapter {
    Context mContext;
    List<String> Data;
    LayoutInflater Inflater;

    public AdapterChannelSpinner(Context context, List<String> data){
        this.mContext=context;
        this.Data=data;
        Inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(Data!=null) return Data.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return Data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=Inflater.inflate(R.layout.channel_spinner_open, parent, false);
        }
        String text=Data.get(position);
        ((TextView)convertView.findViewById(R.id.spinnerText)).setText(text);
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=Inflater.inflate(R.layout.channel_spinner, parent, false);
        }
        if(Data!=null){
            String text=Data.get(position);
            ((TextView)convertView.findViewById(R.id.spinnerText)).setText(text);
        }
        return convertView;
    }
}
