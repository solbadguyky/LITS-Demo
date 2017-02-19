package solstudios.app.moduls.mapviews.MapPool.Interfaces;

/**
 * I_Poolable là interface dùng để quản lí việc tạo đối tượng ở mức thấp nhất, chưa
 * phân biệt loại view hay thuộc tính
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
