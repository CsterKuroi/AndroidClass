package com.food.trace;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.food.trace.bean.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class ShowInf extends Activity {

	private ListView lvofitem=null;
	String showInf="c356461612::10000000";
	String[] erweima;
	char[] chs;
	List<Information> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listinfor);
		showInf=this.getIntent().getStringExtra("content");
		erweima=showInf.split("::");
		if(erweima.length!=2)
			Log.i("slj", "错误描述：二维码包含的信息不正确！");
		chs=erweima[1].toCharArray();
		if(chs.length!=8)
			Log.i("slj", "错误描述：二维码包含的信息不正确！");
		String bql ="select * from Information where conductName =("
				+ "select conductName from Information where objectId = \""+erweima[0]+"\")";
		new BmobQuery<Information>().doSQLQuery(ShowInf.this,bql,new SQLQueryListener<Information>(){
		    public void done(BmobQueryResult<Information> result, BmobException e) {
		    	//这里的传入的result就是查询结果
		        if(e ==null){
		            list= (List<Information>) result.getResults();
		            if(list!=null && list.size()>0){
		            	//Toast.makeText(ShowInf.this, ""+list.get(0).getUsername(), 100).show();
		            }else{
		                Log.i("smile", "查询成功，无数据返回");
		            }
		        }else{
		            Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
		        }

				lvofitem = (ListView)findViewById(R.id.lv);
				lvofitem.setAdapter(new MyAdapter());
		    }
		});
		
	}

	private class MyAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Information one=list.get(position);
			View view = View.inflate(ShowInf.this,R.layout.list_item, null);
			TextView type=(TextView)view.findViewById(R.id.ConductTypeValue);
			type.setText(one.getConductType()==null?"未填写":one.getConductType());
			if(chs[0]=='0')
			{
				LinearLayout l1=(LinearLayout)view.findViewById(R.id.ll1);
				l1.setVisibility(View.GONE);
				type.setVisibility(View.GONE);
				TextView type1=(TextView)view.findViewById(R.id.ConductType);
				type1.setVisibility(View.GONE);
			}
			TextView name=(TextView)view.findViewById(R.id.ConductNameValue);
			name.setText(one.getConductName()==null?"未填写":one.getConductName());
			if(chs[1]=='0')
			{
				LinearLayout l2=(LinearLayout)view.findViewById(R.id.ll2);
				l2.setVisibility(View.GONE);
				name.setVisibility(View.GONE);
				TextView name1=(TextView)view.findViewById(R.id.ConductName);
				name1.setVisibility(View.GONE);
			}
			TextView process=(TextView)view.findViewById(R.id.ProcedureNameValue);
			process.setText(one.getProcedureName()==null?"未填写":one.getProcedureName());
			if(chs[2]=='0')
			{
				LinearLayout l3=(LinearLayout)view.findViewById(R.id.ll3);
				l3.setVisibility(View.GONE);
				process.setVisibility(View.GONE);
				TextView process1=(TextView)view.findViewById(R.id.ProcedureName);
				process1.setVisibility(View.GONE);
			}
			TextView place=(TextView)view.findViewById(R.id.PlaceValue);
			place.setText(one.getPlace()==null?"未填写":one.getPlace());
			if(chs[3]=='0')
			{
				LinearLayout l4=(LinearLayout)view.findViewById(R.id.ll4);
				l4.setVisibility(View.GONE);
				place.setVisibility(View.GONE);
				TextView place1=(TextView)view.findViewById(R.id.Place);
				place1.setVisibility(View.GONE);
			}
			TextView OperatorValue=(TextView)view.findViewById(R.id.OperatorValue);
			OperatorValue.setText(one.getOperator()==null?"未填写":one.getOperator());
			if(chs[4]=='0')
			{
				LinearLayout l5=(LinearLayout)view.findViewById(R.id.ll5);
				l5.setVisibility(View.GONE);
				OperatorValue.setVisibility(View.GONE);
				TextView Operator=(TextView)view.findViewById(R.id.Operator);
				Operator.setVisibility(View.GONE);
			}
			TextView OperateTimeValue=(TextView)view.findViewById(R.id.OperateTimeValue);
			//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			//OperateTimeValue.setText(sdf.format(one.getOperateTime()));
			OperateTimeValue.setText(one.getOperateTime()==null?"未填写":one.getOperateTime().getDate());
			if(chs[5]=='0')
			{
				LinearLayout l6=(LinearLayout)view.findViewById(R.id.ll6);
				l6.setVisibility(View.GONE);
				OperateTimeValue.setVisibility(View.GONE);
				TextView OperateTime=(TextView)view.findViewById(R.id.OperateTime);
				OperateTime.setVisibility(View.GONE);
			}
			TextView EnterpriseValue=(TextView)view.findViewById(R.id.EnterpriseValue);
			EnterpriseValue.setText(one.getEnterprise()==null?"未填写":one.getEnterprise());
			if(chs[6]=='0')
			{
				LinearLayout l7=(LinearLayout)view.findViewById(R.id.ll7);
				l7.setVisibility(View.GONE);
				EnterpriseValue.setVisibility(View.GONE);
				TextView Enterprise=(TextView)view.findViewById(R.id.Enterprise);
				Enterprise.setVisibility(View.GONE);
			}
			TextView WriterIdValue=(TextView)view.findViewById(R.id.WriterIdValue);
			WriterIdValue.setText(one.getWriterId()==null?"未填写":one.getWriterId());
			if(chs[7]=='0')
			{
				LinearLayout l8=(LinearLayout)view.findViewById(R.id.ll8);
				l8.setVisibility(View.GONE);
				WriterIdValue.setVisibility(View.GONE);
				TextView WriterId=(TextView)view.findViewById(R.id.WriterId);
				WriterId.setVisibility(View.GONE);
			}
			TextView time_value=(TextView)view.findViewById(R.id.time_value);
			time_value.setText(one.getCreatedAt().toString());
			return view;
		}
	}
}
