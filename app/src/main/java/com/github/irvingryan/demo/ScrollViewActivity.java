package com.github.irvingryan.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.irvingryan.autokeyboard.AutoKeyboard;

import butterknife.ButterKnife;

public class ScrollViewActivity extends AppCompatActivity {

    private AutoKeyboard autoKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        ButterKnife.bind(this);
        autoKeyboard = new AutoKeyboard(this);
        autoKeyboard.setKeyboardVisibilityCallback(new AutoKeyboard.KeyboardVisibilityCallback() {
            @Override
            public void onSoftKeyboardHide() {
                Toast.makeText(ScrollViewActivity.this, "SoftKeyboardHide", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSoftKeyboardShow() {
                Toast.makeText(ScrollViewActivity.this, "SoftKeyboardShow", Toast.LENGTH_SHORT).show();
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
