package com.example.cm.bluetoothcar1;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends Activity {
    private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询句柄
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB"; //SPP服务UUID号

    private InputStream is;         //输入流，用来接收蓝牙数据
    private TextView text0;         //提示栏句柄
    private EditText edit0;         //发送数据输入句柄
    private ScrollView sv;          //翻页句柄
    private TextView dis;           //接受数据柄
    private TextView directionState;    //方向
    private TextView speedState;    //速度
    private TextView handState;     //机械手状态

    private String smsg = "";       //显示用数据缓存
    private String fmsg = "";       //保存用数据缓存
    private String filename = "";       //用来保存存储的文件名

    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;     //蓝牙通信socket
    boolean bRun = false, bThread = false;
    private int pulse = 8;

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();     //获取本地蓝牙适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);     //设置主界面

        text0 = (TextView) findViewById(R.id.Text0);       //得到提示栏句柄
        edit0 = (EditText) findViewById(R.id.Edit0);       //得到发送栏句柄
        sv = (ScrollView) findViewById(R.id.ScrollView01);              //得到翻页句柄
        dis = (TextView) findViewById(R.id.in);                       //得到数据显示句柄
        directionState = (TextView) findViewById(R.id.DirectionState);
        handState = (TextView) findViewById(R.id.HandState);
        speedState = (TextView) findViewById(R.id.SpeedState);

        ImageButton btnStop = (ImageButton)findViewById(R.id.Stop);
        btnStop.getBackground().setAlpha(50);

        ImageButton btnForward = (ImageButton) findViewById(R.id.Forward);
        btnForward.getBackground().setAlpha(150);
        btnForward.setOnTouchListener(new ImageButton.OnTouchListener() {
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    directionState.setText("前进");
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                send("F");
                                try{
                                    Thread.sleep(20);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnBackward = (ImageButton) findViewById(R.id.Backward);
        btnBackward.getBackground().setAlpha(150);
        btnBackward.setOnTouchListener(new ImageButton.OnTouchListener() {
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    directionState.setText("后退");
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                send("B");
                                try{
                                    Thread.sleep(20);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnLeft = (ImageButton) findViewById(R.id.Left);
        btnLeft.getBackground().setAlpha(150);
        btnLeft.setOnTouchListener(new ImageButton.OnTouchListener() {
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    directionState.setText("左转");
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                send("L");
                                try{
                                    Thread.sleep(150);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnRight = (ImageButton) findViewById(R.id.Right);
        btnRight.getBackground().setAlpha(150);
        btnRight.setOnTouchListener(new ImageButton.OnTouchListener() {
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    directionState.setText("右转");
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                send("R");
                                try{
                                    Thread.sleep(150);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnLiftUp = (ImageButton) findViewById(R.id.LiftUp);
        btnLiftUp.getBackground().setAlpha(150);
        btnLiftUp.setOnTouchListener(new ImageButton.OnTouchListener() {
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    handState.setText("上升");
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                send("U");
                                try{
                                    Thread.sleep(20);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnLiftDown = (ImageButton) findViewById(R.id.LiftDown);
        btnLiftDown.getBackground().setAlpha(150);
        btnLiftDown.setOnTouchListener(new ImageButton.OnTouchListener() {
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    handState.setText("下降");
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                send("D");
                                try{
                                    Thread.sleep(20);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnOpen = (ImageButton) findViewById(R.id.Open);
        btnOpen.getBackground().setAlpha(150);
        btnOpen.setOnTouchListener(new ImageButton.OnTouchListener() {
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    handState.setText("张开");
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                send("O");
                                try{
                                    Thread.sleep(20);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnClose = (ImageButton) findViewById(R.id.Close);
        btnClose.getBackground().setAlpha(150);
        btnClose.setOnTouchListener(new ImageButton.OnTouchListener() {
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    handState.setText("合拢");
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                send("C");
                                try{
                                    Thread.sleep(20);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnSlowDown = (ImageButton) findViewById(R.id.SlowDown);
        btnSlowDown.getBackground().setAlpha(150);
        btnSlowDown.setOnTouchListener(new ImageButton.OnTouchListener(){
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                /*synchronized(this){
                                    Message m = new Message();
                                    if(pulse==1 || !fmsg.equals("S")){
                                        m.obj = pulse + "/16";
                                    }else{
                                        pulse--;
                                        send("S");
                                        m.obj = pulse + "/16";
                                        handler.sendMessage(m);
                                    }
                                }*/
                                Message m = new Message();
                                send("S");
                                m.obj = "减速";
                                handler.sendMessage(m);
                                try{
                                    Thread.sleep(200);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        ImageButton btnSpeedUp = (ImageButton) findViewById(R.id.SpeedUp);
        btnSpeedUp.getBackground().setAlpha(150);
        btnSpeedUp.setOnTouchListener(new ImageButton.OnTouchListener(){
            boolean longClicked;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_DOWN == event.getAction()){
                    v.getBackground().setAlpha(50);
                    longClicked = true;
                    Thread t = new Thread(){
                        public void run(){
                            super.run();
                            while(longClicked){
                                /*synchronized (this){
                                    Message m = new Message();
                                    if(pulse==14 || !fmsg.equals("A")){
                                        m.obj = pulse + "/16";
                                    }else{
                                        pulse++;
                                        send("A");
                                        m.obj = pulse + "/16";
                                        handler.sendMessage(m);
                                    }
                                }*/
                                Message m = new Message();
                                send("A");
                                m.obj = "加速";
                                handler.sendMessage(m);
                                try{
                                    Thread.sleep(200);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    t.start();
                }else if(MotionEvent.ACTION_UP==event.getAction()){
                    longClicked = false;
                    v.getBackground().setAlpha(150);
                    send("Q");
                }
                return false;
            }
        });

        //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if(_bluetooth == null){
            showToast(this, "打开本地蓝牙失败，确定手机是否有蓝牙功能", Toast.LENGTH_LONG);
            finish();
            return;
        }

        //设置设备可以被搜索
        new Thread(){
           public void run(){
               if(_bluetooth.isEnabled() == false){
                   _bluetooth.enable();
               }
           }
        }.start();
    }

    public void send(String com){
        int i=0,n=0;
        try {
            OutputStream os = _socket.getOutputStream();    //蓝牙连接输出流
            byte[] bos = com.getBytes();
            for (i=0;i<bos.length;i++){
                if(bos[i]==0x0a)
                    n++;
            }
            byte[] bos_new = new byte[bos.length + n];
            n = 0;
            for(i = 0;i<bos.length;i++){ //手机中换行为0a,将其改为0d 0a(回车 换行)后再发送
                if(bos[i]==0x0a){
                    bos_new[n++] = 0x0d;
                    bos_new[n++] = 0x0a;
                }else{
                    bos_new[n++] = bos[i];
                }
            }
            os.write(bos_new);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            //showToast(this, "请连接好蓝牙设备", Toast.LENGTH_SHORT);
        }
    }

    //接收数据线程
    Thread readThread = new Thread(){
        public void run(){
            int num=0, i=0, n=0;
            byte[] buffer = new byte[1024], buffer_new = new byte[1024];
            bRun = true;
            //接收线程
            while(true){
                try {
                    while(is.available() == 0){
                        while(bRun == false){}
                    }
                    while(true){
                        System.out.println("你好！");
                        num = is.read(buffer);      //读入数据
                        n = 0;
                        String s0 = new String(buffer,0,num);
                        fmsg += s0;     //保存接收的数据

                        for(i = 0;i<num;i++){
                            if(buffer[i]==0x0d && buffer[i+1]==0x0a){
                                buffer_new[n++] = 0x0a;
                                i++;
                            }else{
                                buffer_new[n++] = buffer[i];
                            }
                        }
                        String s = new String(buffer_new,0,n);
                        smsg += s;      //写入接收缓存
                        if(is.available() ==0) break;           //短时间没有数据才跳出进行显示
                    }
                    handler.sendMessage(handler.obtainMessage());       //发送显示消息，进行显示刷新
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //消息处理队列
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            dis.setText(smsg);  //显示数据
            sv.scrollTo(0,dis.getMeasuredHeight());     //跳至数据最后一页
            speedState.setText((String) msg.obj);
        }
    };

    //关闭程序调用处理部分
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(_socket!=null){
            try {
                _socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //	_bluetooth.disable();  //关闭蓝牙服务
        }
    }

    //发送按键响应函数
    public void onSendButtonClicked(View v){
        int i=0;
        int n=0;
        try {
            OutputStream os = _socket.getOutputStream();        //蓝牙连接输出流
            byte[] bos = edit0.getText().toString().getBytes();
            for (i=0;i<bos.length;i++){
                if(bos[i]==0x0a)
                    n++;
            }
            byte[] bos_new = new byte[bos.length + n];
            n = 0;
            for(i = 0;i<bos.length;i++){ //手机中换行为0a,将其改为0d 0a(回车 换行)后再发送
                if(bos[i]==0x0a){
                    bos_new[n++] = 0x0d;
                    bos_new[n++] = 0x0a;
                }else{
                    bos_new[n++] = bos[i];
                }
            }
            os.write(bos_new);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            showToast(this, "请连接好蓝牙设备", Toast.LENGTH_SHORT);
        }

    }

    //连接按键响应函数
    public void onConnectButtonClicked(View v){
        if(_bluetooth.isEnabled()==false){  //如果蓝牙服务不可用，提示
            showToast(this, "打开蓝牙中……", Toast.LENGTH_LONG);
            return;
        }

        //如未连接设备则打开DeviceListActivity进行搜索
        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        if(_socket == null){
            Intent serverIntent = new Intent(this,DeviceListActivity.class);    //设置跳转设置
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);     //设置返回宏定义
        }else{//关闭socket
            try {
                is.close();
                _socket.close();
                _socket = null;
                bRun = false;
                btnConnect.setText("连接");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ;
    }

    //保存按键响应函数
    public void onSaveButtonClicked(View v){
        save();
    }

    //保存功能实现
    private void save(){
        //显示对话框输入文件名
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);    //图层模板生成器句柄
        final View dialogView = factory.inflate(R.layout.sname, null);
        new AlertDialog.Builder(MainActivity.this).setTitle("文件名").setView(dialogView).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText text1 = (EditText) dialogView.findViewById(R.id.sname);     //得到文件名输入句柄
                filename = text1.getText().toString();
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    filename += ".txt";
                    File sdcardDir = Environment.getExternalStorageDirectory();
                    File buildDir = new File(sdcardDir, "/data");
                    if (buildDir.exists() == false) buildDir.mkdirs();
                    File saveFile = new File(buildDir, filename);
                    FileOutputStream stream = null;
                    try {
                        stream = new FileOutputStream(saveFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        stream.write(fmsg.getBytes());
                        stream.close();
                        showToast(MainActivity.this, "存储成功！", Toast.LENGTH_SHORT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        showToast(MainActivity.this, "信息为空！", Toast.LENGTH_SHORT);
                    }
                } else {
                    showToast(MainActivity.this, "没有存储卡！", Toast.LENGTH_SHORT);
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    //清除按键响应函数
    public void onClearButtonClicked(View v){
        smsg = "";
        fmsg = "";
        dis.setText(smsg);
    }

    //退出按键响应函数
    public void onQuitButtonClicked(View v){
        finish();
    }

    //接收活动结果，响应startActivityForResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE:
                if(resultCode == Activity.RESULT_OK){   //连接成功
                    // MAC地址，由DeviceListActivity设置返回
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // 得到蓝牙设备句柄
                    _device = _bluetooth.getRemoteDevice(address);
                    //用服务号得到socket
                    try {
                        _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                    } catch (IOException e) {
                        showToast(this, "连接失败！", Toast.LENGTH_SHORT);
                    }
                    //连接socket
                    Button btnConnect = (Button) findViewById(R.id.btnConnect);
                    try {
                        _socket.connect();
                        showToast(this, "连接" + _device.getName() + "成功！", Toast.LENGTH_SHORT);
                        btnConnect.setText("断开");
                    } catch (IOException e) {
                        try {
                            showToast(this, "连接失败！", Toast.LENGTH_SHORT);
                            _socket.close();
                            _socket = null;
                        } catch (IOException e1) {
                            showToast(this, "连接失败！", Toast.LENGTH_SHORT);
                        }
                        return;
                    }

                    //打开接收代理
                    try {
                        is = _socket.getInputStream();      // 得到蓝牙数据输入流
                    } catch (IOException e) {
                        showToast(this, "接收数据！", Toast.LENGTH_SHORT);
                        return;
                    }
                    if(bThread==false){
                        readThread.start();
                        bThread = true;
                    }else{
                        bRun = true;
                    }

                }
                break;
            default:break;
        }
    }

    private static Toast mToast;
    public static void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }










    //菜单处理部分
  /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {//建立菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) { //菜单响应函数
        switch (item.getItemId()) {
        case R.id.scan:
        	if(_bluetooth.isEnabled()==false){
        		Toast.makeText(this, "Open BT......", Toast.LENGTH_LONG).show();
        		return true;
        	}
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.quit:
            finish();
            return true;
        case R.id.clear:
        	smsg="";
        	ls.setText(smsg);
        	return true;
        case R.id.save:
        	Save();
        	return true;
        }
        return false;
    }*/
}
