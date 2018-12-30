package com.ctyeung.ndkex1;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/*
 * References:
 *
 * Navigation drawer by Rohit Kumar Kanojia
 * https://medium.com/quick-code/android-navigation-drawer-e80f7fc2594f
 *
 *
 * Author: Frank Ableson
 * https://www.ibm.com/developerworks/opensource/tutorials/os-androidndk/os-androidndk-pdf.pdf
 */

public class MainActivity extends BaseNavDrawerActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        super.initDrawer(R.id.activity_main);
    }

    public native String stringFromJNI();
    public native String string2FromJNI();
}
