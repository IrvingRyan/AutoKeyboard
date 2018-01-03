package com.github.irvingryan.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.irvingryan.autokeyboard.AutoKeyboard;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListViewActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;
    private AutoKeyboard autoKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        ButterKnife.bind(this);
        autoKeyboard = new AutoKeyboard(this);
        autoKeyboard.setKeyboardVisibilityCallback(new AutoKeyboard.KeyboardVisibilityCallback() {
            @Override
            public void onSoftKeyboardHide() {
                Toast.makeText(ListViewActivity.this, "SoftKeyboardHide", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSoftKeyboardShow() {
                Toast.makeText(ListViewActivity.this, "SoftKeyboardShow", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("No. " + i);
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListViewActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (autoKeyboard != null) {
            autoKeyboard.unRegisterSoftKeyboardCallback();
        }
        super.onDestroy();

    }

}
