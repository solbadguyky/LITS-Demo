package solstudios.app.moduls.mapviews.MapPool.Interfaces;

import com.google.android.gms.maps.model.MarkerOptions;

/***
 * Interface dùng để quản lí danh sách các I_Marker được tạo ra
 */
public interface I_ObjectPool<I_MarkerAxis extends I_Poolable> {

    /**
     * Mội I_MarkerAxis được tạo ra sẽ gắn liền với 1 marker tại vị trí hiển thị
     * Nếu như marker đó không còn nằm trong phạm vi hiển thị thì sẽ đưa I_MarkerAxis vào trong
     * Pool để chờ re-cycle
     *
     * @param classView
     * @param markerOptions
     * @return
     */
    I_MarkerAxis adquirePoolObject(solstudios.app.moduls.anchorpoint.AbsMapView.Class classView,
                                   MarkerOptions markerOptions);


    /**
     * Đưa I_MarkerAxis vào trong pool để đợi tái sử dụng / Recycle
     *
     * @param markerOptions
     */
    I_MarkerAxis freePoolObject(MarkerOptions markerOptions);

    I_MarkerAxis findMarerAxis(MarkerOptions markerOptions);

    int getMaxSize();

    int getCurrentSize();
}
