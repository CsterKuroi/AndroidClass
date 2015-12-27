package com.example.wei.spark;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ContentActivity extends AppCompatActivity {

    private String user_name = null;

    private TextView tvContent = null;
    private TextView tvUser = null;
    private ImageView ivPicture = null;
    private TextView tvPicUser = null;
    private Button btnPre = null;
    private Button btnNext = null;
    private Button btnBack = null;
    private Button btnPicture = null;
    private Button btnConfirm = null;

    PostDBHelper db = null;
    String sql = null;
    Cursor cursor;

    private Bitmap bitmap = null;
    private byte[] mContent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_activity);

        tvContent = (TextView)super.findViewById(R.id.tvContent);
        tvUser = (TextView)super.findViewById(R.id.tvUser);
        ivPicture = (ImageView)super.findViewById(R.id.ivPicture);
        tvPicUser = (TextView)super.findViewById(R.id.tvPicUsr);
        btnPre = (Button)super.findViewById(R.id.btnPre);
        btnNext = (Button)super.findViewById(R.id.btnNext);
        btnBack = (Button)super.findViewById(R.id.btnBack);
        btnPicture = (Button)super.findViewById(R.id.btnPicture);
        btnConfirm = (Button)super.findViewById(R.id.btnConfirm);
        btnConfirm.setVisibility(View.INVISIBLE);

        Intent intent = super.getIntent();
        user_name = intent.getStringExtra("user_name");

        db = new PostDBHelper(ContentActivity.this, "PostTable", null, 1);
        sql = "select * from post_table";
        cursor = db.getWritableDatabase().rawQuery(sql, null);

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            showContent();
        }
        else
            tvContent.setText("没有任何记录");
        db.close();


        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cursor.isFirst()) {
                    cursor.moveToPrevious();
                    showContent();
                } else
                    Toast.makeText(getApplicationContext(), "已经到第一条了", Toast.LENGTH_SHORT).show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cursor.isLast()) {
                    cursor.moveToNext();
                    showContent();
                } else
                    Toast.makeText(getApplicationContext(), "已经到最后一条了", Toast.LENGTH_SHORT).show();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentActivity.this.finish();
            }
        });
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册中选择图片
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/jpeg");
                startActivityForResult(getImage, 0);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将图片数据添加到数据库中
                if (bitmap == null)
                    Toast.makeText(getApplicationContext(), "没有选择任何照片", Toast.LENGTH_SHORT).show();
                else{
                    db.setImg(cursor.getInt(cursor.getColumnIndex("_id")), bitmap, user_name);
                    int position = cursor.getPosition();
                    cursor = db.getWritableDatabase().rawQuery(sql, null);
                    cursor.moveToPosition(position);
                    Toast.makeText(getApplicationContext(), "设置照片成功", Toast.LENGTH_SHORT).show();
                    btnConfirm.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 0){
            //从相册中选择了一张图片
            Uri uri = data.getData();
            try {
                mContent = readStream(getContentResolver().openInputStream(Uri.parse(uri.toString())));
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            //将其显示出来
            bitmap = BitmapFactory.decodeByteArray(mContent, 0, mContent.length);
            ivPicture.setImageBitmap(bitmap);
            ivPicture.setVisibility(View.VISIBLE);
            tvPicUser.setText("                                --- Posted by " + user_name);
            btnConfirm.setVisibility(View.VISIBLE);
        }
        else {
            Toast.makeText(getApplicationContext(), "发生了奇怪的错误", Toast.LENGTH_SHORT).show();
        }
    }

    private void showContent(){
        btnConfirm.setVisibility(View.INVISIBLE);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String user_name = cursor.getString(cursor.getColumnIndex("content_name"));
        tvContent.setText(content);
        tvUser.setText("                                --- Posted by " + user_name);

        String pic_user_name = cursor.getString(cursor.getColumnIndex("picture_name"));
        if (pic_user_name.equals("")){
            ivPicture.setVisibility(View.INVISIBLE);
            tvPicUser.setText("");
            //Toast.makeText(getApplicationContext(), "没有照片", Toast.LENGTH_SHORT).show();
        }
        else {
            byte[] in = cursor.getBlob(cursor.getColumnIndex("picture"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
            ivPicture.setImageBitmap(bitmap);
            ivPicture.setVisibility(View.VISIBLE);
            tvPicUser.setText("                                --- Posted by " + pic_user_name);
        }
    }

    private byte[] readStream(InputStream in){ //辅助函数，用于处理从相册中选择的图片
        byte[] buffer  = new byte[1024];
        byte[] data = null;
        int len  =-1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            while ((len = in.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            data = outStream.toByteArray();
            outStream.close();
            in.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return data;
    }
}
