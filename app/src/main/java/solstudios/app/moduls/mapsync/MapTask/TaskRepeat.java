package solstudios.app.moduls.mapsync.MapTask;

import android.content.Context;

import solstudios.app.moduls.mapviews.MapTask.TaskInterface.I_Task;

/**
 * Created by SolbadguyKY on 04-Mar-17.
 */

public class TaskRepeat implements I_Task {

    private Context mContext;
    private boolean isOnDuty = false; ///Nếu một task được yêu cầu trong khoảng thời gian count-down
    ///task sẽ được thực hiện ngay, đồng thời reset count-down
    private MapRunnn mapRunnn;

    private static TaskRepeat currentTaskSchedule;

    private TaskRepeat(Context context) {
        this.mContext = context;
    }

    public static TaskRepeat getCurrentTaskSchedule(Context context) {
        if (currentTaskSchedule == null) {
            currentTaskSchedule = new TaskRepeat(context);
        }

        return currentTaskSchedule;
    }

    @Override
    public void onComplete() {
        isOnDuty = false;
    }

    @Override
    public void onDoingWork() {
        isOnDuty = true;
    }

    @Override
    public void onStart(MapRunnn runnable) {
        ///Nếu không có task nào đang chạy thì start ngay
        if (isOnDuty) {
            if (mapRunnn != null) {
                mapRunnn.onStop();
            }

            mapRunnn = runnable;
            mapRunnn.run();
        }

    }

    @Override
    public void onStop() {
        isOnDuty = false;
    }
}