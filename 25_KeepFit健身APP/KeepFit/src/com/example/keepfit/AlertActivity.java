/**
 * 
 */
package com.example.keepfit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * @author Cathy M
 *
 */
public class AlertActivity extends Activity
{
	  @Override
	  protected void onCreate(Bundle savedInstanceState) 
	  {
	    super.onCreate(savedInstanceState);
	    /* 跳出的闹铃警示  */
	    new AlertDialog.Builder(AlertActivity.this)
	        .setIcon(R.drawable.clock)
	        .setTitle("您设置的锻炼提醒时间到了")
	        .setMessage("开始热身吧")
	        .setPositiveButton("现在就去！",
	         new DialogInterface.OnClickListener()
	        {
	          public void onClick(DialogInterface dialog, int whichButton)
	          {
	            /* 关闭Activity */
	            AlertActivity.this.finish();
	          }
	        })
	        .show();
	  } 
	}