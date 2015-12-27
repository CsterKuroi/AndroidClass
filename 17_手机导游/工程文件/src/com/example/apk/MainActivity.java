package com.example.apk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends Activity {

	public BaiduMap baiduMap = null;
	public LocationClient locationClient = null;  
	MapView mMapView = null; 
	private RadioButton bt1;
	private RadioButton bt2;
	private RadioButton bt3;
	private Context mContext;
	BitmapDescriptor mCurrentMarker = null;  
    boolean isFirstLoc = true;// �Ƿ��״ζ�λ
    private LinearLayout splash;  
    private TextView tv;  
  
    private static final int STOPSPLASH = 0;  
    // time in milliseconds  
    private static final long SPLASHTIME = 500;  
  
    private Handler splashHandler = new Handler() {  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case STOPSPLASH:  
                SystemClock.sleep(4000);      
                splash.setVisibility(View.GONE);  
                    break;  
            }  
            super.handleMessage(msg);  
        }  
    };  
    public BDLocationListener myListener = new BDLocationListener() {  
        @Override  
        public void onReceiveLocation(BDLocation location) {  
            // map view ���ٺ��ڴ����½��յ�λ��  
            if (location == null || mMapView == null)  
                return;  
              
            MyLocationData locData = new MyLocationData.Builder()  
                    .accuracy(location.getRadius())  
                    // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360  
                    .direction(100).latitude(location.getLatitude())  
                    .longitude(location.getLongitude()).build();  
            baiduMap.setMyLocationData(locData);    //���ö�λ����  
              
              
            if (isFirstLoc) {  
                isFirstLoc = false;  
                  
                  
                LatLng ll = new LatLng(location.getLatitude(),  
                        location.getLongitude());  
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);   //���õ�ͼ���ĵ��Լ����ż���  
//              MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);  
                baiduMap.animateMapStatus(u);  
            }  
        }  
    };  
  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS); 
		SDKInitializer.initialize(getApplicationContext()); 
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_main);
		splash = (LinearLayout) findViewById(R.id.splashscreen);  
        tv = (TextView) findViewById(R.id.info);  
        tv.setText("���ڽ�����������");  
      
        Message msg = new Message();  
        msg.what = STOPSPLASH;  
        splashHandler.sendMessageDelayed(msg, SPLASHTIME); 
		mContext=this;
		 mMapView = (MapView) findViewById(R.id.bmapView); 
		 baiduMap = mMapView.getMap(); 
		 bt1=(RadioButton) findViewById(R.id.radio_button1);
		 bt2=(RadioButton)findViewById(R.id.radio_button2);
		 bt3=(RadioButton)findViewById(R.id.radio_button3);
		 baiduMap.setMyLocationEnabled(true);     
	        locationClient = new LocationClient(getApplicationContext()); // ʵ����LocationClient��  
	        locationClient.registerLocationListener(myListener); // ע���������  
	        this.setLocationOption();   //���ö�λ����  
	        locationClient.start(); // ��ʼ��λ  

		 bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
               Intent intent=new Intent(mContext,FirstA.class);
               startActivity(intent);
			}
		});
		 bt2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(); //���������    
		        intent2.setAction("android.media.action.STILL_IMAGE_CAMERA");     
		        startActivity(intent2); 
				
			}
		});
		 bt3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent3=new Intent(mContext,SecondB.class);
				startActivity(intent3);
			}
		});
	}
	 @Override  
	    protected void onDestroy() {  
		   locationClient.stop();
		   baiduMap.setMyLocationEnabled(false);
	        super.onDestroy();  
	        //��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���  
	        mMapView.onDestroy();  
	        mMapView=null;
	    }  
	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        //��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���  
	        mMapView.onResume();  
	        }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        //��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���  
	        mMapView.onPause();  
	        }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
  
        if (resultCode == Activity.RESULT_OK) {  
  
            String sdStatus = Environment.getExternalStorageState();  
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ���sd�Ƿ����  
                Log.v("TestFile",  
                        "SD card is not avaiable/writeable right now.");  
                return;  
            }  
  
            Bundle bundle = data.getExtras();  
            Bitmap bitmap = (Bitmap) bundle.get("data");// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ  
            FileOutputStream b = null;  
            File file = new File("/sdcard/myImage/");  
            file.mkdirs();// �����ļ���  
            String fileName = "/sdcard/myImage/111.jpg";  
  
            try {  
                b = new FileOutputStream(fileName);  
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    b.flush();  
                    b.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }   
        }  
    }
	 private void setLocationOption() {  
	        LocationClientOption option = new LocationClientOption();  
	        option.setOpenGps(true); // ��GPS  
	        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// ���ö�λģʽ  
	        option.setCoorType("bd09ll"); // ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02  
	        option.setScanSpan(5000); // ���÷���λ����ļ��ʱ��Ϊ5000ms  
	        option.setIsNeedAddress(true); // ���صĶ�λ���������ַ��Ϣ  
	        option.setNeedDeviceDirect(true); // ���صĶ�λ��������ֻ���ͷ�ķ���  
	          
	        locationClient.setLocOption(option);  
	    }  
}
