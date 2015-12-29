package com.example.keepfit;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Intent intent=new Intent();
		Timer timer=new Timer();
		TimerTask task=new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				intent.setClass(MainActivity.this,shangdianActivity.class);
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
			}			
		};
		timer.schedule(task,1000*2);
	}
}
