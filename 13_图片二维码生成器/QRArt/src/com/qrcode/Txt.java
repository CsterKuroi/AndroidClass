package com.qrcode;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Txt extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_3);
		
		Intent intent=getIntent();
		final Bitmap bitmap;
		bitmap=intent.getParcelableExtra("bitmap");
		Log.v("Txt",bitmap.toString());
		final EditText input=(EditText)findViewById(R.id.input_txt);
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
				String contentString = input.getText().toString();
				intent=new Intent(Txt.this,Qrcreat.class);
				intent.putExtra("bitmap",bitmap);
				if( contentString!=null){
					Log.v("Txt",contentString);
				}
				intent.putExtra("content",contentString);
				Log.v("Txt",bitmap.toString());
				startActivity(intent);
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.txt, menu);
		return true;
	}

}
