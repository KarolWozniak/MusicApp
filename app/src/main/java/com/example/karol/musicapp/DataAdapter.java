package com.example.karol.musicapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import Json.Video;

/**
 * Created by Karol on 2018-03-27.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{
    private static MainActivity mainActivity;
    private Video data;
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;
        private String Url;
        public ViewHolder(TextView v) {
            super(v);
            v.setOnClickListener(this);
            this.mTextView = v;
        }
        public void setUrl(String url)
        {
            this.Url=url;
        }

        @Override
        public void onClick(View view) {
            Log.i("MainActivity",this.Url);
            mainActivity.downloadUrl(this.Url);
        }
    }

    public DataAdapter(Video data,MainActivity mainActivity)
    {
        this.data=data;
        this.mainActivity=mainActivity;
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
        holder.setUrl(getUrl(position));

    }

    public  String whichFun(int position)
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

    public String getUrl(int number)
    {
        switch(number)
        {
            case 0:
            {
                return this.data.getVidInfo().get0().getDloadUrl();
            }
            case 1:
            {
                return this.data.getVidInfo().get1().getDloadUrl();
            }
            case 2:
            {
                return this.data.getVidInfo().get2().getDloadUrl();
            }
            case 3:
            {
                return this.data.getVidInfo().get3().getDloadUrl();
            }
            case 4:
            {
                return this.data.getVidInfo().get4().getDloadUrl();
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
