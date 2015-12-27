package com.food.trace;


import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CompleteInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_info);
		// 点击保存用户信息的事件
		Button save = (Button) this.findViewById(R.id.complete_info_save);
		save.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				String userName = ((EditText) CompleteInfoActivity.this.findViewById(R.id.complete_info_username)).getText().toString();
				String factory = ((EditText) CompleteInfoActivity.this.findViewById(R.id.complete_info_factory)).getText().toString();
				String type = ((EditText) CompleteInfoActivity.this.findViewById(R.id.complete_info_type)).getText().toString();
				if (userName == null || userName.equals("") || factory == null || factory.equals("") || type == null || type.equals("")) {
					Toast.makeText(CompleteInfoActivity.this, "输入不能为空！", 100).show();
					return;
				}
				
				completeInfo(userName, factory, type);
			}
		});
	}
	
	
	private void completeInfo(String userName, String factory, String type){
		
		final User curUser = BmobUser.getCurrentUser(this, User.class);
		if (curUser != null) {
			
			Log.d("bmob", "getObjectId = " + curUser.getObjectId());
			Log.d("bmob", "getUsername = " + curUser.getUsername());
			Log.d("bmob", "getEmail = "    + curUser.getEmail());
			
			final User newUser = new User();
			newUser.setUsername(userName);
			newUser.setFactory(factory);
			newUser.setType(type);
			newUser.update(CompleteInfoActivity.this, curUser.getObjectId(), new UpdateListener(){
				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(CompleteInfoActivity.this, "完善失败", 100).show();
				}

				@Override
				public void onSuccess() {
					Toast.makeText(CompleteInfoActivity.this, "完善成功", 100).show();
					startActivity(new Intent(CompleteInfoActivity.this, UserHomeActivity.class));
					CompleteInfoActivity.this.finish();
				}
			});
		} else {
			Toast.makeText(CompleteInfoActivity.this, "还未登陆", 100).show();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complete_info, menu);
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
