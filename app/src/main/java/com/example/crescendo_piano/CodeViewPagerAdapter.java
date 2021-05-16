package com.example.crescendo_piano;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CodeViewPagerAdapter extends RecyclerView.Adapter<CodeViewHolderPage> {
    private ArrayList<CodeDataPage> listData;

    CodeViewPagerAdapter(ArrayList<CodeDataPage> data){
        this.listData=data;
    }

    @NonNull
    @Override
    public CodeViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);

        return new CodeViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolderPage holder, int position) {
        if(holder instanceof CodeViewHolderPage){
            CodeViewHolderPage viewHolder = (CodeViewHolderPage) holder;
            viewHolder.onBind(listData.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }
}
