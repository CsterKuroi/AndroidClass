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

public class Second extends Activity {
	Button mp=null,wz=null,txt=null;
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_2);
		
	    Intent intent=getIntent();
		final Bitmap bitmap;
		bitmap=intent.getParcelableExtra("bitmap");
		//Log.v("Second","bitmap"+bitmap.toString());
       // imageview.setImageBitmap(bitmap);
		mp = (Button)findViewById(R.id.button_mp);
		mp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Second.this,Mp.class);
				intent.putExtra("bitmap",bitmap);
				startActivity(intent);
			}
			
		});
		wz = (Button) this.findViewById(R.id.button_int);
		wz.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Second.this,Wz.class);
				intent.putExtra("bitmap",bitmap);
				startActivity(intent);
			}
			
		});
		txt = (Button) this.findViewById(R.id.button_txt);
		txt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Second.this,Txt.class);
				intent.putExtra("bitmap",bitmap);
				Log.v("Second",bitmap.toString());
				Log.v("Second","Intent"+intent.toString());
				startActivity(intent);
			}
			
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}

}
