package com.example.crescendo_piano;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InstViewPagerAdapter extends RecyclerView.Adapter<InstViewHolderPage> {
    private ArrayList<InstDataPage> listData;

    InstViewPagerAdapter(ArrayList<InstDataPage> data){
        this.listData=data;
    }

    @NonNull
    @Override
    public InstViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_inst_viewpager, parent, false);

        return new InstViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstViewHolderPage holder, int position) {
        if(holder instanceof InstViewHolderPage){
            InstViewHolderPage viewHolder = (InstViewHolderPage) holder;
            viewHolder.onBind(listData.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }
}
