package solstudios.app.moduls.mapviews.MapTask;

import android.content.Context;
import android.os.Handler;

import solstudios.app.moduls.mapviews.MapTask.TaskInterface.I_Task;


/**
 * Sau một khoảng thời gian không tương tác trên bản đồ, tất cả những marker nằm ngoài MapBound sẽ bị clear khỏi
 * bộ nhớ
 */
public class ReBaseTaskSchedule implements I_Task {
    private final int RebaseMessage = 100;
    private Context mContext;
    private boolean isNomoreAction = false;
    private static ReBaseTaskSchedule currentTaskSchedule;
    private Handler mHandler;
    private MapRunnn mapRunnn;

    private ReBaseTaskSchedule(Context context) {
        this.mContext = context;
    }

    public static ReBaseTaskSchedule getCurrentTaskSchedule(Context context) {
        if (currentTaskSchedule == null) {
            currentTaskSchedule = new ReBaseTaskSchedule(context);
        }

        return currentTaskSchedule;
    }


    @Override
    public void onComplete() {
        ///Sau 500ms mà không có tương tác sẽ bắt đầu đếm ngược đến thời gian rebase
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ReBaseTaskSchedule.getCurrentTaskSchedule(mContext).isNomoreAction = false;
            }
        }, 500);*/
    }

    @Override
    public void onDoingWork() {

    }

    @Override
    public void onStart(MapRunnn runnable) {
        ///Kiểm tra có task nào đang run hay không, nếu có thì cancel
        if (isNomoreAction) {
            isNomoreAction = false;
        }

        ///Khởi động bộ đếm ngược nếu không có action, nếu có thì clear action cũ
        try {
            getHandler().removeCallbacks(mapRunnn);
        } catch (Exception ignored) {

        }
        mapRunnn = runnable;
        getHandler().postDelayed(mapRunnn, 500);
    }

    private Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }
}
