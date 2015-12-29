/**
 * 
 */
package com.example.keepfit;

import java.util.HashMap;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter.UsernameFilterGeneric;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class woActivity extends Activity {
	DBHelper mHelper;
	TextView userNameText;
	TextView userSexText;
	TextView userWeightText;
	TextView userHeightText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wo);
		mHelper = new DBHelper(this);
		mHelper.open();
		
		Button b0 = (Button)findViewById(R.id.button0);
	    b0.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(woActivity.this,shangdianActivity.class);
	    		startActivity(intent0);
	    		woActivity.this.finish();
		    }			
	    });
	    Button b1 = (Button)findViewById(R.id.button1);
	    b1.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(woActivity.this,kechengActivity.class);
	    		startActivity(intent0);
	    		woActivity.this.finish();
		    }			
	    });
	    Button b3 = (Button)findViewById(R.id.button3);
	    b3.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(woActivity.this,yonghumingActivity.class);
	    		startActivity(intent0);
		    }			
	    });
	    Button b4 = (Button)findViewById(R.id.button4);
	    b4.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(woActivity.this,xingbieActivity.class);
	    		startActivity(intent0);
		    }			
	    });
	    Button b5 = (Button)findViewById(R.id.button5);
	    b5.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(woActivity.this,tizhongActivity.class);
	    		startActivity(intent0);
		    }			
	    });
	    Button b6 = (Button)findViewById(R.id.button6);
	    b6.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(woActivity.this,shengaoActivity.class);
	    		startActivity(intent0);
		    }			
	    });
	    
	    userNameText = (TextView) findViewById(R.id.yonghuming);
	    userSexText = (TextView) findViewById(R.id.xingbie);
	    userWeightText = (TextView) findViewById(R.id.tizhong);
	    userHeightText = (TextView) findViewById(R.id.shengao);
	    HashMap<String,Object> value = mHelper.getUser();
	    String sname = (String)(value.get(DBHelper.U_NAME) );
	    userNameText.setText(sname);
	    userSexText.setText((String)(value.get(DBHelper.U_SEX)) );
	    userWeightText.setText((String)(value.get(DBHelper.U_WEIGHT)) );
	    userHeightText.setText((String)(value.get(DBHelper.U_HEIGHT)) );    
	    
	}
}
