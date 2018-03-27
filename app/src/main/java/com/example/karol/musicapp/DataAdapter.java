package com.example.karol.musicapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import Json.Video;

/**
 * Created by Karol on 2018-03-27.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{
    private Video data;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public DataAdapter(Video data) {
        this.data=data;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.support_simple_spinner_dropdown_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(whichFun(position));

    }

    String whichFun(int position)
    {
        switch(position)
        {
            case 0:
            {
            return this.data.getVidInfo().get0().getMp3size();
            }
            case 1:
            {
                return this.data.getVidInfo().get1().getMp3size();
            }
            case 2:
            {
                return this.data.getVidInfo().get2().getMp3size();
            }
            case 3:
            {
                return this.data.getVidInfo().get3().getMp3size();
            }
            case 4:
            {
                return this.data.getVidInfo().get4().getMp3size();
            }
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return 5;
    }
}
