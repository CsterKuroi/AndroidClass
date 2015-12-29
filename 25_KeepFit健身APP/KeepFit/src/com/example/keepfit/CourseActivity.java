/**
 * 
 */
package com.example.keepfit;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import android.R.integer;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

/**
 * @author Cathy M
 *
 */
public class CourseActivity extends Activity{
	int courseId;
	Bundle bundle;
	HashMap<String,Object> selectValue;
	HashMap<String, Object> detailMap;
	
	Button go , go2 ;
	TextView alarmTextView;
	TextView dayTextView;
	Button selectButton;
	
	DBHelper mHelper;
	Calendar c;
	String db_videoaddr;
	
	int pic ;
	String home;
	String desp;
	
	String dayValue;
	
	private String format(int x)
	  {
	    String s=""+x;
	    if(s.length()==1) s="0"+s;
	    return s;
	  }
	@Override
	  public void onCreate(Bundle savedInstanceState)
	  {
		mHelper = new DBHelper(this);
		mHelper.open();
		
		super.onCreate(savedInstanceState);
		  bundle = this.getIntent().getExtras();
		  courseId = bundle.getInt(DBHelper.C_ID);
		  selectValue  = mHelper.getSelect(courseId);
		  if (selectValue == null) {
			  setView1();
		  }
		  if (selectValue != null) {			  
			  setView2();			
		  }		    		         			               
           	   
		}
	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	private void setView1() {
	    setContentView(R.layout.xuanke);
	    selectButton = (Button) findViewById(R.id.xuanke);
	    
	    String courseIdString = courseId+"";
	    detailMap =  mHelper.getCourse( courseIdString );
	    pic = Integer.parseInt((String) detailMap.get(DBHelper.C_PIC)) ;
	    home = (String) detailMap.get(DBHelper.C_ADR_HOME);
	    desp = (String) detailMap.get(DBHelper.C_DESP);
	    
	    ImageView tuImageView = (ImageView) findViewById(R.id.tu);
		TextView jieshaoTextView = (TextView) findViewById(R.id.jieshao);
		tuImageView.setImageResource(pic);
		jieshaoTextView.setText(desp);
		
		
		selectButton.setOnClickListener(new View.OnClickListener() {
	    	public void onClick(View v) {
	    		mHelper.selectCourse(courseId);	    			    			    				   
			    bundle.putInt(DBHelper.C_ID, courseId);
				bundle.putInt(DBHelper.C_PIC, pic);
				bundle.putString(DBHelper.C_ADR_HOME, home);
				Intent intent0 = new Intent(CourseActivity.this,CourseActivity.class);
	    		intent0.putExtras(bundle);
				startActivity(intent0);
	    		CourseActivity.this.finish();
		    }			
	    });
	}
	/*
	  Requires:
	  Modifies:
	  Effects:
	 */
	private void setView2() {
		 setContentView(R.layout.one);
		 c=Calendar.getInstance();
	    //pic
		int pic = bundle.getInt(DBHelper.C_PIC);
	    ImageView imageView = (ImageView) findViewById(R.id.tu);
	    imageView.setImageResource(pic);
	    //alarm
	    alarmTextView = (TextView) findViewById(R.id.naozhong);
	    String alarmValue = (String) selectValue.get(DBHelper.S_ALARM);
	    alarmTextView.setText(alarmValue);
	    alarmTextView.setClickable(true);
	    alarmTextView.setLongClickable(true);
	    alarmTextView.setOnClickListener(new View.OnClickListener()
	    {
	        public void onClick(View v)
	        {
	          /* ȡ�õ����ťʱ��ʱ����ΪTimePickerDialog��Ĭ��ֵ */
	          c.setTimeInMillis(System.currentTimeMillis());
	          int mHour=c.get(Calendar.HOUR_OF_DAY);
	          int mMinute=c.get(Calendar.MINUTE);
	          
	          new TimePickerDialog(CourseActivity.this,
	            new TimePickerDialog.OnTimeSetListener()
	          	{                
	              public void onTimeSet(TimePicker view,int hourOfDay,
	                                    int minute)
	              {
	                /* ȡ�����ú��ʱ�䣬���������Ϊ0 */
	                c.setTimeInMillis(System.currentTimeMillis());
	                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
	                c.set(Calendar.MINUTE,minute);
	                c.set(Calendar.SECOND,0);
	                c.set(Calendar.MILLISECOND,0);
	                
	                /* ָ����������ʱ�䵽ʱҪ����CallAlarm.class */
	                Intent intent = new Intent(CourseActivity.this, AlarmReceiver.class);
	                /* ����PendingIntent */
	                PendingIntent sender=PendingIntent.getBroadcast(
	                		CourseActivity.this,0, intent, 0);
	                /* AlarmManager.RTC_WAKEUP���÷�����ϵͳ����ʱͬ�������� 
	                 * ��set()���õ�PendingIntentֻ������һ��
	                 * */
	                AlarmManager am;
	                am = (AlarmManager)getSystemService(ALARM_SERVICE);
	                am.set(AlarmManager.RTC_WAKEUP,
	                       c.getTimeInMillis(),
	                       sender
	                      );
	                /* ������ʾ����������ʱ�� */
	                String tmpS=format(hourOfDay)+"��"+format(minute);
	                alarmTextView.setText(tmpS);
	                //db 
			        mHelper.updateAlarm(courseId,tmpS);
	                /* ��Toast��ʾ��������� */
	                Toast.makeText(CourseActivity.this,"��������ʱ��Ϊ"+tmpS,
	                  Toast.LENGTH_SHORT)
	                  .show();
	              }          
	            },mHour,mMinute,true) .show();
	          /* ����TimePickerDialog������ʱ�� */
	         	          
	        }
	      }); 
	    alarmTextView.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
				builder.setTitle(R.string.str_alarm_title);
				builder.setMessage(R.string.str_alarm_msg);
				builder.setPositiveButton(R.string.str_ok, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {								
								Intent intent = new Intent(CourseActivity.this, AlarmReceiver.class);
						        PendingIntent sender=PendingIntent.getBroadcast(
						        		CourseActivity.this,0, intent, 0);
						        /* ��AlarmManager��ɾ�� */
						        AlarmManager am;
						        am =(AlarmManager)getSystemService(ALARM_SERVICE);
						        am.cancel(sender);
						        //db 
						        String set = "Ŀǰ������";
						        mHelper.updateAlarm(courseId,set);
						        /* ��Toast��ʾ��ɾ�����ã���������ʾ������ʱ�� */
						        Toast.makeText(CourseActivity.this,"����ʱ����",
						                       Toast.LENGTH_SHORT).show();
						        alarmTextView.setText("Ŀǰ������");
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
	    });
	    
	    //day
	    dayTextView = (TextView) findViewById(R.id.tianshu);
	    dayValue = (String) selectValue.get(DBHelper.S_DAY);
	    dayTextView.setText(dayValue);
	    
	    //playvedio
	    go=(Button)findViewById(R.id.playvideo);
	    db_videoaddr = bundle.getString(DBHelper.C_ADR_HOME);		    
	    go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	int dayInt = Integer.parseInt(dayValue);
            	dayInt++;
            	dayValue = dayInt+"";
            	dayTextView.setText(dayValue);
            	mHelper.updateDay(courseId,dayValue);
                Intent intent =new Intent();
                intent.putExtra("videoname",db_videoaddr);
                intent.setClass(CourseActivity.this,VedioPlayActivity.class);
                startActivity(intent);
            }
        });
	    
	  //daka
	    go2=(Button)findViewById(R.id.daka);	    
	    go2.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
		
			//File f = new File("/sdcard/Pictures/ScreenShots/11.JPG");
            		//Uri imageUri = Uri.fromFile(f);
			
			
			File f = new File("/storage/emulated/0/keepfit/a.jpg");
           	Uri imageUri = Uri.fromFile(f);
						
			Intent intent=new Intent();
			intent.setComponent(new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI"));
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("image/*");  
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);  
			intent.putExtra("Kdescription", "#KeepFit����# ����������KeepFit������1�죬������1000��·������ͱ�����һ������ɣ�");
			intent.putExtra(Intent.EXTRA_STREAM, imageUri);
			startActivity(Intent.createChooser(intent, "��ͼƬ������Ȧ��"));

		}
	});
		
	}

}
