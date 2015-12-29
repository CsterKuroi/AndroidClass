/**
 * 
 */
package com.example.keepfit;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class xingbieActivity extends Activity{
	
	DBHelper mHelper;
	RadioGroup radioGroup;
	String get;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xingbie);
		
		mHelper = new DBHelper(this);
		mHelper.open();
		
		Button b0 = (Button)findViewById(R.id.button0);
	    b0.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(xingbieActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		xingbieActivity.this.finish();
		    }			
	    });
	    
	    Button b1 = (Button)findViewById(R.id.button1);
	    radioGroup = (RadioGroup) findViewById(R.id.menu);
	        
	    radioGroup.setOnCheckedChangeListener(mChangeRadio);
	    
	    b1.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		
	    		mHelper.updateUser(DBHelper.U_SEX,get);
	    			    
	    		Intent intent0 = new Intent(xingbieActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		xingbieActivity.this.finish();
		    }			
	    });
	}
	private RadioGroup.OnCheckedChangeListener mChangeRadio = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
   
            RadioButton rb = (RadioButton) findViewById(checkedId);
            get = rb.getText().toString();
        }
    };
}
