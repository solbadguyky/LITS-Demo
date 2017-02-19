package solstudios.app.moduls.mapviews.MapPool.Interfaces;

import java.util.Stack;

import solstudios.app.moduls.anchorpoint.AbsMapView;


public interface I_PoolObjectFactory {

    /**
     * Khởi tạo các MapView ở trạng thái cơ bản, lúc này MapView được tạo ra mà chưa có bất
     * cứ một dữ liệu nào
     *
     * @param classView Loại MapView muốn tạo
     * @return MapView ở dạng cơ bản nhất (chưa có dữ liệu)
     */
    I_MarkerAxis createPoolObject(AbsMapView.Class classView);

    /**
     * Nếu một view được tạo ra, thì sẽ thêm nó vào stack, nếu nó được lấy ra từ recyclePool thì
     * vẫn thêm vào danh sách này luôn
     *
     * @param objectPool
     */
    void increaseStack(I_MarkerAxis objectPool);

    /**
     * Xóa một object khỏi stack
     */
    I_MarkerAxis decreaseStack();

    /**
     * Xóa một view xác định khỏi stack
     */
    void decreaseStack(I_MarkerAxis iMarkerAxis);


    /**
     * Kiểm tra trong pool có MapView nào đang đợi tái chế hay không
     *
     * @return view được lấy ra để sử dụng hoặc null nếu recycled-stack không còn view nào
     */
    I_MarkerAxis getObjectFromPool();

    Stack<I_MarkerAxis> getStack();

    /**
     * Đưa một I_MarkerAxis vào reycle-pool để đợi tái sử dụng
     *
     * @param iMarkerAxis cần được đưa vào pool
     */
    void pushObjectToRecyclePool(I_MarkerAxis iMarkerAxis);
}
