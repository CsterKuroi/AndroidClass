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
import android.widget.TextView;

public class yonghumingActivity extends Activity {
	
	DBHelper mHelper;
	EditText userNameEditText;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yonghuming);
		
		mHelper = new DBHelper(this);
		mHelper.open();
	
		Button b0 = (Button)findViewById(R.id.button0);
	    b0.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(yonghumingActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		yonghumingActivity.this.finish();
		    }			
	    });
	    
	    Button b1 = (Button)findViewById(R.id.button1);
	    userNameEditText = (EditText) findViewById(R.id.yonghuming);
	    b1.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		String get = userNameEditText.getText().toString();
	    		mHelper.updateUser(DBHelper.U_NAME,get);
	    		Intent intent0 = new Intent(yonghumingActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		yonghumingActivity.this.finish();
		    }			
	    });
	}
}
