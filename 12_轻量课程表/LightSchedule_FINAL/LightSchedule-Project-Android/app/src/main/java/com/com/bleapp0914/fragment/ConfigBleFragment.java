package com.com.bleapp0914.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

import com.com.bleapp0914.MainActivity;
import com.com.bleapp0914.R;

public class ConfigBleFragment extends Fragment {
    private MainActivity mainActivity;
    private String TAG = ConfigBleFragment.class.getSimpleName();
    private View v;
    private Button btnConnectBle;
    private Button btnGetGatt;
    private Button btnTestSend;
    private Button btnSentOrder;
    private Button btnReceive;
    private TextView tvRes;
    private EditText etBeSentOrder;

    private UUID serviceUUID;
    private UUID characteristicUUID;

    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        if (characteristicUUID.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for (byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" +
                        stringBuilder.toString());
            }
        }
    }

    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {

                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicWrite(gatt, characteristic, status);
                    Log.e(TAG, "onCharWrite " + gatt.getDevice().getName()
                            + " write "
                            + characteristic.getUuid().toString()
                            + " -> "
                            + new String(characteristic.getValue()));
                }

                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    String intentAction;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        intentAction = ACTION_GATT_CONNECTED;
                        mConnectionState = STATE_CONNECTED;
                        broadcastUpdate(intentAction);
                        Log.i(TAG, "Connected to GATT server.");
                        Log.i(TAG, "Attempting to start service discovery:" +
                                mainActivity.mBluetoothGatt.discoverServices());

                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        intentAction = ACTION_GATT_DISCONNECTED;
                        mConnectionState = STATE_DISCONNECTED;
                        Log.i(TAG, "Disconnected from GATT server.");
                        broadcastUpdate(intentAction);
                    }
                }

                @Override
                // New services discovered
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                    } else {
                        Log.w(TAG, "onServicesDiscovered received: " + status);
                    }
                }

                @Override
                // Result of a characteristic read operation
                public void onCharacteristicRead(BluetoothGatt gatt,
                                                 BluetoothGattCharacteristic characteristic,
                                                 int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                    }
                }

                @Override
                public void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                    Log.e(TAG, "Characteristic changed! - 1st");

                    /* ---- test ----*/
                    // ���Զ�ȡ��ǰCharacteristic���ݣ��ᴥ��mOnDataAvailable.onCharacteristicRead()

                    Log.e(TAG, String.valueOf((gatt.readCharacteristic(characteristic))));
                    Log.e(TAG, new String(characteristic.getValue()));
                    Log.e(TAG, "Characteristic changed! - 2nd");

                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvRes.setText(tvRes.getText().toString() + new String(characteristic.getValue()));

                            //2015.09.16
                            Toast.makeText(mainActivity, new String(characteristic.getValue()), Toast.LENGTH_SHORT).show();
                        }
                    });

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mainActivity.mBluetoothGatt.readCharacteristic(mainActivity.mBluetoothGattCharacteristic);
//                        }
//                    }, 500);
//                    tvRes.setText(new String(mainActivity.mBluetoothGattCharacteristic.getValue()));
//                    Log.e(TAG, new String(mainActivity.mBluetoothGattCharacteristic.getValue()));


                }

            };


    private View.OnClickListener connectBleListener = new View.OnClickListener() {

        private void scanLeDevice(final boolean enable) {
            if (enable) {
                mainActivity.mScanning = true;
                mainActivity.mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mainActivity.mScanning = false;
                mainActivity.mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }

        // Device scan callback.
        private BluetoothAdapter.LeScanCallback mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi,
                                         byte[] scanRecord) {

                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Log.e(TAG, "Call Back Invoked!");

                                if (device.getName().equals(getString(R.string.bleDeviceName))) {
                                    mainActivity.bleDevice = device;
                                    Toast.makeText(getActivity(), "Get OBDBLE!", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Get OBDBLE");
                                    mainActivity.mBluetoothAdapter.stopLeScan(mLeScanCallback);
                                }
                            }

                        });

                    }
                };

        @Override
        public void onClick(View v) {
            scanLeDevice(true);

        }
    };

    private View.OnClickListener getGattListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainActivity.mBluetoothGatt = mainActivity.bleDevice.connectGatt(mainActivity, false, mGattCallback);
