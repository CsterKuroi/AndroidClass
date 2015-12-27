package com.qrcode;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Qrcreat extends Activity {
	private static Bitmap photo ;
	private static final String SAVE_PIC_PATH=Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.linearlayout);
		
		final ImageView qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
		Intent intent=getIntent();
		System.out.println(intent.toString());
		photo=intent.getParcelableExtra("bitmap");
		Log.v("QR",photo.toString());
		final String contentString=intent.getStringExtra("content");
		if(contentString==null)
			Log.v("QR","You");
		
        Button generateQRCodeButton = (Button) this.findViewById(R.id.button_cr);//二维码生成按钮
        Log.v("QR",photo.toString());
        generateQRCodeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
				//	String contentString = qrStrEditText.getText().toString();
					if (!contentString.equals("")) {
						//将生成的二维码绘制到显示上。
						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 800,photo);
						SavePicInLocal(qrCodeBitmap);
						qrImgImageView.setImageBitmap(qrCodeBitmap);
					}else {
						Toast.makeText(Qrcreat.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
					}
					
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qrcreat, menu);
		return true;
	}
	// 保存生成的照片到手机的sd卡
	private void SavePicInLocal(Bitmap bitmap) {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		ByteArrayOutputStream baos = null; // 字节数组输出流
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
			String picName = "1.jpg";
			File file = new File(SAVE_PIC_PATH, picName);
			// 将字节数组写入到刚创建的图片文件中
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
