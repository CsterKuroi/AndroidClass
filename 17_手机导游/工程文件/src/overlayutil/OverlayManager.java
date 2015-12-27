package overlayutil;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnPolylineClickListener;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;

public abstract class OverlayManager implements OnMarkerClickListener, OnPolylineClickListener {

    BaiduMap mBaiduMap = null;
    private List<OverlayOptions> mOverlayOptionList = null;

    List<Overlay> mOverlayList = null;

    public OverlayManager(BaiduMap baiduMap) {
        mBaiduMap = baiduMap;
        // mBaiduMap.setOnMarkerClickListener(this);
        if (mOverlayOptionList == null) {
            mOverlayOptionList = new ArrayList<OverlayOptions>();
        }
        if (mOverlayList == null) {
            mOverlayList = new ArrayList<Overlay>();
        }
    }

    public abstract List<OverlayOptions> getOverlayOptions();

    public final void addToMap() {
        if (mBaiduMap == null) {
            return;
        }

        removeFromMap();
        List<OverlayOptions> overlayOptions = getOverlayOptions();
        if (overlayOptions != null) {
            mOverlayOptionList.addAll(getOverlayOptions());
        }

        for (OverlayOptions option : mOverlayOptionList) {
            mOverlayList.add(mBaiduMap.addOverlay(option));
        }
    }

    public final void removeFromMap() {
        if (mBaiduMap == null) {
            return;
        }
        for (Overlay marker : mOverlayList) {
            marker.remove();
        }
        mOverlayOptionList.clear();
        mOverlayList.clear();

    }


    public void zoomToSpan() {
        if (mBaiduMap == null) {
            return;
        }
        if (mOverlayList.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Overlay overlay : mOverlayList) {
                if (overlay instanceof Marker) {
                    builder.include(((Marker) overlay).getPosition());
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }
    }

}
