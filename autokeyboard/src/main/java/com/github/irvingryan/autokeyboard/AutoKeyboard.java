package com.github.irvingryan.autokeyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yanwentao on 2017/12/28.
 */

public class AutoKeyboard implements View.OnFocusChangeListener {

    private static final int CLEAR_FOCUS = 0;
    private static final int KEYBOARD_ON = 1;
    private static final int KEYBOARD_OFF = 2;

    private int layoutBottom;
    private InputMethodManager im;
    private int[] coords;
    private boolean isKeyboardShow;
    private AutoKeyboard.SoftKeyboardChangesThread softKeyboardThread;
    private KeyboardVisibilityCallback mCallback;

    private ViewGroup layout;
    private View tempView; // reference to a focused EditText

    public AutoKeyboard(final Activity activity) {
        layout = activity.findViewById(android.R.id.content);
        this.im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        getScrollView(layout, activity);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dispatchTouchEvent(activity, motionEvent);
                return false;
            }
        });
        keyboardHideByDefault();
    }

    private void keyboardHideByDefault() {
        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
    }

    private void initEditTexts(ViewGroup viewgroup) {
        int childCount = viewgroup.getChildCount();
        for (int i = 0; i <= childCount - 1; i++) {
            View v = viewgroup.getChildAt(i);

            if (v instanceof ViewGroup) {
                initEditTexts((ViewGroup) v);
            }
            if (v instanceof EditText) {
                EditText editText = (EditText) v;
                editText.setOnFocusChangeListener(this);
                editText.setCursorVisible(true);
            }
        }
    }

    private int getLayoutCoordinates() {
        layout.getLocationOnScreen(coords);
        return coords[1] + layout.getHeight();
    }


    private void getScrollView(ViewGroup viewGroup, final Activity activity) {
        if (null == viewGroup) {
            return;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ScrollView) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        dispatchTouchEvent(activity, motionEvent);
                        return false;
                    }
                });
            } else if (view instanceof AbsListView) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        dispatchTouchEvent(activity, motionEvent);
                        return false;
                    }
                });
            } else if (view instanceof RecyclerView) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        dispatchTouchEvent(activity, motionEvent);
                        return false;
                    }
                });
            } else if (view instanceof ViewGroup) {
                this.getScrollView((ViewGroup) view, activity);
            }

            if (view.isClickable() && view instanceof TextView && !(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        dispatchTouchEvent(activity, motionEvent);
                        return false;
                    }
                });
            }
        }
    }

    /**
     * dispatch custom TouchEvent
     *
     * @param mActivity
     * @param ev
     * @return
     */
    public boolean dispatchTouchEvent(Activity mActivity, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = mActivity.getCurrentFocus();
            if (null != v && isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return false;
    }

    /**
     * @param v clicked view
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            Rect rect = new Rect();
            v.getHitRect(rect);
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                return false;
            }
        }
        return true;
    }

    private void hideSoftInput(IBinder token) {
        if (token != null) {
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            tempView = v;
            if (!isKeyboardShow) {
                layoutBottom = getLayoutCoordinates();
                softKeyboardThread.keyboardOpened();
                isKeyboardShow = true;
            }
        }
    }


    public void setKeyboardVisibilityCallback(KeyboardVisibilityCallback callback) {
        initEditTexts(layout);
        this.mCallback = callback;
        this.coords = new int[2];
        this.isKeyboardShow = false;
        if (softKeyboardThread == null) {
            this.softKeyboardThread = new AutoKeyboard.SoftKeyboardChangesThread();
            this.softKeyboardThread.start();
        }
    }

    public void openSoftKeyboard() {
        if (!isKeyboardShow && im != null) {
            layoutBottom = getLayoutCoordinates();
            im.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
            if (softKeyboardThread != null) {
                softKeyboardThread.keyboardOpened();
            }
            isKeyboardShow = true;
        }
    }

    public void closeSoftKeyboard() {
        if (isKeyboardShow && im != null) {
            im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            isKeyboardShow = false;
        }
    }

    /**
     * call when activity onDestroy invoked
     */
    public void unRegisterSoftKeyboardCallback() {
        if (softKeyboardThread != null) {
            softKeyboardThread.stopThread();
            softKeyboardThread = null;
        }
        if (mHandler != null) {
            mHandler.removeMessages(CLEAR_FOCUS);
            mHandler.removeMessages(KEYBOARD_ON);
            mHandler.removeMessages(KEYBOARD_OFF);
            mHandler = null;
        }
    }


    public interface KeyboardVisibilityCallback {
        void onSoftKeyboardHide();

        void onSoftKeyboardShow();
    }

    private class SoftKeyboardChangesThread extends Thread {
        private AtomicBoolean started;

        public SoftKeyboardChangesThread() {
            started = new AtomicBoolean(true);
        }

        @Override
        public void run() {
            while (started.get()) {
                // Wait until keyboard is requested to open
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                int currentBottomLocation = getLayoutCoordinates();

                // There is some lag between open soft-keyboard function and when it really appears.
                while (currentBottomLocation == layoutBottom && started.get()) {
                    currentBottomLocation = getLayoutCoordinates();
                }

                if (started.get() && mHandler != null)
                    mHandler.obtainMessage(KEYBOARD_ON).sendToTarget();

                // When keyboard is opened from EditText, initial bottom location is greater than layoutBottom
                // and at some moment equals layoutBottom.
                // That broke the previous logic, so I added this new loop to handle this.
                while (currentBottomLocation >= layoutBottom && started.get()) {
                    currentBottomLocation = getLayoutCoordinates();
                }

                // Now Keyboard is shown, keep checking layout dimensions until keyboard is gone
                while (currentBottomLocation != layoutBottom && started.get()) {
                    synchronized (this) {
                        try {
                            wait(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    currentBottomLocation = getLayoutCoordinates();
                }

                if (started.get() && mHandler != null)
                    mHandler.obtainMessage(KEYBOARD_OFF).sendToTarget();

                // if keyboard has been opened clicking and EditText.
                if (isKeyboardShow && started.get())
                    isKeyboardShow = false;

                // if an EditText is focused, remove its focus (on UI thread)
                if (started.get())
                    mHandler.obtainMessage(CLEAR_FOCUS).sendToTarget();
            }
        }

        public void keyboardOpened() {
            synchronized (this) {
                notify();
            }
        }

        public void stopThread() {
            synchronized (this) {
                started.set(false);
                notify();
            }
        }

    }

    // This handler will clear focus of selected EditText
    // Processing callback with ui thread
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
                case CLEAR_FOCUS:
                    if (tempView != null) {
                        tempView.clearFocus();
                        tempView = null;
                    }
                    break;
                case KEYBOARD_ON:
                    if (mCallback != null) {
                        mCallback.onSoftKeyboardShow();
                    }
                    break;
                case KEYBOARD_OFF:
                    if (mCallback != null) {
                        mCallback.onSoftKeyboardHide();
                    }
                    break;
            }

        }
    };


}
