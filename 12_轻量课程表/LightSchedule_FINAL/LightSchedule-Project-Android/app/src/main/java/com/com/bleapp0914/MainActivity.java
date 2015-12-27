package com.com.bleapp0914;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import java.util.HashMap;

import com.com.bleapp0914.fragment.ConfigBleFragment;
import com.com.bleapp0914.fragment.HackFragment;
import com.com.bleapp0914.fragment.NavigationDrawerFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private String TAG = MainActivity.class.getSimpleName();
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    private HashMap<String, Fragment> fragmentHashMap;


    public BluetoothDevice bleDevice;
    public BluetoothManager bluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    public boolean mScanning;
    public Handler mHandler;
    // Stops scanning after 10 seconds.
    public static final long SCAN_PERIOD = 2000;

    public BluetoothGatt mBluetoothGatt;
    public BluetoothGattCharacteristic mBluetoothGattCharacteristic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //initial fragments
        fragmentHashMap = new HashMap<String, Fragment>();
        fragmentHashMap.put(getString(R.string.title_config), new ConfigBleFragment());
        fragmentHashMap.put(getString(R.string.title_hack), new HackFragment());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, fragmentHashMap.get(getString(R.string.title_config))).commit();

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //initial system
        mHandler = new Handler();

        //function
        // Initializes Bluetooth adapter.
        bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled.
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (position == 0) {
            fragmentManager.beginTransaction().replace(R.id.container, fragmentHashMap.get(getString(R.string.title_config))).commit();
        }else if (position == 1){
            fragmentManager.beginTransaction().replace(R.id.container, fragmentHashMap.get(getString(R.string.title_hack))).commit();
        }else {
            Log.e(TAG, "*****************************");
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_config);
                break;
            case 2:
                mTitle = getString(R.string.title_hack);
                break;
            case 3:
                mTitle = getString(R.string.title_test);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
