package com.example.apk;

import java.util.ArrayList;

import overlayutil.OverlayManager;
import overlayutil.WalkingRouteOverlay;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecondB extends Activity implements OnMapClickListener,OnGetRoutePlanResultListener
{
	MapView mMapView = null;    // 地图View
    BaiduMap mBaidumap = null;
    RouteLine route = null;
   // Button mBtnPre = null;//上一个节点
   // Button mBtnNext = null;//下一个节点
    private int buaaflag=0;
    private int i=100;
    String[] mid;
    
    //事件 
    /*
    RoutePlanSearch mSearch = null;
    RoutePlanSearch mSearch1 = null;
    RoutePlanSearch mSearch2 = null;
    */
    
    RoutePlanSearch [] mSearchs = new RoutePlanSearch[20];      //一堆事件
    
    
    boolean useDefaultIcon = false;
    OverlayManager routeOverlay = null;
    int nodeIndex = -1;//节点索引,供浏览节点时使用
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	setContentView(R.layout.second);
	CharSequence titleLable = "路线规划功能";
    setTitle(titleLable);
    //初始化地图
    mMapView = (MapView) findViewById(R.id.map);
    mBaidumap = mMapView.getMap();
   // mBtnPre = (Button) findViewById(R.id.pre);
   // mBtnNext = (Button) findViewById(R.id.next);
 ///   mBtnPre.setVisibility(View.INVISIBLE);
  //  mBtnNext.setVisibility(View.INVISIBLE);
    //地图点击事件处理
    mBaidumap.setOnMapClickListener(this);
    
    
    // 初始化搜索模块，注册事件监听
    /*
    mSearch = RoutePlanSearch.newInstance();
    mSearch.setOnGetRoutePlanResultListener(this);
    mSearch1 = RoutePlanSearch.newInstance();
    mSearch1.setOnGetRoutePlanResultListener(this);
    mSearch2 = RoutePlanSearch.newInstance();
    mSearch2.setOnGetRoutePlanResultListener(this);
    */
    
    //为一堆事件注册监听
    for(int i = 0; i < mSearchs.length; i++){
        mSearchs[i] = RoutePlanSearch.newInstance();
        mSearchs[i].setOnGetRoutePlanResultListener(this);
    }
        
   }
public void SearchButtonProcess(View v) {
    //重置浏览节点的路线数据
	
    route = null;
  //  mBtnPre.setVisibility(View.INVISIBLE);
  //  mBtnNext.setVisibility(View.INVISIBLE);
    mBaidumap.clear();
    // 处理搜索按钮响应
    EditText editSt = (EditText) findViewById(R.id.start);
    EditText editMid=(EditText) findViewById(R.id.mid);
    EditText editEn = (EditText) findViewById(R.id.end);
    
    
    //设置起终点信息，对于tranist search 来说，城市名无意义
    PlanNode stNode = PlanNode.withCityNameAndPlaceName("北京", editSt.getText().toString());
    PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", editEn.getText().toString());
    if(editMid.getText().toString().isEmpty()){
    	if (v.getId() == R.id.walk) {
            /*
                mSearch.walkingSearch((new WalkingRoutePlanOption())
                        .from(stNode)
                        .to(midNodes1));
                mSearch1.walkingSearch((new WalkingRoutePlanOption())
                        .from(midNodes1)
                        .to(midNodes2));
                mSearch2.walkingSearch((new WalkingRoutePlanOption())
                        .from(midNodes2)
                        .to(enNode));
                        */
                        
              //  int length = midNodeNames.length;
             //   PlanNode last = null;
             //   for(int i = 0; i < length; i++){
              //      PlanNode begin = (i == 0) ? stNode : last;
                
              //      mSearchs[i].walkingSearch((new WalkingRoutePlanOption()).from(begin).to(midNodes[i]));
              //      last = midNodes[i];
              //  }
                
                mSearchs[0].walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
                
                return ;
            }
    }
    else{
    
    
     try{   String[] midNodeNames = editMid.getText().toString().split("，");//中文逗号
//    ArrayList<PlanNode> midNodes = new ArrayList<PlanNode>();         //节点
      PlanNode [] midNodes = new PlanNode[midNodeNames.length];
    /*
    PlanNode midNodes1=PlanNode.withCityNameAndPlaceName("北京", midNodeArray[0]);
    PlanNode midNodes2=PlanNode.withCityNameAndPlaceName("北京", midNodeArray[1]);
    */
    
    for(int i = 0; i < midNodeNames.length; i++){
        midNodes[i] = PlanNode.withCityNameAndPlaceName("北京", midNodeNames[i]);
    }
   // PlanNode enNode = PlanNode.withCityNameAndPlaceName("北京", editEn.getText().toString());
    if (v.getId() == R.id.walk) {
        /*
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode)
                    .to(midNodes1));
            mSearch1.walkingSearch((new WalkingRoutePlanOption())
                    .from(midNodes1)
                    .to(midNodes2));
            mSearch2.walkingSearch((new WalkingRoutePlanOption())
                    .from(midNodes2)
                    .to(enNode));
                    */
                    
            int length = midNodeNames.length;
            PlanNode last = null;
            for(int i = 0; i < length; i++){
                PlanNode begin = (i == 0) ? stNode : last;
            
                mSearchs[i].walkingSearch((new WalkingRoutePlanOption()).from(begin).to(midNodes[i]));
                last = midNodes[i];
            }
            
            mSearchs[length].walkingSearch((new WalkingRoutePlanOption()).from(last).to(enNode));
            
            return ;
        }
     }catch(Exception e){
    	 Toast.makeText(SecondB.this, "抱歉，输入错误", Toast.LENGTH_SHORT).show();
     }
    }
    
   /*  PlanNode t1 = PlanNode.withLocation(new LatLng(116.360252,39.985745));
     PlanNode t2 = PlanNode.withLocation(new LatLng(116.353498,39.987586));
     PlanNode t3 = PlanNode.withCityNameAndPlaceName("北京","北京航空航天大学-学生公寓7栋");
     PlanNode t4 = PlanNode.withCityNameAndPlaceName("北京", "北京航空航天大学-第二食堂");
     PlanNode t5 = PlanNode.withCityNameAndPlaceName("北京", "北航艺术馆");*/
     
     
    // 实际使用中请对起点终点城市进行正确的设定
  //  if (v.getId() == R.id.walk) {
    /*
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(stNode)
                .to(midNodes1));
        mSearch1.walkingSearch((new WalkingRoutePlanOption())
                .from(midNodes1)
                .to(midNodes2));
        mSearch2.walkingSearch((new WalkingRoutePlanOption())
                .from(midNodes2)
                .to(enNode));
                */
                
   //     int length = midNodeNames.length;
  //      PlanNode last = null;
   //     for(int i = 0; i < length; i++){
    //        PlanNode begin = (i == 0) ? stNode : last;
        
    //        mSearchs[i].walkingSearch((new WalkingRoutePlanOption()).from(begin).to(midNodes[i]));
    //        last = midNodes[i];
     //   }
        
     //   mSearchs[length].walkingSearch((new WalkingRoutePlanOption()).from(last).to(enNode));
        
       // return ;
   // }
    
    
 }
