package com.food.trace;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import cn.bmob.v3.Bmob;

public class MainActivity extends Activity {
	
	private final int SPLASH_DISPLAY_LENGHT = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, "81426109af11587a91141e2bb4f14fa0");
		setContentView(R.layout.activity_main);
		
		new Handler().postDelayed(new Runnable(){ 
			 
	         @Override
	         public void run() { 
	             Intent homeIntent = new Intent(MainActivity.this,HomeActivity.class); 
	             MainActivity.this.startActivity(homeIntent); 
	                 MainActivity.this.finish(); 
	         } 
	        }, SPLASH_DISPLAY_LENGHT); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
