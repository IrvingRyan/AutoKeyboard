package com.github.irvingryan.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.irvingryan.autokeyboard.AutoKeyboard;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class NormalActivity extends AppCompatActivity {

    private AutoKeyboard autoKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        ButterKnife.bind(this);
        autoKeyboard = new AutoKeyboard(this);
        autoKeyboard.setKeyboardVisibilityCallback(new AutoKeyboard.KeyboardVisibilityCallback() {
            @Override
            public void onSoftKeyboardHide() {
                Toast.makeText(NormalActivity.this, "SoftKeyboardHide", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSoftKeyboardShow() {
                Toast.makeText(NormalActivity.this, "SoftKeyboardShow", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.btn_toggle)
    void toggleOn() {
        autoKeyboard.openSoftKeyboard();
    }
}
