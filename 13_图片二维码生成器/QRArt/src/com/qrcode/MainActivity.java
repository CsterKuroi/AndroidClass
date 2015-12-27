package com.qrcode;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;


import com.google.zxing.WriterException;
import com.zxing.activity.CaptureActivity;
import com.zxing.encoding.EncodingHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private TextView resultTextView;
	private EditText qrStrEditText;
	private ImageView qrImgImageView;
	
	//ʵ�ִ�����е�ȡͼƬ������
	private static Bitmap photo=null ;
	private static final int NONE = 0;
	private static final int PHOTO_GRAPH = 1;// ����
	private static final int PHOTO_ZOOM = 2; // ����
	private static final int PHOTO_RESOULT = 3;// ���
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private ImageView imageView = null;
	private Button btnPhone = null;
	private Button btnTakePicture = null;
	private static int ScreenWidth;
	private static int ScreenHeight;
	private static final String SAVE_PIC_PATH=Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//���浽SD��
	private static final String SAVE_REAL_PATH = SAVE_PIC_PATH+"/good/savePic";//�����ȷ��λ��
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_1);
        
//        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
       // qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
       // qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
        
        
        //��ȡ��Ļ�ĸ߶ȺͿ��
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        ScreenWidth = 300;     // ��Ļ��ȣ����أ�
        ScreenHeight = 400;   // ��Ļ�߶ȣ����أ�
        
//        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
//        scanBarCodeButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//					
//				Intent openCameraIntent = new Intent(MainActivity.this,CaptureActivity.class);
//				startActivityForResult(openCameraIntent, 0);
//			}
//		});
        
        //ʵ�ִ�����е�ȡͼƬ������
        //imageView = (ImageView) findViewById(R.id.imageView);
        btnPhone = (Button) findViewById(R.id.button_picture);//���հ�ť����
		btnPhone.setOnClickListener(onClickListener);
        btnTakePicture = (Button) findViewById(R.id.button_camera);//��ᰴť����
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
				if(photo==null){
					Log.v("Error", "1");
				}else{
					Log.v("Error", "0");
				}
				intent= new Intent(MainActivity.this,Second.class);
	        	intent.putExtra("bitmap",photo);
	        	Log.v("Error", "Hello"+intent.toString());
	        	startActivity(intent);
			}
		});
    	
        //ʵ�ִ�����е�ȡͼƬ������
//        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);//��ά�����ɰ�ť
//        generateQRCodeButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				try {
//					String contentString = qrStrEditText.getText().toString();
//					if (!contentString.equals("")) {
//						//�����ɵĶ�ά����Ƶ���ʾ�ϡ�
//						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 800,photo);
//						SavePicInLocal(qrCodeBitmap);
//						qrImgImageView.setImageBitmap(qrCodeBitmap);
//					}else {
//						Toast.makeText(MainActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
//					}
//					
//				} catch (WriterException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
    }

    private  final View.OnClickListener onClickListener = new View.OnClickListener() {
    	@Override
    	public void onClick(View v) {
    			Intent intent;
	        	if(v==btnPhone){ //������ȡͼƬ
	                System.out.println("ok1");
	                intent = new Intent(Intent.ACTION_PICK, null);
		        	intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
		        	startActivityForResult(intent, PHOTO_ZOOM);
		            btnTakePicture.setOnClickListener(onClickListener);
		           
		        	
	        	}else if(v==btnTakePicture){ //�����ջ�ȡͼƬ
	        		intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		        	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"temp.jpg")));
		        	startActivityForResult(intent, PHOTO_GRAPH);
		            btnTakePicture.setOnClickListener(onClickListener);
	        	}
	        	
	        
        }
    };
