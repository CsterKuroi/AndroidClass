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
	MapView mMapView = null;    // ��ͼView
    BaiduMap mBaidumap = null;
    RouteLine route = null;
   // Button mBtnPre = null;//��һ���ڵ�
   // Button mBtnNext = null;//��һ���ڵ�
    private int buaaflag=0;
    private int i=100;
    String[] mid;
    
    //�¼� 
    /*
    RoutePlanSearch mSearch = null;
    RoutePlanSearch mSearch1 = null;
    RoutePlanSearch mSearch2 = null;
    */
    
    RoutePlanSearch [] mSearchs = new RoutePlanSearch[20];      //һ���¼�
    
    
    boolean useDefaultIcon = false;
    OverlayManager routeOverlay = null;
    int nodeIndex = -1;//�ڵ�����,������ڵ�ʱʹ��
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	setContentView(R.layout.second);
	CharSequence titleLable = "·�߹滮����";
    setTitle(titleLable);
    //��ʼ����ͼ
    mMapView = (MapView) findViewById(R.id.map);
    mBaidumap = mMapView.getMap();
   // mBtnPre = (Button) findViewById(R.id.pre);
   // mBtnNext = (Button) findViewById(R.id.next);
 ///   mBtnPre.setVisibility(View.INVISIBLE);
  //  mBtnNext.setVisibility(View.INVISIBLE);
    //��ͼ����¼�����
    mBaidumap.setOnMapClickListener(this);
    
    
    // ��ʼ������ģ�飬ע���¼�����
    /*
    mSearch = RoutePlanSearch.newInstance();
    mSearch.setOnGetRoutePlanResultListener(this);
    mSearch1 = RoutePlanSearch.newInstance();
    mSearch1.setOnGetRoutePlanResultListener(this);
    mSearch2 = RoutePlanSearch.newInstance();
    mSearch2.setOnGetRoutePlanResultListener(this);
    */
    
    //Ϊһ���¼�ע�����
    for(int i = 0; i < mSearchs.length; i++){
        mSearchs[i] = RoutePlanSearch.newInstance();
        mSearchs[i].setOnGetRoutePlanResultListener(this);
    }
        
   }
public void SearchButtonProcess(View v) {
    //��������ڵ��·������
	
    route = null;
  //  mBtnPre.setVisibility(View.INVISIBLE);
  //  mBtnNext.setVisibility(View.INVISIBLE);
    mBaidumap.clear();
    // ����������ť��Ӧ
    EditText editSt = (EditText) findViewById(R.id.start);
    EditText editMid=(EditText) findViewById(R.id.mid);
    EditText editEn = (EditText) findViewById(R.id.end);
    
    
    //�������յ���Ϣ������tranist search ��˵��������������
    PlanNode stNode = PlanNode.withCityNameAndPlaceName("����", editSt.getText().toString());
    PlanNode enNode = PlanNode.withCityNameAndPlaceName("����", editEn.getText().toString());
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
    
    
     try{   String[] midNodeNames = editMid.getText().toString().split("��");//���Ķ���
//    ArrayList<PlanNode> midNodes = new ArrayList<PlanNode>();         //�ڵ�
      PlanNode [] midNodes = new PlanNode[midNodeNames.length];
    /*
    PlanNode midNodes1=PlanNode.withCityNameAndPlaceName("����", midNodeArray[0]);
    PlanNode midNodes2=PlanNode.withCityNameAndPlaceName("����", midNodeArray[1]);
    */
    
    for(int i = 0; i < midNodeNames.length; i++){
        midNodes[i] = PlanNode.withCityNameAndPlaceName("����", midNodeNames[i]);
    }
   // PlanNode enNode = PlanNode.withCityNameAndPlaceName("����", editEn.getText().toString());
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
    	 Toast.makeText(SecondB.this, "��Ǹ���������", Toast.LENGTH_SHORT).show();
     }
    }
    
   /*  PlanNode t1 = PlanNode.withLocation(new LatLng(116.360252,39.985745));
     PlanNode t2 = PlanNode.withLocation(new LatLng(116.353498,39.987586));
     PlanNode t3 = PlanNode.withCityNameAndPlaceName("����","�������պ����ѧ-ѧ����Ԣ7��");
     PlanNode t4 = PlanNode.withCityNameAndPlaceName("����", "�������պ����ѧ-�ڶ�ʳ��");
     PlanNode t5 = PlanNode.withCityNameAndPlaceName("����", "����������");*/
     
     
    // ʵ��ʹ�����������յ���н�����ȷ���趨
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
        Toast.makeText(SecondB.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
    }
    if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
        //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
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
	/*CharSequence titleLable = "·�߹滮����";
    setTitle(titleLable);
    //��ʼ����ͼ
    mMapView = (MapView) findViewById(R.id.map);
    mBaidumap = mMapView.getMap();
    mBtnPre = (Button) findViewById(R.id.pre);
    mBtnNext = (Button) findViewById(R.id.next);
    mBtnPre.setVisibility(View.INVISIBLE);
    mBtnNext.setVisibility(View.INVISIBLE);
    //��ͼ����¼�����
    mBaidumap.setOnMapClickListener(this);
    // ��ʼ������ģ�飬ע���¼�����
    mSearch = RoutePlanSearch.newInstance();
    mSearch.setOnGetRoutePlanResultListener(this);
   // mSearch1[i]=RoutePlanSearch.newInstance();
   // mSearch1[i].setOnGetRoutePlanResultListener(this);*/  
