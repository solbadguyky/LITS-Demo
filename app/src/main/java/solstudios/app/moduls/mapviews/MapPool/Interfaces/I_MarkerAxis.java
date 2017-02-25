package solstudios.app.moduls.mapviews.MapPool.Interfaces;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import solstudios.app.moduls.mapviews.MapData.MapItem;

/**
 * I_MarkerAxis dùng để điều khiển một MapView
 * <p>
 * Mỗi I_MarkerAxis chỉ kết nối đến duy nhất một Lat/Ln hoặc Marker Option
 */
public interface I_MarkerAxis extends I_Poolable {

    /**
     * Mỗi I_Marker Axis được gọi ra bắt buộc phải gắn vào một MapView
     *
     * @param markerOption
     */
    void setMarkerOption(MarkerOptions markerOption);

    void setMarkerLocation(LatLng latLng);

    void setLocationWithLayout(float x, float y);

    void setLocation(float x, float y);

    void setMapItem(MapItem mapItem);
}
