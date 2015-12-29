/**
 * 
 */
package com.example.keepfit;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class kechengActivity extends Activity {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	
	DBHelper mHelper;
	ArrayList<HashMap<String, Object>> allList;
	ListView list;
	String courseIdString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kecheng);
		Button b0 = (Button)findViewById(R.id.button0);
	    b0.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(kechengActivity.this,shangdianActivity.class);
	    		startActivity(intent0);
	    		kechengActivity.this.finish();
		    }			
	    });
		Button b2 = (Button)findViewById(R.id.button2);
	    b2.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(kechengActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		kechengActivity.this.finish();
		    }			
	    });
	   
	    mHelper = new DBHelper(this);
	    mHelper.open();  
    
	    setListLayout(); 
	}
	private void setListLayout() {
		allList = mHelper.getSelectCourse();
		
		SimpleAdapter sAdapter = new SimpleAdapter(
				this, 
				allList, 
				R.layout.course_row, 
				new String[]{DBHelper.C_PIC}, 
				new int[]{R.id.img});
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(sAdapter);
			
		class  ItemClickListener 
		implements OnItemClickListener,OnItemLongClickListener{     
				public void onItemClick(AdapterView<?> bigView,//The AdapterView where the click happened      
		                                  View smallView,//The view within the AdapterView that was clicked     
		                                  int position,//The position of the view in the adapter     
		                                  long id//The row id of the item that was clicked     
		                                  ) {     //在本例中arg2=arg3     
				    @SuppressWarnings("unchecked")
					HashMap<String, Object> value=(HashMap<String, Object>) bigView.getItemAtPosition(position);     
				    //显示所选Item的ItemText     
				    
				    String courseIdString =  value.get(DBHelper.C_ID).toString();
				    int courseId = Integer.parseInt(courseIdString );
				    HashMap<String, Object> detailMap =  mHelper.getCourse( courseIdString );
				    int pic = Integer.parseInt((String) detailMap.get(DBHelper.C_PIC)) ;
				    String home = (String) detailMap.get(DBHelper.C_ADR_HOME);
				   
				    Toast.makeText(kechengActivity.this, "this is : "+pic,Toast.LENGTH_SHORT).show();  
					Intent i = new Intent(kechengActivity.this, CourseActivity.class);
				
					Bundle bundle = new Bundle();
					bundle.putInt(DBHelper.C_ID, courseId);
					bundle.putInt(DBHelper.C_PIC, pic);
					bundle.putString(DBHelper.C_ADR_HOME, home);
					i.putExtras(bundle);
	
					startActivityForResult(i, ACTIVITY_EDIT);
				}

				/* (non-Javadoc)
				 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
				 */
				@Override
				public boolean onItemLongClick(AdapterView<?> bigView,
						View smallView, int position, long id) {
					@SuppressWarnings("unchecked")
					HashMap<String, Object> value=(HashMap<String, Object>) bigView.getItemAtPosition(position);     
				    //显示所选Item的ItemText      
					courseIdString =  value.get(DBHelper.C_ID).toString();
				    
					AlertDialog.Builder builder = new AlertDialog.Builder(kechengActivity.this);
					builder.setTitle(R.string.str_dcourse_title);
					builder.setMessage(R.string.str_dcourse_msg);
					builder.setPositiveButton(R.string.str_ok, 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {								
									
							        //db 
							        mHelper.deleteSelect(courseIdString);
							        /* 以Toast提示已删除设置，并更新显示的闹钟时间 */
							        Toast.makeText(kechengActivity.this,"已经退选本课程",
							                       Toast.LENGTH_SHORT).show();
							        Intent intent =new Intent();
					                intent.setClass(kechengActivity.this,kechengActivity.class);
					                startActivity(intent);
								}
							});
					builder.setNegativeButton(R.string.str_cancel, 
							new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {								
						}
					});
					builder.create().show();
					return true;
				}     
		         
		}    
		
		
	
		ItemClickListener mItemClickListener = new ItemClickListener();
		
		list.setOnItemClickListener(mItemClickListener);
		list.setOnItemLongClickListener(mItemClickListener);
 
	}
}
