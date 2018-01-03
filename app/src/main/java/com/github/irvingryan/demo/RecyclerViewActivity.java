package com.github.irvingryan.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.github.irvingryan.autokeyboard.AutoKeyboard;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private AutoKeyboard autoKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        ButterKnife.bind(this);
        autoKeyboard = new AutoKeyboard(this);
        autoKeyboard.setKeyboardVisibilityCallback(new AutoKeyboard.KeyboardVisibilityCallback() {
            @Override
            public void onSoftKeyboardHide() {
                Toast.makeText(RecyclerViewActivity.this, "SoftKeyboardHide", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSoftKeyboardShow() {
                Toast.makeText(RecyclerViewActivity.this, "SoftKeyboardShow", Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("No. " + i);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(this, list));
    }

    @Override
    protected void onDestroy() {
        if (autoKeyboard != null) {
            autoKeyboard.unRegisterSoftKeyboardCallback();
        }
        super.onDestroy();

    }


}
