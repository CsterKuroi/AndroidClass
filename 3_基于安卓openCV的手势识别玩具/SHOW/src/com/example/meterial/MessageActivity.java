package com.example.meterial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MessageActivity extends Activity{
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_message);
		 
		 View fab = findViewById(R.id.fab_button);
		 fab.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                Intent intent = new Intent(MessageActivity.this,ColorActivity.class);
	                startActivity(intent);
	            }
	        });
		 
		 
	 }
}