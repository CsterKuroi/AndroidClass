package com.heyu.activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.herveweather.R;
import com.heyu.utils.Utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectCityActivity extends Activity {
	private String[] citys;
	private ImageView back;
	private GridView cityList;
	private Intent intent;
	private EditText inputCity;
	private Button search;
	private ProgressDialog dialog;
	private Builder builder;
	private String city;
	
	// ʹ�ðٶ�LLS����
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_city);
		
		// ����LocationClient��
		mLocationClient = new LocationClient(getApplicationContext());
		// ע���������
		mLocationClient.registerLocationListener(myListener);
		// ���ö�λ����
		setLocationOption();
		dialog = new ProgressDialog(SelectCityActivity.this);
		dialog.setMessage("���ڶ�λ...");
		dialog.setCanceledOnTouchOutside(false);

		citys = getResources().getStringArray(R.array.citys);
		cityList = (GridView) findViewById(R.id.city_list);
		back = (ImageView) findViewById(R.id.back);
		inputCity = (EditText) findViewById(R.id.input_city);
		search = (Button) findViewById(R.id.search);
		back.setOnClickListener(new ButtonListener());
		search.setOnClickListener(new ButtonListener());
		inputCity.addTextChangedListener(new Watcher());
		cityList.setAdapter(new MyAdapter(SelectCityActivity.this));
		cityList.setOnItemClickListener(new ClickListener());
	}
	
	/**
	 * ���ö�λ������ ��λģʽ�����ζ�λ����ʱ��λ���������������ͣ��Ƿ��GPS�ȵȡ�
	 */
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// ���صĶ�λ���������ַ��Ϣ
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setScanSpan(24 * 60 * 60 * 1000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.disableCache(true);// ��ֹ���û��涨λ
		option.setPoiNumber(5); // ��෵��POI����
		option.setPoiDistance(1000); // poi��ѯ����
		option.setPoiExtraInfo(true); // �Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ
		mLocationClient.setLocOption(option);
	}
	
	
	/**
	 * ʵ��BDLocationListener�ӿ�
	 * BDLocationListener�ӿ���2��������Ҫʵ�֣� 
	 * 1.�����첽���صĶ�λ�����������BDLocation���Ͳ�����
	 * 2.�����첽���ص�POI��ѯ�����������BDLocation���Ͳ�����
	 * POI�ǰٶ��ṩ�ĵ�����ͼ��Ϣ��ÿ��POI�����ķ�����Ϣ�����ơ���𡢾���γ�ȡ������ľƵ극�����̵���Ϣ
	 */
	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			dialog.cancel();
			int code = location.getLocType();
			String addr = location.getAddrStr();
			if (code == 161 && addr != null) {
				// ��λ�ɹ�
				System.out.println(addr);
				city = formatCity(addr);
				intent = new Intent();
				intent.putExtra("city", city);
				SelectCityActivity.this.setResult(1, intent);
				SelectCityActivity.this.finish();
			} else {
				// ��λʧ��
				builder = new Builder(SelectCityActivity.this);
				builder.setTitle("��ʾ");
				builder.setMessage("�Զ���λʧ�ܡ�");
				builder.setPositiveButton("����",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (Utils.checkNetwork(SelectCityActivity.this) == false) {
									Toast.makeText(SelectCityActivity.this,
											"�����쳣,������������", Toast.LENGTH_SHORT)
											.show();
									return;
								}
								SelectCityActivity.this.dialog.show();
								requestLocation();
							}
						});
				builder.setNegativeButton("ȡ��", null);
				builder.setCancelable(false);
				builder.show();
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	//  ��λ����Ϣת��Ϊ����
	private String formatCity(String addr) {
		String city = null;
		if (addr.contains("������") && addr.contains("��")) {
			city = addr.substring(addr.indexOf("��") + 1, addr.indexOf("��"));
		} else if (addr.contains("��")) {
			city = addr.substring(addr.indexOf("��") + 1, addr.indexOf("��"));
		} else {
			int start = addr.indexOf("��");
			int end = addr.lastIndexOf("��");
			if (start == end) {
				if (addr.contains("ʡ")) {
					city = addr.substring(addr.indexOf("ʡ") + 1,
							addr.indexOf("��"));
				} else if (addr.contains("��")) {
					city = addr.substring(0, addr.indexOf("��"));
				}
			} else {
				city = addr.substring(addr.indexOf("��") + 1,
						addr.lastIndexOf("��"));
			}
		}
		return city;
	}

	// ����λ����Ϣ
	private void requestLocation() {
		if (mLocationClient.isStarted() == false) {
			mLocationClient.start();
		} else {
			mLocationClient.requestLocation();
		}
	}
	
	@SuppressLint("InflateParams") 
	class MyAdapter extends BaseAdapter {
		private Context mContext;
		private MyAdapter(Context mContext) {
			this.mContext = mContext;
		}
		@Override
		public int getCount() {
			return citys.length;
		}
		@Override
		public Object getItem(int arg0) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.city_item, null);
				holder = new ViewHolder();
				holder.city = (TextView) convertView.findViewById(R.id.city);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.city.setText(citys[position]);
			return convertView;
		}

	}

	class ViewHolder {
		TextView city;
	}

	class ClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
			city = citys[arg2];
			if ("�Զ���λ".equals(city)) {
				if (Utils.checkNetwork(SelectCityActivity.this) == false) {
					Toast.makeText(SelectCityActivity.this, "�����쳣,������������",
							Toast.LENGTH_SHORT).show();
					return;
				}
				dialog.show();
				requestLocation();
			} else {
				intent = new Intent();
				intent.putExtra("city", city);
				SelectCityActivity.this.setResult(1, intent);
				SelectCityActivity.this.finish();
			}
		}

	}

	// �����ı���
	class Watcher implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (inputCity.getText().toString().length() == 0) {
				search.setVisibility(View.GONE);
			} else {
				search.setVisibility(View.VISIBLE);
			}
		}
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back: // �˳�ȥ
				back();
				break;
			case R.id.search: // ����
				city = inputCity.getText().toString();
				intent = new Intent();
				intent.putExtra("city", city);
				SelectCityActivity.this.setResult(1, intent);
				SelectCityActivity.this.finish();
				break;
			default:
				break;
			}
		}

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// backǰ�ж��Ƿ������Activity
	private void back() {
		intent = getIntent();
		if ("".equals(intent.getStringExtra("city"))) {
			MainActivity.context.finish();
		}
		SelectCityActivity.this.finish();
		System.exit(0);
	}


}
