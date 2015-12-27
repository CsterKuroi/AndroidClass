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
import com.baidu.mapapi.search.route.TransitRouteLine;

import java.util.ArrayList;
import java.util.List;

public class TransitRouteOverlay extends OverlayManager {

    private TransitRouteLine mRouteLine = null;

    public TransitRouteOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override
    public final List<OverlayOptions> getOverlayOptions() {

        if (mRouteLine == null) {
            return null;
        }

        List<OverlayOptions> overlayOptionses = new ArrayList<OverlayOptions>();

        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {
            
            for (TransitRouteLine.TransitStep step : mRouteLine.getAllStep()) {
                Bundle b = new Bundle();
                b.putInt("index", mRouteLine.getAllStep().indexOf(step));
                if (step.getEntrance() != null) {
                    overlayOptionses.add((new MarkerOptions())
                            .position(step.getEntrance().getLocation())
                                    .anchor(0.5f, 0.5f).zIndex(10).extraInfo(b)
                                            .icon(getIconForStep(step)));
                }
           
                if (mRouteLine.getAllStep().indexOf(step) == (mRouteLine
                        .getAllStep().size() - 1) && step.getExit() != null) {
                    overlayOptionses.add((new MarkerOptions())
                            .position(step.getExit().getLocation())
                                    .anchor(0.5f, 0.5f).zIndex(10)
                                            .icon(getIconForStep(step)));
                }
            }
        }

        if (mRouteLine.getStarting() != null) {
            overlayOptionses.add((new MarkerOptions())
                    .position(mRouteLine.getStarting().getLocation())
                            .icon(getStartMarker() != null ? getStartMarker() :
                                    BitmapDescriptorFactory
                                            .fromAssetWithDpi("Icon_start.png")).zIndex(10));
        }
        if (mRouteLine.getTerminal() != null) {
            overlayOptionses
                    .add((new MarkerOptions())
                            .position(mRouteLine.getTerminal().getLocation())
                                    .icon(getTerminalMarker() != null ? getTerminalMarker() :
                                            BitmapDescriptorFactory
                                                    .fromAssetWithDpi("Icon_end.png"))
                                                            .zIndex(10));
        }
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().size() > 0) {
            
            for (TransitRouteLine.TransitStep step : mRouteLine.getAllStep()) {
                if (step.getWayPoints() == null) {
                    continue;
                }
                int color = 0;
                if (step.getStepType() != TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING) {
//                    color = Color.argb(178, 0, 78, 255);
                    color = getLineColor() != 0 ? getLineColor() : Color.argb(178, 0, 78, 255);
                } else {
//                    color = Color.argb(178, 88, 208, 0);
                    color = getLineColor() != 0 ? getLineColor() : Color.argb(178, 88, 208, 0);
                }
                overlayOptionses.add(new PolylineOptions()
                        .points(step.getWayPoints()).width(10).color(color)
                        .zIndex(0));
            }
        }
        return overlayOptionses;
    }

    private BitmapDescriptor getIconForStep(TransitRouteLine.TransitStep step) {
        switch (step.getStepType()) {
            case BUSLINE:
                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_bus_station.png");
            case SUBWAY:
                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_subway_station.png");
            case WAKLING:
                return BitmapDescriptorFactory.fromAssetWithDpi("Icon_walk_route.png");
            default:
                return null;
        }
    }

    public void setData(TransitRouteLine routeOverlay) {
        this.mRouteLine = routeOverlay;
    }

    public BitmapDescriptor getStartMarker() {
        return null;
    }

    public BitmapDescriptor getTerminalMarker() {
        return null;
    }

    public int getLineColor() {
        return 0;
    }

    public boolean onRouteNodeClick(int i) {
        if (mRouteLine.getAllStep() != null
                && mRouteLine.getAllStep().get(i) != null) {
            Log.i("baidumapsdk", "TransitRouteOverlay onRouteNodeClick");
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
