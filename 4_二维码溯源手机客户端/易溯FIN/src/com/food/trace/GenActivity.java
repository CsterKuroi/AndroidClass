package com.food.trace;


import java.util.List;

import com.food.trace.bean.Information;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class GenActivity extends Activity {

	private Button gen=null;
	private EditText et=null;
	CheckBox[] checks=new CheckBox[8];
	List<Information> list;
	String showInf="欢迎使用 易溯！";
	List<String> e;
	StringBuffer sb;
	boolean flag=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gen2);
		gen=(Button)findViewById(R.id.make);
		checks[0]=(CheckBox)findViewById(R.id.checkBox1);
		checks[1]=(CheckBox)findViewById(R.id.checkBox2);
		checks[2]=(CheckBox)findViewById(R.id.checkBox3);
		checks[3]=(CheckBox)findViewById(R.id.checkBox4);
		checks[4]=(CheckBox)findViewById(R.id.checkBox5);
		checks[5]=(CheckBox)findViewById(R.id.checkBox6);
		checks[6]=(CheckBox)findViewById(R.id.checkBox7);
		checks[7]=(CheckBox)findViewById(R.id.checkBox8);
		et=(EditText)findViewById(R.id.input);
		gen.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				sb=new StringBuffer();
				String bql ="select * from Information where conductName ="+"\""+et.getText()+"\"";
				new BmobQuery<Information>().doSQLQuery(GenActivity.this,bql,new SQLQueryListener<Information>(){
				    public void done(BmobQueryResult<Information> result, BmobException e) {
				    	//这里的传入的result就是查询结果
				        if(e ==null){
				            list= (List<Information>) result.getResults();
				            if(list!=null && list.size()>0){
					        	flag=true;
								sb.append(list.get(0).getObjectId()+"::");
				            	
				            	//Toast.makeText(ShowInf.this, ""+list.get(0).getUsername(), 100).show();
				            }else{
				            	Toast.makeText(GenActivity.this, "不存在名为："+et.getText()+"产品的生产信息记录！请核实！", 1000).show();
				            }
				        }else{
				            Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
				        }

						if(flag)
						{
							for(int i=0;i<8;i++)
							{
								if(checks[i].isChecked())
									sb.append("1");
								else sb.append("0");
							}
							showInf=new String(sb);
							Toast.makeText(GenActivity.this, "二维码信息为："+showInf, 1000).show();
							Intent intent=new Intent();
							intent.putExtra("content", showInf);
							intent.setClass(GenActivity.this, Input.class);
							startActivity(intent);
						}
				    }
				});
			}
		});
	}

	
}
