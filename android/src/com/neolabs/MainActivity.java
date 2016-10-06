package com.neolabs;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class MainActivity extends FragmentActivity implements AndroidFragmentApplication.Callbacks {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String inventoryJson = getIntent().getStringExtra("INVENTORY");
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(android.R.id.content, GameFragment.newInstance(inventoryJson));
        trans.commit();
    }


    @Override
    public void exit() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}