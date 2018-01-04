# **AutoKeyboard**

Soft keyboard show/hidden events in Android and Imitation iOS keyboard automatic hidden

ScreenShot
--
![](https://github.com/IrvingRyan/AutoKeyboard/blob/master/art/screen_01.gif)

Usage
--
##### Gradle:

```groovy
   implementation 'com.github.irvingryan:autokeyboard:1.0.0'
```

Demo
--

##### Java:
```Java
    private AutoKeyboard autoKeyboard;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoKeyboard = new AutoKeyboard(this);
        autoKeyboard.setKeyboardVisibilityCallback(new AutoKeyboard.KeyboardVisibilityCallback() {
            @Override
            public void onSoftKeyboardHide() {
               //code here 
            }

            @Override
            public void onSoftKeyboardShow() {
               //code here
            }
        });
    }

    protected void onDestroy() {
        if (autoKeyboard != null) {
            autoKeyboard.unRegisterSoftKeyboardCallback();
        }
        super.onDestroy();

    }
```

##### Xml:

you must add this line to your activity reference in your manifest.xml

```xml
	android:windowSoftInputMode="adjustResize"
```


Thanks
--

* [HideKeyboard](https://github.com/yingLanNull/HideKeyboard)
* [Catch soft keyboard show/hidden events in Android](https://felhr85.net/2014/05/04/catch-soft-keyboard-showhidden-events-in-android/)

License
--
    Copyright (C) 2018 IrvingRyan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

