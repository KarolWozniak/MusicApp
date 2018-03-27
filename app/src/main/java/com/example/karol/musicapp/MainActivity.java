package com.example.karol.musicapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editText) EditText url;
    @BindView(R.id.text)TextView text;
    @BindView(R.id.button)Button button;
    @BindView(R.id.my_recycler_view)RecyclerView audioList;

    private DataApiHelper data;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        audioList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        audioList.setLayoutManager(mLayoutManager);
    }

    public void check(View view){
        Parser parser = new Parser(this.url.getText().toString());
        text.setText(parser.getRight_link());
        this.data=new DataApiHelper(parser.getRight_link(),this);
    }

    public void show()
    {
        listAdapter = new DataAdapter(data.getVideo());
        audioList.setAdapter(listAdapter);
        text.setText(data.getVideo().getVidTitle());
    }

}