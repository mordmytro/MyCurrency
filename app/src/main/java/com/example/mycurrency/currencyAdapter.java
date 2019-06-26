package com.example.mycurrency;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class currencyAdapter extends RecyclerView.Adapter<currencyAdapter.viewHolder> {
    private List<String> curList;


    public currencyAdapter (List<String> newList) {
        curList = newList;
    }

    public void func(List<String> list1) {
        Log.e("fdd","" + list1.get(0));
        curList = list1;
        notifyDataSetChanged();
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItemId = R.layout.cur_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutItemId,parent,false);

        viewHolder viewHolder1 = new viewHolder(view);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder (viewHolder holder, int position) {
        holder.bind(curList,position);
    }

    @Override
    public int getItemCount () {
        return curList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        public TextView newTextView;


        public viewHolder(View itemView) {
            super(itemView);
            newTextView = itemView.findViewById(R.id.result);
        }

        void bind(List<String> list, int listindex) {
            newTextView.setText(list.get(listindex));
        }
    }
}
