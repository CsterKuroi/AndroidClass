package com.example.joezhou.appname;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */

   // TextView myTextView;
    Button onClickBtn;
   // Button startButton;
    MyReceiver receiver;
    boolean lockState = true;
    IBinder serviceBinder;
    MyService mService;
    Intent intent;
    File file;
    //String s;
    //int value = 0;

    /**************service 命令*********/
    static final int CMD_STOP_SERVICE = 0x01;//停止服务
    static final int CMD_SEND_DATA = 0x02;//发送数据
    static final int CMD_SYSTEM_EXIT =0x03;//退出程序
    static final int CMD_SHOW_TOAST =0x04;//界面上显示toast
    //static final int CMD_START_BTL = 0x5;  //蓝牙设备已开起

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //myTextView = (TextView)findViewById(R.id.myTextView);
        //myTextView.setText("Season");
        onClickBtn = (Button)findViewById(R.id.lock_button);
        onClickBtn.setOnClickListener(new SendButtonClickListener());
       // startButton = (Button)findViewById(R.id.startButton);
       // startButton.setOnClickListener(new StartButtonClickListener());
        createLogFile();
        intent = new Intent(MainActivity.this,MyService.class);
        startService(intent);
        initBtnBackgroud();


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //onClickBtn.setBackgroundResource(R.drawable.lock);
    public class SendButtonClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

           // if(s=="Too far！")
               // return;

            if(lockState==false) {
                // 通过蓝牙发送上锁信号，若成功，则 set lockState .... assume successful set

                lockState = true;
                byte command = 45;
                int value = 76;
                sendCmd(command,value);
                Toast toast = Toast.makeText(MainActivity.this,R.string.locked_toast, Toast.LENGTH_SHORT);
                toast.show();
            }
            else {
                // 通过蓝牙发送解锁信号，若成功，则 unset lockState .... assume successful unset
                lockState = false;
                byte command = 45;
                int value = 79;
                sendCmd(command,value);
                Toast toast = Toast.makeText(MainActivity.this,R.string.welcome_toast, Toast.LENGTH_SHORT);
                toast.show();

            }

            if(lockState==true ){
                onClickBtn.setBackgroundResource(R.drawable.lock);
                addLog("lock");

            }

            else{
                onClickBtn.setBackgroundResource(R.drawable.open);
                addLog("open");

            }
        }
    }
    private void initBtnBackgroud() {

       if(lockState==true){
            onClickBtn.setBackgroundResource(R.drawable.lock);
        }

        else{
            onClickBtn.setBackgroundResource(R.drawable.open);
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if(receiver!=null){
            MainActivity.this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        receiver = new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.lxx");
        MainActivity.this.registerReceiver(receiver,filter);
    }

    public void showToast(String str){//显示提示信息
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }


    public class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equals("android.intent.action.lxx")){
                Bundle bundle = intent.getExtras();
                int cmd = bundle.getInt("cmd");

                if(cmd == CMD_SHOW_TOAST){

                    String str = bundle.getString("str");
                    ///s = str;
                    showToast(str);
                }
                else if(cmd == CMD_SYSTEM_EXIT){
                    System.exit(0);
                }

            }
        }
    }
    //检查并创建系统日志文件
    private void createLogFile() {

        String pathString = Environment.getExternalStorageDirectory().getPath();
        //String pathString = Environment.getDataDirectory().getPath();
        //存放位置
        file = new File(pathString + "/" + "LockLog.txt");
        if (file.exists()== false){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     //添加日志条目
    private void addLog(String content) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        String datetime = "";
        try {
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "
                    + "hh:mm:ss");
            datetime = tempDate.format(new java.util.Date()).toString();
            fw = new FileWriter(file, true);//
            // 创建FileWriter对象，用来写入字符流
            bw = new BufferedWriter(fw); // 将缓冲对文件的输出
            String myreadline = datetime + "[]" + content;

            bw.write(myreadline + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            }
        }
    }

    public void sendCmd(byte command, int value){
        Intent intent = new Intent();//创建Intent对象
        intent.setAction("android.intent.action.cmd");
        intent.putExtra("cmd", CMD_SEND_DATA);
        intent.putExtra("command", command);
        intent.putExtra("value", value);
        sendBroadcast(intent);//发送广播
    }
}
