/**
 * 
 */
package com.example.keepfit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class shengaoActivity extends Activity{
	DBHelper mHelper;
	EditText userNameEditText;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shengao);
		
		mHelper = new DBHelper(this);
		mHelper.open();
		
		Button b0 = (Button)findViewById(R.id.button0);
	    b0.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(shengaoActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		shengaoActivity.this.finish();
		    }			
	    });
	    
	    userNameEditText = (EditText) findViewById(R.id.shengao);
	    Button b1 = (Button)findViewById(R.id.button1);
	    b1.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		String get = userNameEditText.getText().toString();
	    		mHelper.updateUser(DBHelper.U_HEIGHT,get);
	    			    
	    		Intent intent0 = new Intent(shengaoActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		shengaoActivity.this.finish();
		    }			
	    });
	}
}