@Override
public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void onGetTransitRouteResult(TransitRouteResult arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void onGetWalkingRouteResult(WalkingRouteResult result) {
	// TODO Auto-generated method stub
	if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
        Toast.makeText(SecondB.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
    }
    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
        //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
        //result.getSuggestAddrInfo()
        return;
    }
    if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        nodeIndex = -1;
     //   mBtnPre.setVisibility(View.VISIBLE);
     //   mBtnNext.setVisibility(View.VISIBLE);
        route = result.getRouteLines().get(0);
        WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
        mBaidumap.setOnMarkerClickListener(overlay);
        routeOverlay = overlay;
        overlay.setData(result.getRouteLines().get(0));
        overlay.addToMap();
        overlay.zoomToSpan();
    }
}
@Override
public void onMapClick(LatLng point) {
	// TODO Auto-generated method stub
	mBaidumap.hideInfoWindow();
	
}
@Override
public boolean onMapPoiClick(MapPoi poi) {
	// TODO Auto-generated method stub
	return false;
}
private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

    public MyWalkingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public BitmapDescriptor getStartMarker() {
        if (useDefaultIcon) {
        		return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
          
        	
        }
        return null;
    }

    @Override
    public BitmapDescriptor getTerminalMarker() {
        if (useDefaultIcon) {
       
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
        return null;
    }
}
@Override
protected void onPause() {
    mMapView.onPause();
    super.onPause();
}

@Override
protected void onResume() {
    mMapView.onResume();
    super.onResume();
}

@Override
protected void onDestroy() {
//    mSearch.destroy();
	for(int i = 0; i < mSearchs.length; i++){
		mSearchs[i].destroy();
	}
    mMapView.onDestroy();
    super.onDestroy();
}
}
	/*CharSequence titleLable = "路线规划功能";
    setTitle(titleLable);
    //初始化地图
    mMapView = (MapView) findViewById(R.id.map);
    mBaidumap = mMapView.getMap();
    mBtnPre = (Button) findViewById(R.id.pre);
    mBtnNext = (Button) findViewById(R.id.next);
    mBtnPre.setVisibility(View.INVISIBLE);
    mBtnNext.setVisibility(View.INVISIBLE);
    //地图点击事件处理
    mBaidumap.setOnMapClickListener(this);
    // 初始化搜索模块，注册事件监听
    mSearch = RoutePlanSearch.newInstance();
    mSearch.setOnGetRoutePlanResultListener(this);
   // mSearch1[i]=RoutePlanSearch.newInstance();
   // mSearch1[i].setOnGetRoutePlanResultListener(this);*/  