//            mainActivity.mBluetoothGatt.connect();
//            mainActivity.mBluetoothGatt.discoverServices();
            Log.e(TAG, "mBluetoothGatt : " + String.valueOf(mainActivity.mBluetoothGatt.getDevice().getName() + " --- MAC : " + mainActivity.mBluetoothGatt.getDevice().getAddress()));
            Toast.makeText(mainActivity, "mBluetoothGatt : " + String.valueOf(mainActivity.mBluetoothGatt.getDevice().getName() + " --- MAC : " + mainActivity.mBluetoothGatt.getDevice().getAddress()), Toast.LENGTH_SHORT).show();
        }
    };

    private View.OnClickListener testSendListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /* ------ 2015.09.15 - class ------*/

            for (BluetoothGattService gattService : mainActivity.mBluetoothGatt.getServices()) {
                //-----Service���ֶ���Ϣ-----//
                Log.w(TAG, "-->service type:" + gattService.getType());
                Log.w(TAG, "-->includedServices size:" + gattService.getIncludedServices().size());
                Log.w(TAG, "-->service uuid:" + gattService.getUuid());

                //-----Characteristics���ֶ���Ϣ-----//
                List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
                for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    Log.w(TAG, "---->char uuid:" + gattCharacteristic.getUuid());

                    int permission = gattCharacteristic.getPermissions();
                    Log.w(TAG, "---->char permission:" + permission);

                    int property = gattCharacteristic.getProperties();
                    Log.w(TAG, "---->char property:" + property);

                    byte[] data = gattCharacteristic.getValue();
                    if (data != null && data.length > 0) {
                        Log.e(TAG, "---->char value:" + new String(data));
                    }

                    //UUID_KEY_DATA�ǿ��Ը�����ģ�鴮��ͨ�ŵ�Characteristic
                    if (gattCharacteristic.getUuid().toString().equals(getString(R.string.characteristic_uuid))) {
                        mainActivity.mBluetoothGattCharacteristic = gattCharacteristic;
                        Log.e(TAG, "Get Characteristic! uuid : " + mainActivity.mBluetoothGattCharacteristic.getUuid().toString());
                        Toast.makeText(mainActivity, "Get Characteristic! uuid : " + mainActivity.mBluetoothGattCharacteristic.getUuid().toString(), Toast.LENGTH_SHORT).show();

                        //����Characteristic��д��֪ͨ,�յ�����ģ������ݺ�ᴥ��mOnDataAvailable.onCharacteristicWrite()
                        mainActivity.mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);


                    }

                }
            }

        }
    };

    private View.OnClickListener sendOrderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            tvRes.setText("Initial State.");

            //������������
            mainActivity.mBluetoothGattCharacteristic.setValue(etBeSentOrder.getText().toString() + '\r');
            //������ģ��д������
            mainActivity.mBluetoothGatt.writeCharacteristic(mainActivity.mBluetoothGattCharacteristic);

        }
    };

    private View.OnClickListener receiveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //���Զ�ȡ��ǰCharacteristic���ݣ��ᴥ��mOnDataAvailable.onCharacteristicRead()
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainActivity.mBluetoothGatt.readCharacteristic(mainActivity.mBluetoothGattCharacteristic);
                }
            }, 500);
            tvRes.setText(new String(mainActivity.mBluetoothGattCharacteristic.getValue()));
            Log.e(TAG, new String(mainActivity.mBluetoothGattCharacteristic.getValue()));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_config_ble, container, false);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainActivity = (MainActivity) getActivity();

        serviceUUID = UUID.fromString(getString(R.string.service_uuid));
        characteristicUUID = UUID.fromString(getString(R.string.characteristic_uuid));

        btnConnectBle = (Button) v.findViewById(R.id.btnConnectBle);
        btnGetGatt = (Button) v.findViewById(R.id.btnGetGatt);
        btnTestSend = (Button) v.findViewById(R.id.btnTestSend);
        btnSentOrder = (Button) v.findViewById(R.id.btnSendOrder);
        btnReceive = (Button) v.findViewById(R.id.btnReceive);
        tvRes = (TextView) v.findViewById(R.id.tvRes);
        etBeSentOrder = (EditText) v.findViewById(R.id.etBeSentOrder);

        btnConnectBle.setOnClickListener(connectBleListener);
        btnGetGatt.setOnClickListener(getGattListener);
        btnTestSend.setOnClickListener(testSendListener);
        btnSentOrder.setOnClickListener(sendOrderListener);
        btnReceive.setOnClickListener(receiveListener);
    }
}
