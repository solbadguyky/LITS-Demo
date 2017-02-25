package solstudios.app.moduls.mapviews.MapPool;

import android.content.Context;

import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

import solstudios.app.moduls.anchorpoint.AbsMapView;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_MarkerAxis;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_ObjectPool;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_Poolable;

/**
 * Object pool là nơi điều phối các MapView, nếu một Marker yêu cầu một MapView tại vị trí của nó
 * Object Pool sẽ lấy một mapview có sẵn trong pool, hoặc khởi tạo một MapView cho đến khi đạt đủ
 * số lượng tối đa
 */
public class ObjectPool implements I_ObjectPool<I_Poolable> {

    private PoolObjectFactory poolObjectFactory;
    private int maxSize;
    private Map<MarkerOptions, I_MarkerAxis> currentPoolObjects;
    private Context mContext;

    public boolean needRedcycler = true;

    public ObjectPool(Context context, int initSize) {
        this.mContext = context;
        this.maxSize = initSize;
        this.currentPoolObjects = new HashMap<>();
        this.poolObjectFactory = new PoolObjectFactory(context);

    }

    @Override
    public synchronized I_Poolable adquirePoolObject(AbsMapView.Class classView) {
        //Logger.d(this.currentPoolObjects.size());
        I_Poolable poolObject = null;

        ///Nếu trong pool chưa có object nào thì khởi tạo
        if (this.poolObjectFactory.getCreatedObjectSize() == 0) {
            poolObject = this.poolObjectFactory.createPoolObject(classView);
        } else {
            ///Nếu đã có object thì kiểm tra xem có bao nhiêu object đã được tạo ra, nếu
            /// số lượng object vượt quá số object tối đa trên màn hình thì
            /// sẽ recycle lại object cũ
            if (this.poolObjectFactory.getCreatedObjectSize() >= this.maxSize) {

                ///Không khởi tạo nữa mà tiến hành recycle view cũ
                //Toast.makeText(this.mContext, "Khong the tao them MapView|Limit reached:"
                // + this.maxSize, Toast.LENGTH_SHORT).show();
                try {
                    I_Poolable recycledObject = this.poolObjectFactory.getObjectFromPool();

                    if (recycledObject != null) {
                        ///Có view có thể tái sử dụng, tiến hành recycling view
                        poolObject = recycledObject;
                        Logger.d("obtain recycled-object");
                    }

                } catch (EmptyStackException e) {
                    e.printStackTrace();
                    ///Hiện không có view nào trong stack, kiểm tra view hiện có trong pool, nếu còn
                    // thì reset lại stack trong pool

                }
            } else {
                /// Khởi tạo thêm view mới
                poolObject = this.poolObjectFactory.createPoolObject(classView);
            }
        }


        return poolObject;
    }

    @Override
    public void freePoolObject(MarkerOptions markerOptions, I_Poolable i_poolable) {
        if (this.currentPoolObjects.containsKey(markerOptions)) {
            //Logger.d("freePoolObject|markerOptions: " + this.currentPoolObjects.get(markerOptions));
            this.poolObjectFactory.pushObjectToRecyclePool(this.currentPoolObjects.get(markerOptions));

            this.currentPoolObjects.remove(markerOptions);

        }
    }

    @Override
    public boolean findMarkerOptionHasBeenSet(MarkerOptions markerOptions) {
        return currentPoolObjects.get(markerOptions) != null;
    }

    @Override
    public void setMarkerObject(MarkerOptions markerObject, I_Poolable i_poolable) {
        if (i_poolable instanceof I_MarkerAxis) {
            this.currentPoolObjects.put(markerObject, (I_MarkerAxis) i_poolable);
        } else {
            Logger.e("Error while casting to I_MarkerAxis");
        }

    }

    public int getCurrentSize() {
        // Logger.d("getCurrentSize|size: " + this.currentPoolObjects.size());
        return this.currentPoolObjects.size();
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public Map<MarkerOptions, I_MarkerAxis> getCurrentPoolObjects() {
        return this.currentPoolObjects;
    }

}