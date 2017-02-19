package solstudios.app.moduls.mapviews.MapPool.Interfaces;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * I_MarkerAxis kế thừa I_Poolable, dùng trong khởi tạo/recycle Object Pool
 */
public interface I_MarkerAxis extends I_Poolable {
    void setMarkerOption(MarkerOptions markerOption);

    void setLocationWithLayout(float x, float y);

    void setLocation(float x, float y);

    void setLocation(GoogleMap mMap);

    boolean isInitialized();
}
