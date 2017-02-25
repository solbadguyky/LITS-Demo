package solstudios.app.moduls.mapviews.MapPool.Interfaces;

/**
 * I_Poolable để lấy một mapview, khởi tạo trước khi gắn nó vào một đối tượng trên bản đồ thông qua
 * I_MarkerAxis
 */
public interface I_Poolable {
    /**
     * Khởi tạo object
     */
    void initializePoolObject();

    /**
     * Giải phóng view
     */
    void finalizePoolObject();
}
