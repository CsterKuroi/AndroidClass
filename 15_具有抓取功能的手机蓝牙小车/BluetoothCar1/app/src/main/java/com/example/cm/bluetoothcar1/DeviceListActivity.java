package com.example.cm.bluetoothcar1;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by CM on 2015/8/29.
 */
public class DeviceListActivity extends Activity {
    //调试用
    private static final String TAG = "DeviceListActivity";
    private static final Boolean D = true;

    //返回数据标签
    public static String EXTRA_DEVICE_ADDRESS = "设备地址";

    //成员域
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDeviceArrayAdapter;
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {  //设备响应函数
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mBtAdapter.cancelDiscovery();   //准备连接设备，关闭服务查找
            //得到mac地址
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);            //获得最后的17个字符，形式xx:xx:xx:xx:xx:xx，其中x为16进制大写的数
            //设置返回数据
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS,address);
            // 设置返回值并结束程序
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    };
    private BroadcastReceiver mReceiver = new BroadcastReceiver() { // 查找到设备和搜索完成action监听器
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //查找到设备action
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //得到蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //如果是配对的则略过，已得到显示，其余的再添加到列表中进行显示
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    mNewDeviceArrayAdapter.add(device.getName()+"\n"+device.getAddress());
                }else{
                    mPairedDevicesArrayAdapter.add(device.getName()+"\n"+device.getAddress());
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(false);
                setTitle("选择要连接的设备");
                if(mNewDeviceArrayAdapter.getCount() == 0){
                    String noDevice = "没有找到设备";
                    mNewDeviceArrayAdapter.add(noDevice);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //创建并显示窗口
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);    //设置窗口显示模式为窗口方式
        setContentView(R.layout.device_list);

        //设置默认返回值为取消
        setResult(Activity.RESULT_CANCELED);

        //设定扫描按键响应
        Button scanButton = (Button) findViewById(R.id.btnScan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        //初使化设备存储数组
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,R.layout.device_name);
        mNewDeviceArrayAdapter = new ArrayAdapter<String>(this,R.layout.device_name);

        // 设置已配队设备列表
        ListView pairedListView = (ListView)findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        //设置新查找设备列表
        ListView newDeviceListView = (ListView)findViewById(R.id.new_devices);
        newDeviceListView.setAdapter(mNewDeviceArrayAdapter);
        newDeviceListView.setOnItemClickListener(mDeviceClickListener);

        // 注册接收查找到设备action接收器
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver,filter);

        //得到本地蓝牙句柄
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭服务查找
        if(mBtAdapter != null)
            mBtAdapter.cancelDiscovery();
        //注销action接收器
        this.unregisterReceiver(mReceiver);
    }

    public void onCancel(View v){
        finish();
    }

    /**
     * 开始服务和查找设备
     */
    private void doDiscovery(){
        if(D) Log.d(TAG,"doDiscovery()");
        //在窗口显示查找中信息
        setProgressBarIndeterminateVisibility(true);
        setTitle("查找设备中……");

        // 显示其它设备（未配对设备）列表
        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        //关闭再进行的服务查找
        if(mBtAdapter.isDiscovering()){
            mBtAdapter.cancelDiscovery();
        }
        //重新开始
        mBtAdapter.startDiscovery();
    }
}
