package com.food.trace;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ListViewofInf extends Activity {

	private ListView lvofinf=null;
	private ListView lvofitem=null;
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    if(listView == null) return;
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }
	    int totalHeight = 0;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);
	        totalHeight += listItem.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listinfor);
		

		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String, Object>>();

		lvofitem = (ListView)findViewById(R.id.lv_item);
		lvofitem.setAdapter(new MyAdapter1());
		lvofinf = (ListView)findViewById(R.id.lv);
		lvofinf.setAdapter(new MyAdapter2());
	}

	private class MyAdapter1 extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
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
			
			View view = View.inflate(ListViewofInf.this,R.layout.iteminf, null);
			
			return view;
		}
	}
	private class MyAdapter2 extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
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
			
			View view = View.inflate(ListViewofInf.this,R.layout.list_item, null);
			
			return view;
		}
	}
	
}
