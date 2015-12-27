package com.food.trace;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;
import com.food.trace.bean.Information;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class UserHomeActivity extends Activity {
	
	private Context mcontext=null;
	private View v1=null;
	static public User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, "81426109af11587a91141e2bb4f14fa0");

		setContentView(R.layout.activity_user_home);
		
		Button gen = (Button) this.findViewById(R.id.gen);
		gen.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UserHomeActivity.this,GenActivity.class);
				startActivity(intent);
			}
		});
		
		Button scan = (Button)this.findViewById(R.id.scan);
		scan.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(UserHomeActivity.this,CaptureActivity.class);
				startActivity(intent);
			}
		});
		
		
		
		Button insert = (Button)this.findViewById(R.id.insert);
		insert.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v){
				showPopupWindow(v);
				
			}
		});
		
		
	}
	
	
	private void showPopupWindow(View view){
		v1 = LayoutInflater.from(this).inflate(R.layout.insert_info, null);
		final PopupWindow pop = new PopupWindow(v1,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
		pop.setTouchable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.showAsDropDown(view,5,5);
		
		Button update = (Button)v1.findViewById(R.id.update);
		update.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				insertInfo(
						((TextView)v1.findViewById(R.id.editText1)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText2)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText3)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText4)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText5)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText6)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText7)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText8)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText9)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText10)).getText().toString(),
						((TextView)v1.findViewById(R.id.editText11)).getText().toString()					
						);
				pop.dismiss();
			}
			
		});
	}
	private void insertInfo(String conductName,String conductType,String procedureName,
			String place,String operator,String year,String month,String day,String hour,String minute,String enterprise){
		Information info = new Information();
		Calendar cal = Calendar.getInstance(); 
		cal.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
				Integer.parseInt(hour), Integer.parseInt(minute));
		Date date = cal.getTime();		
		BmobDate bmobDate = new BmobDate(date);
		info.setConductName(conductName);
		info.setConductType(conductType);
		info.setEnterprise(enterprise);
		info.setOperateTime(bmobDate);
		info.setOperator(operator);
		info.setPlace(place);
		info.setProcedureName(procedureName);
		info.setWriterId(user.getUsername());
		info.save(UserHomeActivity.this, new SaveListener(){
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
		        Toast.makeText(UserHomeActivity.this, "加入失败！", 100).show();
		        Log.i("bmob", "加入失败！");
				
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(UserHomeActivity.this, "加入成功！", 100).show();
				Log.i("bmob", "加入成功！");
//				Intent intent = new Intent();
//				intent.setClass(RegisterActivity.this, HomeActivity.class);
//				startActivity(intent);
//				EventActivity.this.finish();
			}
		});
	}

}
