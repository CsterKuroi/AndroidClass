package y.Curriculum;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Curriculum extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView tv=(TextView)findViewById(R.id.day);
        Date now=new Date();
        SimpleDateFormat f=new SimpleDateFormat("yyyy年MM月dd日");
        tv.setText(f.format(now).toString());//在首页显示了时间
        
        ListView lv = (ListView)findViewById(R.id.ListView01);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, 
        new String[]{getString(R.string.mon),getString(R.string.tue),
        getString(R.string.wed),getString(R.string.thu),getString(R.string.fri),
        getString(R.string.sat),getString(R.string.sun)});
        //数组适配器
        lv.setAdapter(aa);//数据更新
        lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView< ?> arg0, View arg1, int arg2,
					long arg3) {
				
				Intent i=new Intent(Curriculum.this,Day.class);
				i.putExtra("d", arg2);
				startActivity(i);//转到day.java
				
				
			}
        	
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater=getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }//menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    	case R.id.help:
    		new AlertDialog.Builder(this).setTitle(R.string.help).setMessage(R.string.help_text).setIcon(android.R.drawable.ic_menu_help).show();
    		break;    	
    	case R.id.exit:
    	{
    		new AlertDialog.Builder(this).setTitle("确认退出？ ").setPositiveButton("是",
    				new DialogInterface.OnClickListener() 
    				{
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							
							new AlertDialog.Builder(Curriculum.this).setMessage(
									" ").create().show();
							finish();
							
						}
					}).setNegativeButton("否",
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
									new AlertDialog.Builder(Curriculum.this).setMessage(
											" ").create().show();
									
								}
							}).show();
    		}
    	}
    	return false;
    }
}