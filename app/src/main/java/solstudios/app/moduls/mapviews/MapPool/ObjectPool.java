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

/**
 * Object pool là nơi điều phối các MapView, nếu một Marker yêu cầu một MapView tại vị trí của nó
 * Object Pool sẽ lấy một mapview có sẵn trong pool, hoặc khởi tạo một MapView cho đến khi đạt đủ
 * số lượng tối đa
 */
public class ObjectPool implements I_ObjectPool<I_MarkerAxis> {

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

    public synchronized I_MarkerAxis adquirePoolObject(AbsMapView.Class classView, MarkerOptions markerOptions) {
        Logger.d(this.currentPoolObjects.size());
        I_MarkerAxis poolObject = null;

        ///Nếu trong pool chưa có object nào thì khởi tạo
        if (this.currentPoolObjects.size() == 0) {
            poolObject = this.poolObjectFactory.createPoolObject(classView);
        } else {
            ///Nếu đã có object thì kiểm tra xem có bao nhiêu object đã được tạo ra, nếu
            /// số lượng object vượt quá số object tối đa trên màn hình thì
            /// sẽ recycle lại object cũ
            if (this.currentPoolObjects.size() >= this.maxSize) {

                ///Không khởi tạo nữa mà tiến hành recycle view cũ
                //Toast.makeText(this.mContext, "Khong the tao them MapView|Limit reached:" + this.maxSize, Toast.LENGTH_SHORT).show();
                try {
                    I_MarkerAxis recycledObject = this.poolObjectFactory.getObjectFromPool();
                    this.poolObjectFactory.increaseStack(recycledObject);

                    if (recycledObject != null) {
                        ///Có view có thể tái sử dụng, tiến hành recycling view
                        poolObject = recycledObject;
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

        if (poolObject != null) {
            poolObject.setMarkerOption(markerOptions);
            currentPoolObjects.put(markerOptions, poolObject);
        }

        return poolObject;
    }

    public synchronized I_MarkerAxis freePoolObject(MarkerOptions markerOptions) {
        I_MarkerAxis freeIMarkerAxis = currentPoolObjects.get(markerOptions);
        // currentPoolObjects.remove(markerOptions);
        this.poolObjectFactory.decreaseStack(freeIMarkerAxis);
        this.poolObjectFactory.pushObjectToRecyclePool(freeIMarkerAxis);
        return freeIMarkerAxis;
    }

    @Override
    public I_MarkerAxis findMarerAxis(MarkerOptions markerOptions) {
        return currentPoolObjects.get(markerOptions);
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }

    @Override
    public int getCurrentSize() {
        return this.currentPoolObjects.size();
    }


}