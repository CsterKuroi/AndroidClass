package com.qrcode;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Wz extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_http);
		Intent intent=getIntent();
		final Bitmap bitmap;
		bitmap=intent.getParcelableExtra("bitmap");
		final EditText input=(EditText)findViewById(R.id.input_http);
		   Button before=(Button)findViewById(R.id.button_before);
	        before.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		Button next=(Button)findViewById(R.id.button_next);
		next.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent intent;
				String contentString ="http://"+ input.getText().toString();
				intent=new Intent(Wz.this,Qrcreat.class);
				intent.putExtra("bitmap",bitmap);
				intent.putExtra("content",contentString);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wz, menu);
		return true;
	}

}
