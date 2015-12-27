package com.heyu.utils;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.weixvn.wae.webpage.WebPage;

import com.heyu.activity.MainActivity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class UpdateWeather extends WebPage {

	@Override
	public void onStart() {
		String city = getHtmlValue("city");
		final String ak = "W69oaDTCfuGwzNwmtVvgWfGH"; // 百度AK
		this.uri = "http://api.map.baidu.com/telematics/v3/weather";
		this.type = RequestType.GET;
		// 百度天气API参数设置
		this.params = getParams();
		params.put("location", city);
		params.put("ak", ak);
	}

	@Override
	public void onSuccess(Document doc) {
		analyze(doc);
	}

	// 解析得到的数据：（并通过handler异步到主线程处理）
	private void analyze(Document doc) {
		Message msg = MainActivity.handler.obtainMessage();
		String status = doc.getElementsByTag("status").get(0).text();
		if ("success".equals(status)) { // 查询成功
			msg.what = 1;
			String city = doc.getElementsByTag("currentcity").get(0).text();
			String pm25 = doc.getElementsByTag("pm25").get(0).text();
			System.out.println("pm25"+pm25);
			Log.d("heyu", "getpm25");
			Element indexElement = doc.getElementsByTag("index").get(0); // 取出指数信息
			Log.d("heyu", "getindex");
			Elements titleElements = indexElement.getElementsByTag("title");
			Log.d("heyu", "getindextitle");
			Elements zsElements = indexElement.getElementsByTag("zs");
			Log.d("heyu", "getindexzs");
			Elements tiptElements = indexElement.getElementsByTag("tipt");
			Log.d("heyu", "getindextipt");
			Elements desElements = indexElement.getElementsByTag("des");
			Log.d("heyu", "getindexdes");
			String[] titleArray = new String[6];
			String[] zsArray = new String[6];
			String[] tiptArray = new String[6];
			String[] desArray = new String[6];
			for(int i=0;i<6;i++){ // 索引从0开始
				titleArray[i] = titleElements.get(i).text();
				zsArray[i] = zsElements.get(i).text();
				tiptArray[i] = tiptElements.get(i).text();
				desArray[i] = desElements.get(i).text();
			}
			Log.d("heyu", "getindexarray");
			Element weatherDataElem = doc.getElementsByTag("weather_data").get(0); // 取出weather_data
			Elements dateElem = weatherDataElem.getElementsByTag("date");
			Elements weatherElem = weatherDataElem.getElementsByTag("weather");
			Elements windElem = weatherDataElem.getElementsByTag("wind");
			Elements temperatureElem = weatherDataElem.getElementsByTag("temperature");
			String[] dateArray = new String[4];
			String[] weatherArray = new String[4];
			String[] windArray = new String[4];
			String[] temperatureArray = new String[4];
			String currentTemperature = null;
			for (int i = 0; i < 4; i++) {
				String date = dateElem.get(i).text();
				if (i == 0) {
					if (date.contains("实时")) {
						currentTemperature = date.substring(
								date.indexOf("：") + 1, date.indexOf("℃")) + "°";
					}
					date = date.substring(0, 2);
				}
				dateArray[i] = date;
				weatherArray[i] = weatherElem.get(i).text();
				windArray[i] = windElem.get(i).text();
				String temperature = temperatureElem.get(i).text();
				if (temperature.contains("~")) {
					String highTem = temperature.substring(0,
							temperature.indexOf(" "));
					String lowTem = temperature.substring(
							temperature.lastIndexOf(" ") + 1,
							temperature.indexOf("℃"));
					temperature = lowTem + "~" + highTem + "°";
				} else {
					temperature = temperature.replace("℃", "°");
				}
				temperatureArray[i] = temperature;
			}
			if (currentTemperature == null) {
				currentTemperature = temperatureArray[0];
			}
			Bundle bundle = new Bundle();
			bundle.putString("pm25", pm25);
			Log.d("heyu", "putindex_before");
			bundle.putStringArray("title", titleArray);
			bundle.putStringArray("zs", zsArray);
			bundle.putStringArray("tipt", tiptArray);
			bundle.putStringArray("des", desArray);
			Log.d("heyu", "putindex_after");
			bundle.putStringArray("date", dateArray);
			bundle.putStringArray("weather", weatherArray);
			bundle.putStringArray("wind", windArray);
			bundle.putStringArray("temperature", temperatureArray);
			bundle.putString("city", city);
			bundle.putString("current_temperature", currentTemperature);
			msg.setData(bundle);
		} else if ("No result available".equals(status)) {
			// 没有天气信息
			msg.what = 2;
		} else {
			// 其他错误
			msg.what = 0;
		}
		MainActivity.handler.sendMessage(msg);
		Log.d("heyu", "Update_Over");
	}
}

