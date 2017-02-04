package solstudios.app;

import android.app.Application;

import com.orhanobut.logger.Logger;

import timber.log.Timber;

public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // initiate Timber
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                Logger.log(priority, tag, message, t);
            }
        });

        // Logger configuration
        // Logger.init().hideThreadInfo().logLevel(LogLevel.NONE);
    }
}
