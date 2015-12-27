package com.food.trace;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import cn.domob.android.ads.DomobAdEventListener;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobInterstitialAd;
import cn.domob.android.ads.DomobInterstitialAdListener;
import cn.domob.android.ads.DomobAdManager.ErrorCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import ad.standopen.contact;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Input extends Activity {
    private ImageView result=null;
    private Button save=null;
    private Button share=null;
    private Button back=null;
    Bitmap bmp=null;
    DomobInterstitialAd mInterstitialAd;
	RelativeLayout mAdContainer;
	DomobAdView mAdview320x50;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.input);
        showbaner();
        showad();
        save=(Button)findViewById(R.id.save);
        share=(Button)findViewById(R.id.share);
        result=(ImageView)findViewById(R.id.img);
        back=(Button)findViewById(R.id.back);
        String content=this.getIntent().getStringExtra("content");
        try {
			bmp=createBitmap(Create2DCode(content));
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        result.setImageBitmap(bmp);
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();

    	bmp.compress(Bitmap.CompressFormat.PNG, 100, baos1);

    	try {
			Writetemp(baos1.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        save.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 ByteArrayOutputStream baos = new ByteArrayOutputStream();

			    	bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);

			    	if (mInterstitialAd.isInterstitialAdReady()){
						mInterstitialAd.showInterstitialAd(Input.this);
					} else {
						Log.i("DomobSDKDemo", "Interstitial Ad is not ready");
						mInterstitialAd.loadInterstitialAd();
					}
			    	
			    	
			    	
			    	try {
						Write(baos.toByteArray());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		});
        share.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				  Intent intent=new Intent(Intent.ACTION_SEND);  
	               intent.setType("image/png");  
	               File f = new File("/mnt/sdcard/zibuyu/temp/temp.jpg");
	                Uri u = Uri.fromFile(f);
	               intent.putExtra(Intent.EXTRA_SUBJECT, "share");  
	               intent.putExtra(Intent.EXTRA_TEXT,"二维码说说:一切尽在图片中！");
	               intent.putExtra(Intent.EXTRA_STREAM, u);
	               intent.putExtra("sms_body","二维码说说:一切尽在图片中！");
	               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	               startActivity(Intent.createChooser(intent, "分享图片到："));
			}
		});
       back.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	});
       
       
    }
    public Bitmap Create2DCode(String str) throws WriterException, UnsupportedEncodingException {
		//生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
    	
		BitMatrix matrix = new MultiFormatWriter().encode(new String(str.getBytes("GBK"),"ISO-8859-1"),BarcodeFormat.QR_CODE, 300, 300);
		
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		//二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if(matrix.get(x, y)){
					pixels[y * width + x] = 0xff000000;
				}
				
			}
		}	
		int[] colors={R.color.white};
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		//通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
    public  void Write(byte []b) throws IOException
	{
		File cacheFile =null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdCardDir = Environment.getExternalStorageDirectory();
			
			long time=Calendar.getInstance().getTimeInMillis();
			String fileName =time+".png";
			File dir = new File(sdCardDir.getCanonicalPath()
					+"/zibuyu/");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			cacheFile = new File(dir, fileName);
		
		}  
		Toast toast = Toast.makeText(getApplicationContext(),
				"图片保存在了内存卡下zibuyu文件夹下，请查看！", Toast.LENGTH_LONG);
	      	   toast.setGravity(Gravity.CENTER, 0, 0);
	      	   LinearLayout toastView = (LinearLayout) toast.getView();
	      	   ImageView imageCodeProject = new ImageView(getApplicationContext());
	      	   imageCodeProject.setImageResource(R.drawable.fun);
	      	   toastView.addView(imageCodeProject, 0);
	      	   toast.show();
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
			
				bos.write(b,0,b.length);
				bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public  void Writetemp(byte []b) throws IOException
  	{
  		File cacheFile =null;
  		if (Environment.getExternalStorageState().equals(
  				Environment.MEDIA_MOUNTED)) {
  			File sdCardDir = Environment.getExternalStorageDirectory();
  			Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
  			String fileName ="temp.jpg";
  			File dir = new File(sdCardDir.getCanonicalPath()
  					+"/zibuyu/temp");
  			if (!dir.exists()) {
  				dir.mkdirs();
  			}
  			cacheFile = new File(dir, fileName);
  		
  		}  
  		BufferedOutputStream bos = null;
  		try {
  			bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
  			
  				bos.write(b,0,b.length);
  				bos.close();
  		} catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	}
    private Bitmap createBitmap( Bitmap src)

    {
    	
    if( src == null )
    {
    return null;
    }
    Paint paint=new Paint();
    paint.setColor(Color.WHITE);
    paint.setAntiAlias(true);
   
    int w = 300;
    int h = 300;
    Bitmap newb = Bitmap.createBitmap( w, h, Config.ARGB_8888 );
    Canvas cv = new Canvas( newb );

    cv.drawColor(Color.WHITE);
 
    cv.drawBitmap(src, 0, 0, null );
    cv.save( Canvas.ALL_SAVE_FLAG );
    cv.restore();//存储
    return newb;

    }
    
    public void showad()
    {
    	mInterstitialAd = new DomobInterstitialAd(this,contact.PUBLISHER_ID,
				contact.InterstitialPPID, DomobInterstitialAd.INTERSITIAL_SIZE_300X250);	
		mInterstitialAd.setInterstitialAdListener(new DomobInterstitialAdListener() {
			public void onInterstitialAdReady() {
				Log.i("DomobSDKDemo", "onAdReady");
			}

			public void onLandingPageOpen() {
				Log.i("DomobSDKDemo", "onLandingPageOpen");
			}

			public void onLandingPageClose() {
				Log.i("DomobSDKDemo", "onLandingPageClose");
			}

			public void onInterstitialAdPresent() {
				Log.i("DomobSDKDemo", "onInterstitialAdPresent");
			}

			public void onInterstitialAdDismiss() {
				// Request new ad when the previous interstitial ad was closed.
				mInterstitialAd.loadInterstitialAd();
				Log.i("DomobSDKDemo", "onInterstitialAdDismiss");
			}

			public void onInterstitialAdFailed(ErrorCode arg0) {
				Log.i("DomobSDKDemo", "onInterstitialAdFailed");				
			}

			public void onInterstitialAdLeaveApplication() {
				Log.i("DomobSDKDemo", "onInterstitialAdLeaveApplication");
				
			}
		});
		
		mInterstitialAd.loadInterstitialAd();
	
    }
    
    public void showbaner()
	 {
		 mAdContainer = (RelativeLayout) findViewById(R.id.adcontainer);
			// Create ad view
			mAdview320x50 = new DomobAdView(this,contact.PUBLISHER_ID,contact.InlinePPID, DomobAdView.INLINE_SIZE_320X50);
			mAdview320x50.setKeyword("game");
			mAdview320x50.setUserGender("male");
			mAdview320x50.setUserBirthdayStr("2000-08-08");
			mAdview320x50.setUserPostcode("123456");

			mAdview320x50.setAdEventListener(new DomobAdEventListener() {
							
				public void onDomobAdReturned(DomobAdView adView) {
					Log.i("DomobSDKDemo", "onDomobAdReturned");				
				}

				public void onDomobAdOverlayPresented(DomobAdView adView) {
					Log.i("DomobSDKDemo", "overlayPresented");
				}

				public void onDomobAdOverlayDismissed(DomobAdView adView) {
					Log.i("DomobSDKDemo", "Overrided be dismissed");				
				}

				public void onDomobAdClicked(DomobAdView arg0) {
					Log.i("DomobSDKDemo", "onDomobAdClicked");				
				}

				public void onDomobAdFailed(DomobAdView arg0, ErrorCode arg1) {
					Log.i("DomobSDKDemo", "onDomobAdFailed");				
				}

				public void onDomobLeaveApplication(DomobAdView arg0) {
					Log.i("DomobSDKDemo", "onDomobLeaveApplication");				
				}

				public Context onDomobAdRequiresCurrentContext() {
					return Input.this;
				}
			});
			
			mAdContainer.addView(mAdview320x50);
	 }
}
