package com.leafli7.lightschedule;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lightschedule.R;
import com.leafli7.lightschedule.Activity.QrCaptureActivity;
import com.leafli7.lightschedule.Entity.WeekSchedule;
import com.leafli7.lightschedule.Fragment.MainScheduleFragment;
import com.leafli7.lightschedule.Utils.Constant;
import com.leafli7.lightschedule.Utils.OwnDbHelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zxing.activity.CaptureActivity;

import java.util.HashMap;

/**
 * @author leafli7
 */
public class MainActivity extends AppCompatActivity {
    /*
    TODO : 添加课程时应该列出已有课程,若没有再开新activity添加.
     */
    String TAG = Constant.TAG + getClass().getSimpleName();

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    OwnDbHelper dbHelper;

    FragmentManager fragmentManager;
    HashMap<String, Fragment> fragmentHashMap;

    public static final String mainScheduleFragment = "mainScheduleFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();

        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragmentHashMap = new HashMap<String, Fragment>();
        fragmentHashMap.put(mainScheduleFragment, new MainScheduleFragment());
        fragmentManager.beginTransaction().add(R.id.main_container, fragmentHashMap.get(mainScheduleFragment)).commit();


        initialDatabase();
        initialStatue();
        Constant.initial(this);
        initialFindView();
        initialToolbar();
        initialNav();
    }

    private void initialStatue() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(ContextCompat.getColor(this, R.color.colorMainDark));
        }
    }

    private void initialDatabase(){
        dbHelper = new OwnDbHelper(this);
        WeekSchedule weekSchedule = Constant.weekSchedule;
    }

    private void initialFindView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.colorMainDark));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    private void initialToolbar() {
        mToolbar.setTitle(getString(R.string.title_activity_main));
        setSupportActionBar(mToolbar);
        // use own style rules for tab layout
    }

    private void initialNav() {
        // Click events for Navigation Drawer
        LinearLayout navButton = (LinearLayout) findViewById(R.id.mainScheduleNavButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "nav main schedule clicked!", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragmentHashMap.get(mainScheduleFragment)).commit();
                mDrawerLayout.closeDrawers();
            }
        });

        LinearLayout qrCodeNavButton = (LinearLayout) findViewById(R.id.qrCodeNavButton);
        qrCodeNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QrCaptureActivity.class);
                startActivityForResult(i, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            final String res = data.getExtras().getString("result");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "capture : " + res, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

}
