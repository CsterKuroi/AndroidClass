/**
 * 
 */
package com.example.keepfit;


import java.io.File;
import java.util.*;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class shangdianActivity extends Activity {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	DBHelper mHelper;
	ArrayList<HashMap<String, Object>> allList;
	ListView list;
	static public String path = "/storage/emulated/0/keepfit";
	Object[] searchBy = new Object[3];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 mHelper = new DBHelper(this);
		 mHelper.open();  
		 if (mHelper.getUser() == null) {
			mHelper.insertInit();
			File sd=Environment.getExternalStorageDirectory(); 
	        path=sd.getPath()+"/keepfit"; 
	        File file=new File(path); 
	        if(!file.exists()) 
	         file.mkdir(); 
		}
		
		
		setContentView(R.layout.shangdian);
		
		Button b1 = (Button)findViewById(R.id.button1);
	    b1.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(shangdianActivity.this,kechengActivity.class);
	    		startActivity(intent0);
	    		shangdianActivity.this.finish();
		    }			
	    });
		Button b2 = (Button)findViewById(R.id.button2);
	    b2.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		Intent intent0 = new Intent(shangdianActivity.this,woActivity.class);
	    		startActivity(intent0);
	    		shangdianActivity.this.finish();
		    }			
	    });
	    
	   
	    allList = mHelper.getAllCourse();
	    setListLayout(); 
	    setSearch();
	}
	
	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	private void setSearch() {
		searchBy[0] = SexChoice.ALL;
		searchBy[1] = PartChoice.ALL;
		searchBy[2] = HardChoice.ALL;
		Spinner sexSpinner = (Spinner) findViewById(R.id.xingbie);
		sexSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					searchBy[0] = SexChoice.ALL;
					break;
				case 1:
					searchBy[0] = SexChoice.MAN;
					break;	
				case 2:
					searchBy[0] = SexChoice.WOMEN;
					break;
				default:
					break;
				}
				
				allList = mHelper.search(searchBy);
				setListLayout();
		
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		Spinner partSpinner = (Spinner) findViewById(R.id.buwei);
		partSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					searchBy[1] = PartChoice.ALL;
					break;
				case 1:
					searchBy[1] = PartChoice.LEG;
					break;	
				case 2:
					searchBy[1] = PartChoice.STOMACH;
					break;
				case 3:
					searchBy[1] = PartChoice.BACK;
					break;	
				case 4:
					searchBy[1] = PartChoice.ARM;
					break;
				default:
					break;
				}				
				allList = mHelper.search(searchBy);
				setListLayout();				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {				
			}			
		});
		
		Spinner hardSpinner = (Spinner) findViewById(R.id.dengji);
		hardSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					searchBy[2] = HardChoice.ALL;
					break;
				case 1:
					searchBy[2] = HardChoice.EASY;
					break;	
				case 2:
					searchBy[2] = HardChoice.MEDIUM;
					break;
				case 3:
					searchBy[2] = HardChoice.HARD;
					break;
				default:
					break;
				}			
				allList = mHelper.search(searchBy);
				setListLayout();						
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {				
			}			
		});
	}

	private void setListLayout() {   				
		SimpleAdapter sAdapter = new SimpleAdapter(
				this, 
				allList, 
				R.layout.course_row, 
				new String[]{DBHelper.C_PIC}, 
				new int[]{R.id.img});
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(sAdapter);
			
		class  ItemClickListener implements OnItemClickListener{     
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
				   
				    Toast.makeText(shangdianActivity.this, "this is : "+pic,Toast.LENGTH_SHORT).show();  
					Intent i = new Intent(shangdianActivity.this, CourseActivity.class);
				
					Bundle bundle = new Bundle();
					bundle.putInt(DBHelper.C_ID, courseId);
					bundle.putInt(DBHelper.C_PIC, pic);
					bundle.putString(DBHelper.C_ADR_HOME, home);
					i.putExtras(bundle);
	
					startActivityForResult(i, ACTIVITY_EDIT);
				}     
		         
		}    
	
		ItemClickListener mItemClickListener = new ItemClickListener();
		
		list.setOnItemClickListener(mItemClickListener);
 
	}
}
