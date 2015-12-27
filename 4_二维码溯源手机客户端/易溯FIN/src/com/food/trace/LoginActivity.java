package com.food.trace;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// button login
		Button login = (Button) this.findViewById(R.id.login_login);
		login.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				
				String phone = ((EditText) LoginActivity.this.findViewById(R.id.login_username)).getText().toString();
				String  pswd = ((EditText) LoginActivity.this.findViewById(R.id.login_password)).getText().toString();
				
				if (phone == null || phone.equals("")) Toast.makeText(LoginActivity.this, "�û�������Ϊ�գ�", 100).show();
				if (pswd  == null || pswd.equals(""))  Toast.makeText(LoginActivity.this, "���벻��Ϊ�գ�", 100).show();
				
				loginByPhonePwd(phone, pswd);
			}
		});
	}
	
	private void loginByPhonePwd(String mobliePhoneNumber, String password) {
    	BmobUser.loginByAccount(this, mobliePhoneNumber, password, new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                // TODO Auto-generated method stub
                if (user != null) {
                    Log.i("Bmob","�û���½�ɹ�");
                    Toast.makeText(LoginActivity.this, "��¼�ɹ���", Toast.LENGTH_SHORT).show();           
                    // ��ʼ�����û��Ļ���·��
        			BmobConfiguration config = new BmobConfiguration.Builder(LoginActivity.this).customExternalCacheDir(user.getUsername()).build();
        			BmobPro.getInstance(LoginActivity.this).initConfig(config);
        			
        			UserHomeActivity.user=user;
        			
        			Intent intent = new Intent();
        			intent.setClass(LoginActivity.this, UserHomeActivity.class);
        			startActivity(intent);
        			LoginActivity.this.finish();
                }
                else {
                	Log.i("Bmob","�û���½ʧ��");
                	Toast.makeText(LoginActivity.this, "��¼ʧ�ܣ�",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
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
