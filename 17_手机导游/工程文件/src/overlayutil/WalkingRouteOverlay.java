package overlayutil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.WalkingRouteLine;

import java.util.ArrayList;
import java.util.List;

public class WalkingRouteOverlay extends OverlayManager {

    private WalkingRouteLine mRouteLine = null;

    public WalkingRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    public void setData(WalkingRouteLine line) {
        mRouteLine = line;
    }

    @Override
    public final List<OverlayOptions> getOverlayOptions() {
        if (mRouteLine == null) {
            return null;
        }

        List<OverlayOptions> overlayList = new ArrayList<OverlayOptions>();
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {
            for (WalkingRouteLine.WalkingStep step : mRouteLine.getAllStep()) {
                Bundle b = new Bundle();
                b.putInt("index", mRouteLine.getAllStep().indexOf(step));
                if (step.getEntrance() != null) {
                    overlayList.add((new MarkerOptions())
                            .position(step.getEntrance().getLocation())
                                    .rotate((360 - step.getDirection()))
                                            .zIndex(10)
                                                    .anchor(0.5f, 0.5f)
                                                            .extraInfo(b)
                                                                    .icon(BitmapDescriptorFactory
                                                                            .fromAssetWithDpi("Icon_line_node.png")));
                }

    
                if (mRouteLine.getAllStep().indexOf(step) == (mRouteLine
                        .getAllStep().size() - 1) && step.getExit() != null) {
                    overlayList.add((new MarkerOptions())
                            .position(step.getExit().getLocation())
                                    .anchor(0.5f, 0.5f)
                                            .zIndex(10)
                                                    .icon(BitmapDescriptorFactory
                                                            .fromAssetWithDpi("Icon_line_node.png")));

                }
            }
        }
        // starting
        if (mRouteLine.getStarting() != null) {
            overlayList.add((new MarkerOptions())
                    .position(mRouteLine.getStarting().getLocation())
                            .icon(getStartMarker() != null ? getStartMarker() :
                                    BitmapDescriptorFactory
                                            .fromAssetWithDpi("Icon_start.png")).zIndex(10));
        }
        // terminal
        if (mRouteLine.getTerminal() != null) {
            overlayList
                    .add((new MarkerOptions())
                            .position(mRouteLine.getTerminal().getLocation())
                                    .icon(getTerminalMarker() != null ? getTerminalMarker() :
                                            BitmapDescriptorFactory
                                                    .fromAssetWithDpi("Icon_end.png"))
                                                            .zIndex(10));
        }

   
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {
            LatLng lastStepLastPoint = null;
            for (WalkingRouteLine.WalkingStep step : mRouteLine.getAllStep()) {
                List<LatLng> watPoints = step.getWayPoints();
                if (watPoints != null) {
                    List<LatLng> points = new ArrayList<LatLng>();
                    if (lastStepLastPoint != null) {
                        points.add(lastStepLastPoint);
                    }
                    points.addAll(watPoints);
                    overlayList.add(new PolylineOptions().points(points).width(10)
                            .color(getLineColor() != 0 ? getLineColor() : Color.argb(178, 0, 78, 255)).zIndex(0));
                    lastStepLastPoint = watPoints.get(watPoints.size() - 1);
                }
            }
            
        }

        return overlayList;
    }

    public BitmapDescriptor getStartMarker() {
        return null;
    }
    public int getLineColor() {
        return 0;
    }

    public BitmapDescriptor getTerminalMarker() {
        return null;
    }

    public boolean onRouteNodeClick(int i) {
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().get(i) != null) {
            Log.i("baidumapsdk", "WalkingRouteOverlay onRouteNodeClick");
        }
        return false;
    }

    @Override
    public final boolean onMarkerClick(Marker marker) {
        for (Overlay mMarker : mOverlayList) {
            if (mMarker instanceof Marker && mMarker.equals(marker)) {
                if (marker.getExtraInfo() != null) {
                    onRouteNodeClick(marker.getExtraInfo().getInt("index"));
                }
            }
        }
        return true;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {
        // TODO Auto-generated method stub
        return false;
    }
}
