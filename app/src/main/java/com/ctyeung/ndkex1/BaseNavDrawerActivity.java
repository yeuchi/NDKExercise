package com.ctyeung.ndkex1;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseNavDrawerActivity extends AppCompatActivity
{
    public DrawerLayout dl;
    public ActionBarDrawerToggle t;
    public NavigationView nv;
    protected Context mContext;

    public void initDrawer(int layoutId)
    {
        mContext = this.getApplicationContext();

        dl = (DrawerLayout)findViewById(layoutId);
        t = new ActionBarDrawerToggle(this, dl,R.string.open, R.string.close);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;

                switch(id)
                {
                    case R.id.item_convolution:
                        Toast.makeText(mContext, "Convolution",Toast.LENGTH_SHORT).show();
                        intent = new Intent(mContext, ConvolutionActivity.class);
                        break;

                    case R.id.item_hough_line:
                        Toast.makeText(mContext, "Hough lines",Toast.LENGTH_SHORT).show();
                        intent = new Intent(mContext, HoughLineActivity.class);
                        break;

                    case R.id.item_hough_circle:
                        Toast.makeText(mContext, "Hough circles",Toast.LENGTH_SHORT).show();
                        intent = new Intent(mContext, HoughCircleActivity.class);
                        break;

                    default:
                        Toast.makeText(mContext, "Unknown",Toast.LENGTH_SHORT).show();
                        return false;
                }

                startActivity(intent);
                return true;

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
