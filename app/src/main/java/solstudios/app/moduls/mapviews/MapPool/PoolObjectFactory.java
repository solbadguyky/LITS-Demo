package solstudios.app.moduls.mapviews.MapPool;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.Stack;

import solstudios.app.moduls.anchorpoint.AbsMapView;
import solstudios.app.moduls.anchorpoint.MarkerAxisView;
import solstudios.app.moduls.anchorpoint.StatusView;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_PoolObjectFactory;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_Poolable;

/**
 * PoolObjectFactory dùng để tạo ra các MapView
 * Nếu một Mapview đang được sử dụng, nó sẽ nằm trong object pool
 * Khi không còn sử dụng nữa, nó sẽ được đưa vào objectRecycleList đợi tái sử dụng
 * <p>
 * Có 2 danh sách được factory lưu trữ, một danh sách luôn chứa các I_Poolable đã được khởi tạo
 */
public class PoolObjectFactory implements I_PoolObjectFactory {
    private Context mContext;
    private Stack<I_Poolable> objectPoolList ///Những object nào đã được khởi tạo thì sẽ nằm trong bộ counter vĩnh viễn
            ///tránh trường hợp không kiểm soát được số lượng view tạo ra
            , objectRecycleList ///Những object nào có thể tái chế thì sẽ được đưa vào đây, sau khi
            // tái chế sẽ xóa khỏi danh sách chờ tái chế
            ;

    PoolObjectFactory(Context context) {
        this.mContext = context;
        this.objectPoolList = new Stack<>();
        this.objectRecycleList = new Stack<>();
    }


    @Override
    public synchronized I_Poolable createPoolObject(AbsMapView.Class classView) {
        //Logger.d("createPoolObject");
        I_Poolable i_absMapView;

        switch (classView) {
            case Status:
                StatusView statusView = new StatusView(mContext);
                i_absMapView = statusView;
                increaseStack(i_absMapView);
                return statusView;
            case Marker:
                MarkerAxisView markerAxisView = new MarkerAxisView(mContext);
                i_absMapView = markerAxisView;
                increaseStack(i_absMapView);
                return markerAxisView;
        }


        return null;
    }

    @Override
    public synchronized void increaseStack(I_Poolable objectPool) {
        Logger.d(objectPoolList.size());
        if (!objectPoolList.contains(objectPool)) {
            objectPoolList.add(objectPool);
        }

    }

    @Override
    public I_Poolable getFreeObjectFromPool() {
        return null;
    }


    I_Poolable getObjectFromPool() {
        Logger.d("getObjectFromPool|size:" + objectRecycleList.size());
        //Logger.d("getObjectFromPool|static.size:" + objectPoolList.size());
        if (objectRecycleList.size() > 0) {
            return objectRecycleList.pop();
        }
        return null;
    }

    @Override
    public Stack<I_Poolable> getStack() {
        return this.objectPoolList;
    }

    @Override
    public void pushObjectToRecyclePool(I_Poolable iMarkerAxis) {
        if (iMarkerAxis != null) {
            //Logger.d("pushObjectToRecyclePool|i:" + iMarkerAxis);
            iMarkerAxis.finalizePoolObject();
            objectRecycleList.add(iMarkerAxis);
            Logger.d("pushObjectToRecyclePool|size:" + objectRecycleList.size());
        }

    }


    int getCreatedObjectSize() {
        return this.objectPoolList.size();
    }
}
