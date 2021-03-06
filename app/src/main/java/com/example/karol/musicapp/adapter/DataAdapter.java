package com.example.karol.musicapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karol.musicapp.activities.MainActivity;
import com.example.karol.musicapp.R;

import com.example.karol.musicapp.json.downloadURL;

/**
 * Created by Karol on 2018-03-27.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>{
    private static MainActivity mainActivity;
    private downloadURL data;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;
        private String Url;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            this.mTextView = v.findViewById(R.id.title);
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

    public DataAdapter(downloadURL data, MainActivity mainActivity) {
        this.data=data;
        this.mainActivity=mainActivity;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_item, parent, false);
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
       return "Only one option is available now!!";
    }

    public String getUrl(int number)
    {
        return data.getDownloadURL();
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