//    /**
//    * 
//                ����ͼƬ
//    *
//    */
    public void startPhotoZoom(Uri uri) {

	    Intent intent = new Intent("com.android.camera.action.CROP");//����Androidϵͳ�Դ���һ��ͼƬ����ҳ��,
	
	    intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
	    intent.putExtra("crop", "true");//�����޼�
	     //aspectX aspectY �ǿ�ߵı���
	    intent.putExtra("aspectX", 1);
	    intent.putExtra("aspectY", 1);
	    // outputX outputY �ǲü�ͼƬ���
	    intent.putExtra("outputX", 350);
	    intent.putExtra("outputY", 350);
	    intent.putExtra("return-data", true);
	    startActivityForResult(intent, PHOTO_RESOULT);
    }
    
  
    
    public Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // ����Ϊtureֻ��ȡͼƬ��С
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // ����Ϊ��  �ڴ�ռ�ü���
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // ����
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//����ɨ���ά��ķ��ؽ��	
//		if (resultCode == RESULT_OK) {
//			Bundle bundle = data.getExtras();
//			String scanResult = bundle.getString("result");
//			resultTextView.setText(scanResult);
//		}
		//�����Ƭ�����յĲ���
		if (resultCode == NONE)
			return;
			// ����
		if (requestCode == PHOTO_GRAPH) {
			// �����ļ�����·��
			File picture = new File(Environment.getExternalStorageDirectory()
			+ "/temp.jpg");
			startPhotoZoom(Uri.fromFile(picture));
			}
			// ��ȡ�������ͼƬ
		if (requestCode == PHOTO_ZOOM) {
				System.out.println(data.getData().getPath());
				startPhotoZoom(data.getData());
				//photo=convertToBitmap( data.getData().getPath(),600,800);	
			}
			// ������
		if (requestCode == PHOTO_RESOULT) {
				Bundle extras = data.getExtras();
				if (extras != null) {
			    //��ȡͼƬ
				photo = extras.getParcelable("data");	
				}
			}
	}
	//����ͼƬ�ĵ����㷨
	 private static Bitmap overlay(Bitmap bmp1,Bitmap overlay)  
    {  
        //long start = System.currentTimeMillis();  
        int width = bmp1.getWidth();  
        int height = bmp1.getHeight();  
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
		// �Ա߿�ͼƬ�������� 
        int w = overlay.getWidth();  
        int h = overlay.getHeight();  
        float scaleX = width * 1F / w;  
        float scaleY = height * 1F / h;  
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleX, scaleY);  
        Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);  
        int pixColor = 0;  
        int layColor = 0;  
          
        int pixR = 0;  
        int pixG = 0;  
        int pixB = 0;  
        int pixA = 0;  
          
        int newR = 0;  
        int newG = 0;  
        int newB = 0;  
        int newA = 0;  
          
        int layR = 0;  
        int layG = 0;  
        int layB = 0;  
        int layA = 0;  
          
        final float alpha = 0.8F;  
          
        int[] srcPixels = new int[width * height];  
        int[] layPixels = new int[width * height];  
        bmp1.getPixels(srcPixels, 0, width, 0, 0, width, height);  
        overlayCopy.getPixels(layPixels, 0, width, 0, 0, width, height);  
          
        int pos = 0;  
        for (int i = 0; i < height; i++)  
        {  
            for (int k = 0; k < width; k++)  
            {  
                pos = i * width + k;  
                pixColor = srcPixels[pos];  
                layColor = layPixels[pos];  
                  
                pixR = Color.red(pixColor);  
                pixG = Color.green(pixColor);  
                pixB = Color.blue(pixColor);  
                pixA = Color.alpha(pixColor);  
                  
                layR = Color.red(layColor);  
                layG = Color.green(layColor);  
                layB = Color.blue(layColor);  
                layA = Color.alpha(layColor);  
                  
                newR = (int) (pixR * alpha + layR * (1 - alpha));  
                newG = (int) (pixG * alpha + layG * (1 - alpha));  
                newB = (int) (pixB * alpha + layB * (1 - alpha));  
                layA = (int) (pixA * alpha + layA * (1 - alpha));  
                  
                newR = Math.min(255, Math.max(0, newR));  
                newG = Math.min(255, Math.max(0, newG));  
                newB = Math.min(255, Math.max(0, newB));  
                newA = Math.min(255, Math.max(0, layA));  
                  
                srcPixels[pos] = Color.argb(newA, newR, newG, newB);  
            }  
        }  
          
        bitmap.setPixels(srcPixels, 0, width, 0, 0, width, height);  
       
        return bitmap;  
    } 
	 
	// �������ɵ���Ƭ���ֻ���sd��
		private void SavePicInLocal(Bitmap bitmap) {
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			ByteArrayOutputStream baos = null; // �ֽ����������
			try {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				byte[] byteArray = baos.toByteArray();// �ֽ����������ת�����ֽ�����
				String picName = "1.jpg";
				File file = new File(SAVE_PIC_PATH, picName);
				// ���ֽ�����д�뵽�մ�����ͼƬ�ļ���
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bos.write(byteArray);
				Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				Uri uri = Uri.fromFile(file);
				intent.setData(uri);
				this.getBaseContext().sendBroadcast(intent);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (bos != null) {
					try {
						bos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (fos != null) {
					try {
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		}
}