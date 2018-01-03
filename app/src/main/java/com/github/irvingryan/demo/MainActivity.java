package com.github.irvingryan.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_normal)
    void goNormal() {
        startActivity(new Intent(this, NormalActivity.class));
    }

    @OnClick(R.id.btn_scrollview)
    void goScrollView() {
        startActivity(new Intent(this, ScrollViewActivity.class));
    }

    @OnClick(R.id.btn_listview)
    void goListView() {
        startActivity(new Intent(this, ListViewActivity.class));
    }

    @OnClick(R.id.btn_recyclerview)
    void goRecyclerView() {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }
}
