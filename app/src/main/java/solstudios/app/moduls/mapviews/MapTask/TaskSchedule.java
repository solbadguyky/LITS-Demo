package solstudios.app.moduls.mapviews.MapTask;

import android.content.Context;
import android.os.Handler;

import solstudios.app.moduls.mapviews.MapTask.TaskInterface.I_Task;

/**
 * Các công việc tính toán trên bản đồ sẽ được thực hiện sau mội một khoảng time-interval
 * để tránh quá tải trên server cũng như quá tải trên ứng dụng
 * <p>
 * Khác với việc đưa task cần xử lí vào stack, những task được yêu cầu khi TaskSchedule vẫn
 * 'onDuty* sẽ bị cancel - tức là không được thực hiện cho đến khi có yêu cầu vào thời điểm khác
 * <p>
 * Sau 250ms, nếu không có thao tác mới thì sẽ update lần cuối tọa độ được hiển thị lên
 * <p>
 * Created by SolbadguyKY on 20-Feb-17.
 */
public class TaskSchedule implements I_Task {

    private Context mContext;
    private boolean isOnDuty = false; ///Nếu TaskSchedule đang trong 'duty', những công việc tiếp
    /// theo nếu được yêu cầu sẽ bị 'cancel'
    private MapRunnn mapRunnn;

    private static TaskSchedule currentTaskSchedule;

    private TaskSchedule(Context context) {
        this.mContext = context;
    }

    public static TaskSchedule getCurrentTaskSchedule(Context context) {
        if (currentTaskSchedule == null) {
            currentTaskSchedule = new TaskSchedule(context);
        }

        return currentTaskSchedule;
    }

    @Override
    public void onComplete() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isOnDuty = false;
            }
        }, 250);
    }

    @Override
    public void onDoingWork() {
        isOnDuty = true;
    }

    @Override
    public void onStart(MapRunnn runnable) {
        ///Nếu không có task nào đang chạy thì start ngay
        if (!isOnDuty) {
            mapRunnn = runnable;
            mapRunnn.run();
        }

    }


}
