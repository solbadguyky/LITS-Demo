package solstudios.app.moduls.mapviews.MapPool.Interfaces;

import com.google.android.gms.maps.model.MarkerOptions;

/***
 * Interface dùng để quản lí danh sách các I_Marker được tạo ra
 */
public interface I_ObjectPool<I_Poolable> {

    /**
     * Lấy một ObjectPool hoặc khởi tạo một object pool
     *
     * @param classView
     * @return
     */
    I_Poolable adquirePoolObject(solstudios.app.moduls.anchorpoint.AbsMapView.Class classView);


    /**
     * Kiểm tra trong pool có object nào bị "bỏ quên" hay không, nếu có sẽ đưa vào hồ
     */
    void freePoolObject(MarkerOptions markerOptions, I_Poolable i_poolable);

    /**
     * Kiểm tra xem marker options đã được set hay chưa, nếu chưa thì sẽ đưa vào tính toán
     *
     * @param markerOptions
     * @return
     */
    boolean findMarkerOptionHasBeenSet(MarkerOptions markerOptions);

    /**
     * Khi mộit marker options được gán vào một I_Poolable, I_poolable sẽ chuyển thành I_MarkerAxis
     * kể từ đây, công việc tính toán của I_MarkerAxis sẽ hoàn toàn dựa vào Marker Options này
     *
     * @param markerObject
     * @param i_poolable
     */
    void setMarkerObject(MarkerOptions markerObject, I_Poolable i_poolable);
}
