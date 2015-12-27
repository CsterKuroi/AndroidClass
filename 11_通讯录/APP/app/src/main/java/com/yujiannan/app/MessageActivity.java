package com.yujiannan.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.meg7.widget.CircleImageView;


public class MessageActivity extends Activity implements TextWatcher {

    private Button button,btn_return;
    private ImageView picture,call,send_Sms;
    private TextView textView;
    private EditText tv_name,tv_real_name,tv_tel;
    private DBOpenHandler dbOpenHandler = new DBOpenHandler(this,"db.db",null,1);
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        button = (Button)findViewById(R.id.button);
        btn_return = (Button)findViewById(R.id.btn_return);
        picture = (ImageView)findViewById(R.id.picture);
        call = (ImageView)findViewById(R.id.btn_call);
        send_Sms = (ImageView)findViewById(R.id.btn_sentMessage);
        tv_real_name = (EditText)findViewById(R.id.tv_real_name);
        tv_tel = (EditText)findViewById(R.id.tv_tel);

        tv_name = (EditText)findViewById(R.id.tv_name);

        init();

        tv_name.addTextChangedListener(this);
        tv_real_name.addTextChangedListener(this);
        tv_tel.addTextChangedListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                MessageActivity.this.startActivityForResult(intent, 1);
            }
        });

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageActivity.this.finish();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbOpenHandler.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM message WHERE no=?", new String[]{String.valueOf(MainActivity.no)});
                String phoneNumber = "";
                if (cursor.moveToFirst()) {
                    phoneNumber = cursor.getString(cursor.getColumnIndex("tel"));
                }
                db.close();
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNumber));
                startActivity(intent);
            }
        });

        send_Sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this,SendSmsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {;
        db = dbOpenHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM message WHERE no=?",new String[]{String.valueOf(MainActivity.no)});
        String pictureString = "";
        if (cursor.moveToFirst()) {
            pictureString = cursor.getString(cursor.getColumnIndex("picture"));
        }
        if (pictureString != ""){
            picture.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex("picture"))));
        }
        tv_name.setText(cursor.getString(cursor.getColumnIndex("name")));
        tv_real_name.setText(cursor.getString(cursor.getColumnIndex("real_name")));
        tv_tel.setText(cursor.getString(cursor.getColumnIndex("tel")));
        db.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED){
            return;
        }
        Uri imageuri = data.getData();
        String temp = imageuri.toString();
        db = dbOpenHandler.getWritableDatabase();
        db.execSQL("UPDATE message SET picture=? WHERE no=?",new String[]{temp,String.valueOf(MainActivity.no)});
        picture.setImageURI(imageuri);
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        db = dbOpenHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM message WHERE no=?",new String[]{String.valueOf(MainActivity.no)});
        db.execSQL("UPDATE message SET name=?,real_name=?,tel=? WHERE no=?",new String[]{tv_name.getText().toString(),tv_real_name.getText().toString(),tv_tel.getText().toString(),String.valueOf(MainActivity.no)});
        db.close();
    }
}
