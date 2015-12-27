package com.heyu.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {
	
	// 检查网络连接：
	public static boolean checkNetwork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {

					return true;
				}
			}
		}
		return false;
	}
	
	// 获取版本号
	public static String getVersion(Context context) {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = null;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info.versionName;
	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


}
