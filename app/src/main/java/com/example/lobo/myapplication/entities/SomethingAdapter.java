package com.example.lobo.myapplication.entities;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.lobo.myapplication.R;

import java.util.List;

/**
 * Created by lobo on 05/05/18.
 */

public class SomethingAdapter extends RecyclerView.Adapter<SomethingAdapter.SomethingHolder>{

    List<Something> listSomething;

    public SomethingAdapter(List<Something> listSomething) {
        this.listSomething = listSomething;
    }

    @Override
    public SomethingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.some_list,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new SomethingHolder(vista);
    }

    @Override
    public void onBindViewHolder(SomethingHolder holder, int position) {
        holder.txtID.setText(String.valueOf(listSomething.get(position).getId()));
        holder.txtDescription.setText(listSomething.get(position).getDescription().toString());
    }

    @Override
    public int getItemCount() {
        return listSomething.size();
    }

    public class SomethingHolder extends RecyclerView.ViewHolder{

        TextView txtID,txtDescription;

        public SomethingHolder(View itemView) {
            super(itemView);
            txtID= (TextView) itemView.findViewById(R.id.txtID);
            txtDescription= (TextView) itemView.findViewById(R.id.txtDescription);
        }
    }
}