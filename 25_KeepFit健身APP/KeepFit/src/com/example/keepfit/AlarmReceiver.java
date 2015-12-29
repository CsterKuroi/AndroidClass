/**
 * 
 */
package com.example.keepfit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Cathy M
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	@Override
	  public void onReceive(Context context, Intent intent)
	  {
	    /* create Intent£¬µ÷ÓÃAlarmAlert.class */
	    Intent i = new Intent(context, AlertActivity.class);
	        
	    Bundle bundleRet = new Bundle();
	    bundleRet.putString("STR_CALLER", "");
	    i.putExtras(bundleRet);
	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(i);
	  }

}
