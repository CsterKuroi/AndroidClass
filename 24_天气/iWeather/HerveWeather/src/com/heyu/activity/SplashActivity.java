package com.heyu.activity;

import com.example.herveweather.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity {
	
	private final int SPLASH_DISPLAY_LENGHT = 3000; // �ӳ�����  
	boolean isFirstIn = false;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    private static final String SHAREDPREFERENCES_NAME = "first_pref";  // SharedPreferences�洢�ļ���

	
	
	protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_splash);
        init(); 
    }
	
	
	// ʹ��handler�����첽��Ϣ
	 @SuppressLint("HandlerLeak") 
	 private Handler mHandler = new Handler() {

	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case GO_HOME:
	                goHome();
	                break;
	            case GO_GUIDE:
	                goGuide();
	                break;
	            }
	            super.handleMessage(msg);
	        }
	    };
	
	
	// ��SharedPreferences�ж�ȡ��Ҫ������
	private void init() 
	{
        // ʹ��SharedPreferences����¼�����ʹ�ô���
        SharedPreferences preferences = getSharedPreferences( SHAREDPREFERENCES_NAME, MODE_PRIVATE);

        // ȡ����Ӧ��ֵ�����û�и�ֵ��˵����δд�룬��true��ΪĬ��ֵ
        isFirstIn = preferences.getBoolean("isFirstIn", true);

        // �жϳ�����ڼ������У�����ǵ�һ����������ת���������棬������ת��������
        if (!isFirstIn) {
            // ʹ��Handler��postDelayed������3���ִ����ת��MainActivity
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DISPLAY_LENGHT);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DISPLAY_LENGHT);
        }

    }
	
	private void goGuide() {
        Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
}
