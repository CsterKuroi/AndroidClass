package com.heyu.activity;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.weixvn.wae.manager.EngineManager;

import com.example.herveweather.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.heyu.utils.UpdateWeather;
import com.heyu.utils.Utils;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends Activity {
	
	final private String DATE_KEY[] = { "date_0", "date_1", "date_2", "date_3" };
	final private String WEATHER_KEY[] = { "weather_0", "weather_1", "weather_2", "weather_3" };
	final private String WIND_KEY[] = { "wind_0", "wind_1", "wind_2", "wind_3" };
	final private String TEMPERATURE_KEY[] = { "temperature_0","temperature_1", "temperature_2", "temperature_3" };
	final private String TITLE_KEY[] = { "TITLE_0", "TITLE_1", "TITLE_2", "TITLE_3","TITLE_4","TITLE_5" };
	final private String ZS_KEY[] = { "ZS_0", "ZS_1", "ZS_2", "ZS_3","ZS_4","ZS_5" };
	final private String TIPT_KEY[] = { "TIPT_0", "TIPT_1", "TIPT_2", "TIPT_3","TIPT_4","TIPT_5" };
	final private String DES_KEY[] = { "DES_0","DES_1", "DES_2", "DES_3","DES_4","DES_5" };
	private String[] titleArray;
	private String[] zsArray;
	private String[] tiptArray;
	private String[] desArray;
	// �õ��Ŀؼ���
	public static Handler handler;
	public static MainActivity context;
	private String[] dateArray, weatherArray, windArray, temperatureArray;
	private SharedPreferences sp;
	private LinearLayout weatherBg;
	private LinearLayout titleBarLayout;
	private LinearLayout changeCity;
	private TextView cityText;
	private ImageView share;
	private ImageView about;
	private ImageView refresh;
	private ProgressBar refreshing;
	private TextView updateTimeText;
	private ScrollView scrollView;
	private LinearLayout currentWeatherLayout;
	private ImageView weatherIcon;
	private TextView currentTemperatureText;
	private TextView currentWeatherText;
	private TextView temperatureText;
	private TextView windText;
	private TextView dateText;
	private ListView weatherForecastList;
	private Intent intent;
	private Time time;
	private Runnable run;
	private Builder builder;
	private String currentWeekDay;
	private String city;
	private String currentTemperature;
	private int index = 0; // ��ǰ��������
	String updateTime;
	
	
	
	// ����pm2.5�Լ�����ָ��
	private TextView pm25TextView;
	private String pm25;
	private TextView pm25_desTextView;
	
	private TextView clothes_index_title_TextView;
	private TextView clothes_index_zs_TextView;
	private TextView clothes_index_des_TextView;
	
	private TextView cold_index_title_TextView;
	private TextView cold_index_zs_TextView;
	private TextView cold_index_des_TextView;
	
	private TextView ziwaixian_index_title_TextView;
	private TextView ziwaixian_index_zs_TextView;
	private TextView ziwaixian_index_des_TextView;
	
	private TextView sport_index_title_TextView;
	private TextView sport_index_zs_TextView;
	private TextView sport_index_des_TextView;
	
	private TextView car_index_title_TextView;
	private TextView car_index_zs_TextView;
	private TextView car_index_des_TextView;
	
	private TextView traval_index_title_TextView;
	private TextView traval_index_zs_TextView;
	private TextView traval_index_des_TextView;
	
	
	// ��������ˢ��
	private PullToRefreshScrollView mPullRefreshScrollView;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather);
		Log.d("heyu", "xxx1");
		EngineManager.getInstance().setContext(this.getApplicationContext()).setDB(null);
		init();
		// ��������
		Typeface face = Typeface.createFromAsset(getAssets(),"fonts/HelveticaNeueLTPro-Lt.ttf");
		currentTemperatureText.setTypeface(face);
		setCurrentWeatherLayoutHight();
		Log.d("heyu", "xxx2");
		handler = new MyHandler();
		context = this;
		time = new Time();
		run = new Runnable() {
			@Override
			public void run() {
				refreshing(false);
				Toast.makeText(MainActivity.this, "���糬ʱ,���Ժ�����", Toast.LENGTH_SHORT)
						.show();
			}
		};
		sp = getSharedPreferences("weather", Context.MODE_PRIVATE);
		Log.d("heyu", "xxx3");
		if ("".equals(sp.getString("city", ""))) { // ���û�г�����Ϣ�����ѡ�����
			Log.d("heyu", "xxx4");
			intent = new Intent();
			intent.setClass(MainActivity.this, SelectCityActivity.class);
			intent.putExtra("city", "");
			MainActivity.this.startActivityForResult(intent, 100);
			updateTimeText.setText("�� �� ����");
			weatherBg.setBackgroundResource(R.drawable.bg_na);
			scrollView.setVisibility(View.GONE);
		} else {
			updateData();
			updateWeatherImage();
			updateWeatherInfo();
		}
	
	}
	
	private void init()
	{
		weatherBg = (LinearLayout) findViewById(R.id.weather_bg);
		titleBarLayout = (LinearLayout) findViewById(R.id.title_bar_layout);
		changeCity = (LinearLayout) findViewById(R.id.change_city_layout);
		cityText = (TextView) findViewById(R.id.city);
		share = (ImageView) findViewById(R.id.share);
		about = (ImageView) findViewById(R.id.about);
		refresh = (ImageView) findViewById(R.id.refresh);
		refreshing = (ProgressBar) findViewById(R.id.refreshing);
		updateTimeText = (TextView) findViewById(R.id.update_time);
//		scrollView = (ScrollView) findViewById(R.id.scroll_view);
		currentWeatherLayout = (LinearLayout) findViewById(R.id.current_weather_layout);
		weatherIcon = (ImageView) findViewById(R.id.weather_icon);
		currentTemperatureText = (TextView) findViewById(R.id.current_temperature);
		currentWeatherText = (TextView) findViewById(R.id.current_weather);
		temperatureText = (TextView) findViewById(R.id.temperature);
		windText = (TextView) findViewById(R.id.wind);
		dateText = (TextView) findViewById(R.id.date);
		weatherForecastList = (ListView) findViewById(R.id.weather_forecast_list);
		changeCity.setOnClickListener(new ButtonListener());
		share.setOnClickListener(new ButtonListener());
		about.setOnClickListener(new ButtonListener());
		refresh.setOnClickListener(new ButtonListener());
		
		// 
		pm25TextView = (TextView)findViewById(R.id.pm25);
		pm25_desTextView = (TextView)findViewById(R.id.pm25_des);
		clothes_index_title_TextView = (TextView)findViewById(R.id.clothes_index_title);
		clothes_index_zs_TextView = (TextView)findViewById(R.id.clothes_index_zs);
		clothes_index_des_TextView = (TextView)findViewById(R.id.clothes_index_des);
		cold_index_title_TextView = (TextView)findViewById(R.id.cold_index_title);
		cold_index_zs_TextView = (TextView)findViewById(R.id.cold_index_zs);
		cold_index_des_TextView = (TextView)findViewById(R.id.cold_index_des);
		ziwaixian_index_title_TextView = (TextView)findViewById(R.id.ziwaixian_index_title);
		ziwaixian_index_zs_TextView = (TextView)findViewById(R.id.ziwaixian_index_zs);
		ziwaixian_index_des_TextView = (TextView)findViewById(R.id.ziwaixian_index_des);
		sport_index_title_TextView = (TextView)findViewById(R.id.sport_index_title);
		sport_index_zs_TextView = (TextView)findViewById(R.id.sport_index_zs);
		sport_index_des_TextView = (TextView)findViewById(R.id.sport_index_des);
		car_index_title_TextView = (TextView)findViewById(R.id.car_index_title);
		car_index_zs_TextView = (TextView)findViewById(R.id.car_index_zs);
		car_index_des_TextView = (TextView)findViewById(R.id.car_index_des);
		traval_index_title_TextView = (TextView)findViewById(R.id.traval_index_title);
		traval_index_zs_TextView = (TextView)findViewById(R.id.traval_index_zs);
		traval_index_des_TextView = (TextView)findViewById(R.id.traval_index_des);
		
				
		// ����ˢ��
		 mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshScrollView);  
	     mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel("");   // lastUpdateLabel
	     mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("����ˢ��...");  
	     mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("ˢ����...");  
	     mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("�ɿ�ˢ��");  // releaseLabel
	     //�����������趨  
	     mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);  
	     //������������  
	     mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {  
	              
	            @Override  
	            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {  
	                //ִ��ˢ�º���  
	            	if (Utils.checkNetwork(MainActivity.this) == false) {
						Toast.makeText(MainActivity.this, "�����쳣,������������",
								Toast.LENGTH_SHORT).show();
						mPullRefreshScrollView.onRefreshComplete();
						return;
					}
					updateWeather();
					mPullRefreshScrollView.onRefreshComplete();
	            }  
	        });  
	   // ��ȡScrollView����
	   scrollView = mPullRefreshScrollView.getRefreshableView(); 
	
	}
	
	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_city_layout:
				intent = new Intent();
				intent.setClass(MainActivity.this, SelectCityActivity.class);
				MainActivity.this.startActivityForResult(intent, 100);
				break;
			case R.id.share: 
				String shareInfoString = city+","
										+currentTemperature+","
										+weatherArray[index]+","
										+temperatureArray[index]+","
										+windArray[index]+","
										+updateTime+"���¡�"
										+"-------����iWeather�ͻ��˷���";
				intent = new Intent(Intent.ACTION_SEND);
				intent.setType("image/*");
				intent.putExtra(Intent.EXTRA_SUBJECT, "���ѷ���");
				intent.putExtra(Intent.EXTRA_TEXT, shareInfoString);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MainActivity.this.startActivity(Intent.createChooser(intent, "���ѷ���"));
				break;
			case R.id.about: 
				LayoutInflater inflater = getLayoutInflater();
				View dialogLayout = inflater.inflate(R.layout.weather_dialog,
						(ViewGroup) findViewById(R.layout.weather_dialog));
				TextView version = (TextView) dialogLayout.findViewById(R.id.version);
				version.setText("V " + Utils.getVersion(MainActivity.this));
				builder = new Builder(MainActivity.this);
				builder.setTitle("����");
				builder.setView(dialogLayout);
				builder.setPositiveButton("ȷ��", null);
				builder.setCancelable(false);
				builder.show();
				break;	
			case R.id.refresh:
				if (Utils.checkNetwork(MainActivity.this) == false) {
					Toast.makeText(MainActivity.this, "�����쳣,������������",
							Toast.LENGTH_SHORT).show();
					return;
				}
				updateWeather();
				break;
			default:
				break;
			}
		}
	}
		
	// ���ò��ֵĸ߶ȣ�������Ļ��
	private void setCurrentWeatherLayoutHight() {
		// ֪ͨ���߶�
		int statusBarHeight = 0;
		try {
			statusBarHeight = getResources().getDimensionPixelSize(
					Integer.parseInt(Class
							.forName("com.android.internal.R$dimen")
							.getField("status_bar_height")
							.get(Class.forName("com.android.internal.R$dimen")
									.newInstance()).toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// ��Ļ�߶�
		int displayHeight = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getHeight();
		// title bar LinearLayout�߶�
		titleBarLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		int titleBarHeight = titleBarLayout.getMeasuredHeight();

		LayoutParams linearParams = (LayoutParams) currentWeatherLayout
				.getLayoutParams();
		linearParams.height = displayHeight - statusBarHeight - titleBarHeight;
		currentWeatherLayout.setLayoutParams(linearParams);
	}
	
	// ˢ��ʱ��ʾ������
	private void refreshing(boolean isRefreshing) {
		if (isRefreshing) {
			refresh.setVisibility(View.GONE);
			refreshing.setVisibility(View.VISIBLE);
		} else {
			refresh.setVisibility(View.VISIBLE);
			refreshing.setVisibility(View.GONE);
		}
	}

	private void updateData() {
		titleArray = new String[6];
		zsArray = new String[6];
		tiptArray = new String[6];
		desArray = new String[6];
		for(int i=0;i<6;i++){ // clothes,car,travel,cold,sport,zi
			titleArray[i] = sp.getString(TITLE_KEY[i], "");
			zsArray[i] = sp.getString(ZS_KEY[i], "");
			tiptArray[i] = sp.getString(TIPT_KEY[i], "");
			desArray[i] = sp.getString(DES_KEY[i], "");
		}		
		dateArray = new String[4];
		weatherArray = new String[4];
		windArray = new String[4];
		temperatureArray = new String[4];
		for (int i = 0; i < 4; i++) {
			dateArray[i] = sp.getString(DATE_KEY[i], "");
			weatherArray[i] = sp.getString(WEATHER_KEY[i], "");
			windArray[i] = sp.getString(WIND_KEY[i], "");
			temperatureArray[i] = sp.getString(TEMPERATURE_KEY[i], "");
		}
		city = sp.getString("city", "");
		pm25 = sp.getString("pm25", "");
		
		currentTemperature = sp.getString("current_temperature", "");
		time.setToNow();
		switch (time.weekDay) {
		case 0:
			currentWeekDay = "����";
			break;
		case 1:
			currentWeekDay = "��һ";
			break;
		case 2:
			currentWeekDay = "�ܶ�";
			break;
		case 3:
			currentWeekDay = "����";
			break;
		case 4:
			currentWeekDay = "����";
			break;
		case 5:
			currentWeekDay = "����";
			break;
		case 6:
			currentWeekDay = "����";
			break;
		default:
			break;
		}
		for (int i = 0; i < 4; i++) {
			if (dateArray[i].equals(currentWeekDay)) {
				index = i;
			}
		}
	}
	
	private void updateWeatherImage() {
		scrollView.setVisibility(View.VISIBLE);
		String currentWeather = weatherArray[index];
		if (currentWeather.contains("ת")) {
			currentWeather = currentWeather.substring(0,
					currentWeather.indexOf("ת"));
		}
		time.setToNow();
		if (currentWeather.contains("��")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherBg.setBackgroundResource(R.drawable.bg_fine_day);
				weatherIcon.setImageResource(R.drawable.weather_img_fine_day);
			} else {
				weatherBg.setBackgroundResource(R.drawable.bg_fine_night);
				weatherIcon.setImageResource(R.drawable.weather_img_fine_night);
			}
		} else if (currentWeather.contains("����")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherBg.setBackgroundResource(R.drawable.bg_cloudy_day);
				weatherIcon.setImageResource(R.drawable.weather_img_cloudy_day);
			} else {
				weatherBg.setBackgroundResource(R.drawable.bg_cloudy_night);
				weatherIcon
						.setImageResource(R.drawable.weather_img_cloudy_night);
			}
		} else if (currentWeather.contains("��")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherBg.setBackgroundResource(R.drawable.bg_overcast_day);
			}
			else{
				weatherBg.setBackgroundResource(R.drawable.bg_overcast_night);
			}
			weatherIcon.setImageResource(R.drawable.weather_img_overcast);
		} else if (currentWeather.contains("��")) {
			weatherBg.setBackgroundResource(R.drawable.bg_thunder_storm);
			weatherIcon.setImageResource(R.drawable.weather_img_thunder_storm);
		} else if (currentWeather.contains("��")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherBg.setBackgroundResource(R.drawable.bg_rain_day);
			}
			else{
				weatherBg.setBackgroundResource(R.drawable.bg_rain_night);
			}
			
			if (currentWeather.contains("С��")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_small);
			} else if (currentWeather.contains("����")) {
				weatherIcon
						.setImageResource(R.drawable.weather_img_rain_middle);
			} else if (currentWeather.contains("����")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_big);
			} else if (currentWeather.contains("����")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_storm);
			} else if (currentWeather.contains("���ѩ")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_snow);
			} else if (currentWeather.contains("����")) {
				weatherIcon.setImageResource(R.drawable.weather_img_sleet);
			} else {
				weatherIcon
						.setImageResource(R.drawable.weather_img_rain_middle);
			}
		} else if (currentWeather.contains("ѩ")|| currentWeather.contains("����")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherBg.setBackgroundResource(R.drawable.bg_snow_day);
			}
			else{
				weatherBg.setBackgroundResource(R.drawable.bg_snow_night);
			}
			
			if (currentWeather.contains("Сѩ")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_small);
			} else if (currentWeather.contains("��ѩ")) {
				weatherIcon
						.setImageResource(R.drawable.weather_img_snow_middle);
			} else if (currentWeather.contains("��ѩ")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_big);
			} else if (currentWeather.contains("��ѩ")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_storm);
			} else if (currentWeather.contains("����")) {
				weatherIcon.setImageResource(R.drawable.weather_img_hail);
			} else {
				weatherIcon
						.setImageResource(R.drawable.weather_img_snow_middle);
			}
		} else if (currentWeather.contains("��")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherBg.setBackgroundResource(R.drawable.bg_fog_day);
			}
			else{
				weatherBg.setBackgroundResource(R.drawable.bg_fog_night);
			}
			weatherIcon.setImageResource(R.drawable.weather_img_fog);
		} else if (currentWeather.contains("��")) {
			weatherBg.setBackgroundResource(R.drawable.bg_haze);
			weatherIcon.setImageResource(R.drawable.weather_img_fog);
		} else if (currentWeather.contains("ɳ����")
				|| currentWeather.contains("����")
				|| currentWeather.contains("��ɳ")) {
			weatherBg.setBackgroundResource(R.drawable.bg_sand_storm);
			weatherIcon.setImageResource(R.drawable.weather_img_sand_storm);
		} else {
			weatherBg.setBackgroundResource(R.drawable.bg_na);
			weatherIcon.setImageResource(R.drawable.weather_img_fine_day);
		}
	}

	private void updateWeatherInfo() {
		cityText.setText(city);
		pm25TextView.setText(pm25);
		if(!"".equals(pm25))
		{
			/* 0~50��һ�����ţ���ɫ��51~100��������������ɫ�� 
			 * 101~150�������������Ⱦ����ɫ�� 151~200���ļ����ж���Ⱦ ����ɫ�� 
			 * 201~300���弶���ض���Ⱦ ����ɫ�� >300��������������Ⱦ�� �ֺ�ɫ
			 */
			int num = Integer.parseInt(pm25);
			if(num<=50){
				pm25_desTextView.setText("��");
			}
			else if(num<=100){
				pm25_desTextView.setText("��");
			}
			else if(num<=150){
				pm25_desTextView.setText("�����Ⱦ");
			}
			else if(num<=200){
				pm25_desTextView.setText("�ж���Ⱦ");
			}
			else if(num<=300){
				pm25_desTextView.setText("�ض���Ⱦ");
			}
			else{
				pm25_desTextView.setText("������Ⱦ");
			}
			
		}
		else{
			pm25_desTextView.setText("");
		}
		
		// ��������ָ�� 
		clothes_index_title_TextView.setText(tiptArray[0]);
		clothes_index_zs_TextView.setText(zsArray[0]);
		clothes_index_des_TextView.setText(desArray[0]);
		
		cold_index_title_TextView.setText(tiptArray[3]);
		cold_index_zs_TextView.setText(zsArray[3]);
		cold_index_des_TextView.setText(desArray[3]);
		
		ziwaixian_index_title_TextView.setText(tiptArray[5]);
		ziwaixian_index_zs_TextView.setText(zsArray[5]);
		ziwaixian_index_des_TextView.setText(desArray[5]);
		
		sport_index_title_TextView.setText(tiptArray[4]);
		sport_index_zs_TextView.setText(zsArray[4]);
		sport_index_des_TextView.setText(desArray[4]);
		
		car_index_title_TextView.setText(tiptArray[1]);
		car_index_zs_TextView.setText(zsArray[1]);
		car_index_des_TextView.setText(desArray[1]);
		
		traval_index_title_TextView.setText(tiptArray[2]);
		traval_index_zs_TextView.setText(zsArray[2]);
		traval_index_des_TextView.setText(desArray[2]);
		
		currentTemperatureText.setText(currentTemperature);
		currentWeatherText.setText(weatherArray[index]);
		temperatureText.setText(temperatureArray[index]);
		windText.setText(windArray[index]);
		Time time = new Time();
		time.setToNow();
		String date = new SimpleDateFormat("MM/dd").format(new Date());
		dateText.setText(currentWeekDay + " " + date);
		updateTime = sp.getString("update_time", "");
		if (Integer.parseInt(updateTime.substring(0, 4)) == time.year
				&& Integer.parseInt(updateTime.substring(5, 7)) == time.month + 1
				&& Integer.parseInt(updateTime.substring(8, 10)) == time.monthDay) {
			updateTime = "����" + updateTime.substring(updateTime.indexOf(" "));
			updateTimeText.setTextColor(getResources().getColor(R.color.daily_white));
		} else {
			updateTime = updateTime.substring(5).replace("-", "��")
					.replace(" ", "�� ");
			updateTimeText.setTextColor(getResources().getColor(R.color.red));
			// ����һ��û�и����������Զ����û�����
			if (Utils.checkNetwork(MainActivity.this) == true) {
				updateWeather();
			}
		}
		updateTimeText.setText(updateTime + " ����");
		weatherForecastList.setAdapter(new MyAdapter(MainActivity.this));
		Utils.setListViewHeightBasedOnChildren(weatherForecastList);
	}
	
	// ��������
	private void updateWeather() {
		refreshing(true);
		handler.postDelayed(run, 60 * 1000);
		EngineManager.getInstance().getWebPageMannger().getWebPage(UpdateWeather.class).setHtmlValue("city", city);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				EngineManager.getInstance().getWebPageMannger().updateWebPage(UpdateWeather.class, true);
			}
		});
		thread.start();
	}
	
	
	class MyAdapter extends BaseAdapter {

		private Context mContext;

		private MyAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return getData().size();
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
						R.layout.weather_forecast_item, null);
				holder = new ViewHolder();
				holder.date = (TextView) convertView.findViewById(R.id.weather_forecast_date);
				holder.img = (ImageView) convertView.findViewById(R.id.weather_forecast_img);
				holder.weather = (TextView) convertView.findViewById(R.id.weather_forecast_weather);
				holder.temperature = (TextView) convertView.findViewById(R.id.weather_forecast_temperature);
				holder.wind = (TextView) convertView.findViewById(R.id.weather_forecast_wind);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Typeface face = Typeface.createFromAsset(getAssets(),
					"fonts/fangzhenglantingxianhe_GBK.ttf");
			holder.date.setText(getData().get(position).get("date").toString());
			holder.img.setImageResource((Integer) getData().get(position).get("img"));
			holder.weather.setText(getData().get(position).get("weather").toString());
			holder.temperature.setText(getData().get(position).get("temperature").toString());
			holder.temperature.setTypeface(face);
			holder.wind.setText(getData().get(position).get("wind").toString());
			return convertView;
		}

	}

	class ViewHolder {
		TextView date;
		ImageView img;
		TextView weather;
		TextView temperature;
		TextView wind;
	}
	
	// �õ�������Ϣ
	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 4; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (dateArray[i].equals(currentWeekDay)) {
				map.put("date", "����");
			} else {
				map.put("date", dateArray[i]);
			}
			map.put("img", getWeatherImg(weatherArray[i]));
			map.put("weather", weatherArray[i]);
			map.put("temperature", temperatureArray[i]);
			map.put("wind", windArray[i]);
			list.add(map);
		}
		return list;
	}
	
	// ����������Ӧ��ͼƬ
	private int getWeatherImg(String weather) {
		int img = 0;
		if (weather.contains("ת")) {
			weather = weather.substring(0, weather.indexOf("ת"));
		}
		if (weather.contains("��")) {
			img = R.drawable.weather_icon_fine;
		} else if (weather.contains("����")) {
			img = R.drawable.weather_icon_cloudy;
		} else if (weather.contains("��")) {
			img = R.drawable.weather_icon_overcast;
		} else if (weather.contains("��")) {
			img = R.drawable.weather_icon_thunder_storm;
		} else if (weather.contains("С��")) {
			img = R.drawable.weather_icon_rain_small;
		} else if (weather.contains("����")) {
			img = R.drawable.weather_icon_rain_middle;
		} else if (weather.contains("����")) {
			img = R.drawable.weather_icon_rain_big;
		} else if (weather.contains("����")) {
			img = R.drawable.weather_icon_rain_storm;
		} else if (weather.contains("���ѩ")) {
			img = R.drawable.weather_icon_rain_snow;
		} else if (weather.contains("����")) {
			img = R.drawable.weather_icon_sleet;
		} else if (weather.contains("Сѩ")) {
			img = R.drawable.weather_icon_snow_small;
		} else if (weather.contains("��ѩ")) {
			img = R.drawable.weather_icon_snow_middle;
		} else if (weather.contains("��ѩ")) {
			img = R.drawable.weather_icon_snow_big;
		} else if (weather.contains("��ѩ")) {
			img = R.drawable.weather_icon_snow_storm;
		} else if (weather.contains("����")) {
			img = R.drawable.weather_icon_hail;
		} else if (weather.contains("��") || weather.contains("��")) {
			img = R.drawable.weather_icon_fog;
		} else if (weather.contains("ɳ����") || weather.contains("����")
				|| weather.contains("��ɳ")) {
			img = R.drawable.weather_icon_sand_storm;
		} else {
			img = R.drawable.weather_icon_fine;
		}
		return img;
	}

	// 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1 && !data.getStringExtra("city").equals(city)) {
			city = data.getStringExtra("city");
			cityText.setText(city);
			updateTimeText.setText("�� �� ����");
			weatherBg.setBackgroundResource(R.drawable.bg_na);
			scrollView.setVisibility(View.GONE);
			if (Utils.checkNetwork(MainActivity.this) == false) {
				Toast.makeText(MainActivity.this, "�����쳣,������������", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			updateWeather();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// handler�첽��Ϣ����
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			refreshing(false);
			switch (msg.what) {
			case 1: // �����������������
				Log.d("heyu", "parsedata");
				handler.removeCallbacks(run);
				Bundle bundle = msg.getData();
				dateArray = bundle.getStringArray("date");
				weatherArray = bundle.getStringArray("weather");
				windArray = bundle.getStringArray("wind");
				temperatureArray = bundle.getStringArray("temperature");
				city = bundle.getString("city");
				currentTemperature = bundle.getString("current_temperature");
				pm25 = bundle.getString("pm25");
				Log.d("heyu", "parsetitle");
				titleArray = bundle.getStringArray("title");
				zsArray = bundle.getStringArray("zs");
				tiptArray = bundle.getStringArray("tipt");
				desArray = bundle.getStringArray("des");
				Log.d("heyu", "parsedes");
				saveData();
				Log.d("heyu", "savedata");
				updateData();
				Log.d("heyu", "updatedata");
				updateWeatherImage();
				Log.d("heyu", "updateweatherImage");
				updateWeatherInfo();
				Log.d("heyu", "updataweatherinfo");
				break;
			case 2: // ����ʧ��
				Log.d("heyu", "parseerror");
				builder = new Builder(MainActivity.this);
				builder.setTitle("��ʾ");
				builder.setMessage("û�в�ѯ��[" + city + "]��������Ϣ��");
				builder.setPositiveButton("����",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								intent = new Intent();
								intent.setClass(MainActivity.this, SelectCityActivity.class);
								MainActivity.this.startActivityForResult(intent, 100);
							}
						});
				builder.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
								finish();
							}
						});
				builder.setCancelable(false);
				builder.show();
				break;
			case 0: // δ֪����
				Toast.makeText(MainActivity.this, "����ʧ��,���Ժ�����", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	private void saveData() {
		Log.d("heyu", "enter_savedata");
		String updateTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Time time = new Time();
		time.setToNow();
		String hour, minute;
		hour = time.hour + "";
		minute = time.minute + "";
		if (hour.length() < 2) {
			hour = "0" + hour;
		}
		if (minute.length() < 2) {
			minute = "0" + minute;
		}
		updateTime = updateTime + " " + hour + ":" + minute;
		Editor editor = sp.edit();
		editor.putString("update_time", updateTime);
		for (int i = 0; i < 4; i++) {
			editor.putString(DATE_KEY[i], dateArray[i]);
			editor.putString(WEATHER_KEY[i], weatherArray[i]);
			editor.putString(WIND_KEY[i], windArray[i]);
			editor.putString(TEMPERATURE_KEY[i], temperatureArray[i]);
		}
		editor.putString("city", city);
		editor.putString("current_temperature", currentTemperature);
		editor.putString("pm25", pm25);
		Log.d("heyu", "editor");
		for(int i=0;i<6;i++){
			Log.d("heyu", "editor"+i);
			editor.putString(TITLE_KEY[i], titleArray[i]);
			editor.putString(ZS_KEY[i], zsArray[i]);
			editor.putString(TIPT_KEY[i], tiptArray[i]);
			editor.putString(DES_KEY[i], desArray[i]);
			Log.d("heyu", "editor"+i+"over");
		}
		Log.d("heyu", "putString");
		editor.commit();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
		}
		return false;
	}

	
	 //  ˫���˳�����
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer timer = null;
		if (isExit == false) {
			isExit = true;
			Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}
	
}
