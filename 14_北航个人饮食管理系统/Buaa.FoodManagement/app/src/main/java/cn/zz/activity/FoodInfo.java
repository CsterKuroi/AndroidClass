package cn.zz.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.util.Log;





public class FoodInfo extends ListActivity {
	private Button like = null;
	Button image=null;
	Button back=null;
	TextView foodinfo=null;
	String[] efood={"合一楼"};
	String [] efoodinfo={"3层xx号窗口，当前该层食堂状态为繁忙/空闲"};
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.foodinfo);
	Bundle bundle=getIntent().getExtras();

	like=(Button)findViewById(R.id.like);
	like.setOnClickListener(new MyButtonListener());//这里是对like button的设置

	int drawable=bundle.getInt("drawable");
	String foodname=bundle.getString("foodname");
	String efoodname=bundle.getString("efoodnema");
	String foodinfos=bundle.getString("foodinfo");
	image=(Button)findViewById(R.id.Button);
	image.setBackgroundResource(drawable);
	image.setText(foodname);
	foodinfo=(TextView)findViewById(R.id.TextView03);
	foodinfo.setText(foodinfos);
	back=(Button)findViewById(R.id.backbutton);
	back.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Button b=(Button)v;
			b.setBackgroundResource(R.drawable.btn_back_active);
			Intent intent=new Intent(FoodInfo.this, FoodListView.class);
			startActivity(intent);
		}
	});
	List<Map<String, Object>> data=new ArrayList<Map<String,Object>>();
	for(int i=0;i<efood.length;i++){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("TextView04", efood[i]);
		map.put("TextView05", efoodinfo[i]);
		data.add(map);
	}
	SimpleAdapter adapter=new SimpleAdapter(this, data, R.layout.ex_foodinfo,new String[]{"TextView04","TextView05"} , new int[]{R.id.TextView04,R.id.TextView05});
	setListAdapter(adapter);
	}

	class MyButtonListener implements OnClickListener
	{

		public void onClick(View v)
		{


			if(v.getId()==R.id.like) {
				like.setBackgroundResource(R.drawable.like_active);

				new AlertDialog.Builder(FoodInfo.this).setTitle("系统提示")//设置对话框标题

						.setMessage("已成功加入喜爱！")//设置显示的内容

						.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


									@Override

									public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

										// TODO Auto-generated method stub

										finish();

									}

								}

						).show();
			}else if(v.getId()==R.id.send){
				EditText editText = (EditText)findViewById(R.id.edit_message);
				String message = editText.getText().toString();
				new AlertDialog.Builder(FoodInfo.this).setTitle("系统提示")//设置对话框标题

						.setMessage(message)//设置显示的内容

						.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮


									@Override

									public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

										// TODO Auto-generated method stub

										finish();

									}

								}

						).show();
			}
		}


	}
}
