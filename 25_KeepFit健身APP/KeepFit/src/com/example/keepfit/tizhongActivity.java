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

public class tizhongActivity extends Activity{
	
	DBHelper mHelper;
	EditText userNameEditText;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tizhong);
		
		mHelper = new DBHelper(this);
		mHelper.open();
		
		Button b0 = (Button)findViewById(R.id.button0);
	    b0.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(tizhongActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		tizhongActivity.this.finish();
		    }			
	    });
	    
	    userNameEditText = (EditText) findViewById(R.id.tizhong);
	    Button b1 = (Button)findViewById(R.id.button1);
	    b1.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		String get = userNameEditText.getText().toString();
	    		mHelper.updateUser(DBHelper.U_WEIGHT,get);
	    			    
	    		Intent intent0 = new Intent(tizhongActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		tizhongActivity.this.finish();
		    }			
	    });
	}
}
