package solstudios.app.moduls.mapviews.MapPool;

import android.content.Context;

import com.orhanobut.logger.Logger;

import java.util.EmptyStackException;
import java.util.Stack;

import solstudios.app.moduls.anchorpoint.AbsMapView;
import solstudios.app.moduls.anchorpoint.MarkerAxisView;
import solstudios.app.moduls.anchorpoint.StatusView;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_MarkerAxis;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_PoolObjectFactory;

/**
 * PoolObjectFactory dùng để tạo ra các MapView
 * Nếu một Mapview đang được sử dụng, nó sẽ nằm trong object pool
 * Khi không còn sử dụng nữa, nó sẽ được đưa vào objectRecycleList đợi tái sử dụng
 */
public class PoolObjectFactory implements I_PoolObjectFactory {
    private Context mContext;
    private Stack<I_MarkerAxis> objectPoolList, objectRecycleList;

    PoolObjectFactory(Context context) {
        this.mContext = context;
        this.objectPoolList = new Stack<>();
        this.objectRecycleList = new Stack<>();
    }


    @Override
    public I_MarkerAxis createPoolObject(AbsMapView.Class classView) {
        //Logger.d("createPoolObject");
        I_MarkerAxis i_absMapView;

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
    public void increaseStack(I_MarkerAxis objectPool) {
        Logger.d(objectPoolList.size());
        if (!objectPoolList.contains(objectPool)) {
            objectPoolList.add(objectPool);
        }

    }


    @Override
    public I_MarkerAxis decreaseStack() throws EmptyStackException {
        return objectPoolList.pop();
    }

    @Override
    public void decreaseStack(I_MarkerAxis iMarkerAxis) {
        objectPoolList.remove(iMarkerAxis);
    }

    @Override
    public I_MarkerAxis getObjectFromPool() {
        Logger.d(objectRecycleList.size());
        if (objectRecycleList.size() > 0) {
            return objectRecycleList.pop();
        }
        return null;
    }

    @Override
    public Stack<I_MarkerAxis> getStack() {
        return this.objectPoolList;
    }

    @Override
    public void pushObjectToRecyclePool(I_MarkerAxis iMarkerAxis) {
        objectRecycleList.push(iMarkerAxis);
    }

}
