package solstudios.app.moduls.mapviews.MapTask.TaskInterface;

import android.content.Context;

public interface I_Task {
    void onComplete();

    void onDoingWork();

    void onStart(MapRunnn runnable);

    void onStop();

    class MapRunnn implements Runnable, I_Task {
        private Context mContext;

        protected MapRunnn(Context context) {
            this.mContext = context;
        }

        @Override
        public void run() {
            onDoingWork();
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onDoingWork() {

        }

        @Override
        public void onStart(MapRunnn runnable) {

        }

        @Override
        public void onStop() {

        }
    }
}
