package com.example.meterial;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import at.abraxas.amarino.Amarino;

public class ColorActivity extends Activity {

    private SharedPreferences sharedpreferences;
    private View revealView;
    BluetoothAdapter btAdapt;
    Button fab;
    Switch themeSwitch ;
    ArrayAdapter<String> adtDevices;
    private Amarino amarino;
    private Intent Mintent = new Intent();
    private String address = "00:BA:55:56:70:DF";
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        // Set the saved theme
        sharedpreferences = getSharedPreferences("test", Context.MODE_PRIVATE);
        setTheme(sharedpreferences.getInt("theme", R.style.AppTheme));
        setContentView(R.layout.activity_color);
        //address="00:BA:55:56:70:DF";
        
        btAdapt = BluetoothAdapter.getDefaultAdapter();// 初始化本机蓝牙功能  
        if(btAdapt == null){  
                //表明此手机不支持蓝牙  
        new AlertDialog.Builder(ColorActivity.this)
		.setTitle("温馨提示")
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage("您的设备不支持蓝牙功能，确认关闭应用吗？")
		.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						btAdapt.disable();
						adtDevices.clear();
					}
				})
		.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.cancel();// 取消弹出框
					}
				}).create().show(); 
        }  
        // Views
        themeSwitch = (Switch) findViewById(R.id.theme_switch);
        themeSwitch.setChecked(sharedpreferences.getBoolean(themeSwitch.getId()+"", false));
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
               
                if(isChecked){ 
                	if (btAdapt.getState() == BluetoothAdapter.STATE_OFF){
                	Intent discoverableIntent = new Intent(  
                            BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);  
                    discoverableIntent.putExtra(  
                            BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);  
                    startActivity(discoverableIntent);
       
                    BluetoothAdapter.LeScanCallback mLeScanCallback =
                    	      new BluetoothAdapter.LeScanCallback() {

                    	    @Override
                    	    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    	      runOnUiThread(new Runnable() {
                    	        @Override
                    	        public void run() {
                    	        	Toast.makeText(ColorActivity.this, "设备已经找到", 1000).show(); 
                    	        }
                    	      });
                    	    }
                    	  };
                   // startLeScan(UUID[], mLeScanCallback);
                	}
            	     else{
            	           Toast.makeText(ColorActivity.this, "蓝牙已经开启", 1000).show(); 
            	     }
                }
                else
                {
                	btAdapt.disable();
                	Toast.makeText(ColorActivity.this, "蓝牙已经关闭", 1000).show(); 
                }
            } 
        }); 
      //  CheckBox themeCheck = (CheckBox) findViewById(R.id.theme_check);
      //  themeCheck.setChecked(sharedpreferences.getBoolean(themeCheck.getId()+"", false));
      //  themeCheck.setOnCheckedChangeListener(checkedListener);

        revealView = findViewById(R.id.reveal_view);

        // Show the unReveal effect
        final int cx = sharedpreferences.getInt("x", 0);
        final int cy = sharedpreferences.getInt("y", 0);

        startHideRevealEffect(cx, cy);
    }

    private void startHideRevealEffect(final int cx, final int cy) {

        if (cx != 0 && cy != 0) {
            // Show the unReveal effect when the view is attached to the window
            revealView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                    //Get the accent color
                    TypedValue outValue = new TypedValue();
                    getTheme().resolveAttribute(android.R.attr.colorPrimary, outValue, true);
                    revealView.setBackgroundColor(outValue.data);
                   
                    Utils.hideRevealEffect(revealView, cx, cy, 1920);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                }
            });
        }
    }


    private void hideNavigationStatus() {

        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
    }


    Animator.AnimatorListener revealAnimationListener_message = new Animator.AnimatorListener() {

        public void onAnimationEnd(Animator animation) {
        	Intent i = new Intent(ColorActivity.this, MessageActivity.class);
        	startActivity(i);
        }

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			
		}
    };
    Animator.AnimatorListener revealAnimationListener_1 = new Animator.AnimatorListener() {

        public void onAnimationEnd(Animator animation) {
        	/*if(themeSwitch.isChecked()){
                Mintent.putExtra("single", "A");
                Amarino.connect(ColorActivity.this, address);
                Amarino.sendDataToArduino(ColorActivity.this,address,'E',',');
            }
           else{
                Amarino.disconnect(ColorActivity.this,address);
                Toast.makeText(ColorActivity.this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
            }*/
        	Mintent.putExtra("single", "A");
            Amarino.connect(ColorActivity.this, address);
            Amarino.sendDataToArduino(ColorActivity.this,address,'E',',');
        	Intent i = new Intent(ColorActivity.this, FirstActivity.class);
        	startActivity(i);
        }

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			
		}
    };
    Animator.AnimatorListener revealAnimationListener_2 = new Animator.AnimatorListener() {

        public void onAnimationEnd(Animator animation) {
        	/*if(themeSwitch.isChecked()){
            Mintent.putExtra("single", "A");
            Amarino.connect(ColorActivity.this, address);
            Amarino.sendDataToArduino(ColorActivity.this,address,'E',',');
        }
       else{
            Amarino.disconnect(ColorActivity.this,address);
            Toast.makeText(ColorActivity.this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
        }*/
    	Mintent.putExtra("single", "A");
        Amarino.connect(ColorActivity.this, address);
        Amarino.sendDataToArduino(ColorActivity.this,address,'E','.');
    	Intent i = new Intent(ColorActivity.this, FirstActivity.class);
    	startActivity(i);
        }

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			
		}
    };
    Animator.AnimatorListener revealAnimationListener_3 = new Animator.AnimatorListener() {

        public void onAnimationEnd(Animator animation) {
        	/*if(themeSwitch.isChecked()){
            Mintent.putExtra("single", "A");
            Amarino.connect(ColorActivity.this, address);
            Amarino.sendDataToArduino(ColorActivity.this,address,'E',',');
        }
       else{
            Amarino.disconnect(ColorActivity.this,address);
            Toast.makeText(ColorActivity.this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
        }*/
    	Mintent.putExtra("single", "A");
        Amarino.connect(ColorActivity.this, address);
        Amarino.sendDataToArduino(ColorActivity.this,address,'E','&');
    	Intent i = new Intent(ColorActivity.this, FirstActivity.class);
    	startActivity(i);
        }

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			
		}
    };
    Animator.AnimatorListener revealAnimationListener_4 = new Animator.AnimatorListener() {

        public void onAnimationEnd(Animator animation) {
        	/*if(themeSwitch.isChecked()){
            Mintent.putExtra("single", "A");
            Amarino.connect(ColorActivity.this, address);
            Amarino.sendDataToArduino(ColorActivity.this,address,'E',',');
        }
       else{
            Amarino.disconnect(ColorActivity.this,address);
            Toast.makeText(ColorActivity.this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
        }*/
    	Mintent.putExtra("single", "A");
        Amarino.connect(ColorActivity.this, address);
        Amarino.sendDataToArduino(ColorActivity.this,address,'E','?');
    	Intent i = new Intent(ColorActivity.this, FirstActivity.class);
    	startActivity(i);
        }

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			
		}
    };


    public void view(View view) {

        int selectedTheme = 0;
        int primaryColor = 0;

        switch (view.getId()) {
            case R.id.circle1:
                selectedTheme = R.style.theme1;
                primaryColor = getResources().getColor(R.color.color_set_1_primary);
                break;

            case R.id.circle2:
                selectedTheme = R.style.theme2;
                primaryColor = getResources().getColor(R.color.color_set_2_primary);
                break;

            case R.id.circle3:
                selectedTheme = R.style.theme3;
                primaryColor = getResources().getColor(R.color.color_set_3_primary);
                break;

            case R.id.circle4:
                selectedTheme = R.style.theme4;
                primaryColor = getResources().getColor(R.color.color_set_4_primary);
                break;
                
            case R.id.fab_button:
            	selectedTheme = R.style.theme5;
                primaryColor = getResources().getColor(R.color.color_set_5_primary);
                break;
            	
        }
        int [] location = new int[2];
        revealView.setBackgroundColor(primaryColor);
        view.getLocationOnScreen(location);

        int cx = (location[0] + (view.getWidth() / 2));
        int cy = location[1] + (Utils.getStatusBarHeight(this) / 2);

        SharedPreferences.Editor ed = sharedpreferences.edit();
        ed.putInt("x", cx);
        ed.putInt("y", cy);
        ed.putInt("theme", selectedTheme);
        ed.apply();

        switch (view.getId()) {
        case R.id.circle1:
        	hideNavigationStatus();
        	Utils.showRevealEFfect(revealView, cx, cy, revealAnimationListener_1);
            break;

        case R.id.circle2:
        	hideNavigationStatus();
        	Utils.showRevealEFfect(revealView, cx, cy, revealAnimationListener_2);
            break;

        case R.id.circle3:
        	hideNavigationStatus();
        	Utils.showRevealEFfect(revealView, cx, cy, revealAnimationListener_3);
            break;

        case R.id.circle4:
        	hideNavigationStatus();
        	Utils.showRevealEFfect(revealView, cx, cy, revealAnimationListener_4);
            break;
            
        case R.id.fab_button:
        	hideNavigationStatus();
        	Utils.showRevealEFfect(revealView, cx, cy, revealAnimationListener_message);
            break;
        	}
        }
    
        CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences.Editor ed = sharedpreferences.edit();
            ed.putInt("theme", (isChecked) ? R.style.Base_Theme_AppCompat : R.style.Base_Theme_AppCompat_Light);
            ed.putBoolean(buttonView.getId()+"", isChecked);
            ed.apply();
        }
    };
}
