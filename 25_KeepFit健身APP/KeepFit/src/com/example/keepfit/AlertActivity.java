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
	    /* ���������徯ʾ  */
	    new AlertDialog.Builder(AlertActivity.this)
	        .setIcon(R.drawable.clock)
	        .setTitle("�����õĶ�������ʱ�䵽��")
	        .setMessage("��ʼ�����")
	        .setPositiveButton("���ھ�ȥ��",
	         new DialogInterface.OnClickListener()
	        {
	          public void onClick(DialogInterface dialog, int whichButton)
	          {
	            /* �ر�Activity */
	            AlertActivity.this.finish();
	          }
	        })
	        .show();
	  } 
	}