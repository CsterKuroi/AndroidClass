package com.com.bleapp0914.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.com.bleapp0914.MainActivity;
import com.com.bleapp0914.R;

public class HackFragment extends Fragment {
    private View v;
    private MainActivity mainActivity;

    private Button btnSendATSP6;
    private Button btnSendATCFC1;
    private Button btnSendCAF0;
    private Button btnSendSH;
    private Button btnTurnOnFarLight;
    private Button btnTurnOnHorn;
    private Button btnLock;
    private Button btnUnlock;
    private Button btnTurnOnDoubleLight;
    private Button btnTurnOffEngine;
    private EditText etSH;

    public HackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hack, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        v = getView();
        mainActivity = (MainActivity) getActivity();

        Button btnSendATSP6 = (Button) v.findViewById(R.id.btnSendATSP6);
        Button btnSendATCFC1 = (Button) v.findViewById(R.id.btnSendATCFC1);
        Button btnSendATCAF0 = (Button) v.findViewById(R.id.btnSendATCAF0);
        Button btnSendATSH = (Button) v.findViewById(R.id.btnSendATSH);
        Button btnTurnOnFarLight = (Button) v.findViewById(R.id.btnTurnOnFarLight);
        Button btnTurnOnHorn = (Button) v.findViewById(R.id.btnTurnOnHorn);
        Button btnLock = (Button) v.findViewById(R.id.btnLock);
        Button btnUnlock = (Button) v.findViewById(R.id.btnUnlock);
        Button btnTurnOnDoubleLight = (Button) v.findViewById(R.id.btnTurnOnDoubleLight);
        Button btnTurnOffEngine = (Button) v.findViewById(R.id.btnTurnOffEngine);
        final EditText etSH = (EditText) v.findViewById(R.id.etSH);


        btnSendATSP6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("ATSP6");
            }
        });

        btnSendATCFC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("ATCFC1");
            }
        });

        btnSendATCAF0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("ATCAF0");
            }
        });

        btnSendATSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("ATSH" + etSH.getText().toString());
            }
        });

        btnTurnOnFarLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("40063015002000");
            }
        });

        btnTurnOnHorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("40043014002000");
            }
        });

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("40053011004000");
            }
        });

        btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("40053011004000");
            }
        });

        btnTurnOnDoubleLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("40043014004000");
            }
        });

        btnTurnOffEngine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderToBle("06301c00ffa50100");
            }
        });



    }


    private void sendOrderToBle(String order){
        //������������
        mainActivity.mBluetoothGattCharacteristic.setValue(order + '\r');
        //������ģ��д������
        mainActivity.mBluetoothGatt.writeCharacteristic(mainActivity.mBluetoothGattCharacteristic);
    }
}
