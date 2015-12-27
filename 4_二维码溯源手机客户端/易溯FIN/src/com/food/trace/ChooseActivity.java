package com.food.trace;

import java.util.List;

import com.bmob.BmobConfiguration;
import com.bmob.BmobPro;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
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
import android.os.Bundle;
import android.provider.Contacts.People;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bmob.initialize(this, "81426109af11587a91141e2bb4f14fa0");
		setContentView(R.layout.activity_main);
		
		// button back
//		Button scan = (Button) this.findViewById(R.id.scan);
//		scan.setOnClickListener(new Button.OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(ChooseActivity.this, CaptureActivity.class);
//				startActivity(intent);
//			}
//		});
//		Button gen = (Button) this.findViewById(R.id.gen);
//		gen.setOnClickListener(new Button.OnClickListener() {
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(ChooseActivity.this, GenActivity.class);
//				startActivity(intent);
//			}
//		});
	}
}
