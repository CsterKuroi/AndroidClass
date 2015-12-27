package com.yujiannan.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SendSmsActivity extends ActionBarActivity {

    private DBOpenHandler dbOpenHandler = new DBOpenHandler(this,"db.db",null,1);
    private SQLiteDatabase db;
    private TextView tel;
    private EditText content;
    private Button send_Sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        tel = (TextView)findViewById(R.id.sms_tel);
        content = (EditText)findViewById(R.id.sms_content);

        send_Sms = (Button)findViewById(R.id.btn_send);
        db = dbOpenHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM message WHERE no=?",new String[]{String.valueOf(MainActivity.no)});
        if (cursor.moveToFirst()){
            tel.setText(cursor.getString(cursor.getColumnIndex("tel")));
        }
        db.close();

        send_Sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                String temp = content.getText().toString();
                ArrayList<String> texts = smsManager.divideMessage(temp);
                for (String text:texts){
                    smsManager.sendTextMessage(
                            tel.getText().toString(),
                            null,
                            text,
                            null,
                            null
                    );
                    Toast.makeText(SendSmsActivity.this,"啦啦啦 成功了！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_sms, menu);
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
}
