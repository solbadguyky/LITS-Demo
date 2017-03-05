package solstudios.app.instances;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by SolbadguyKY on 16-Jan-17.
 */

public class VolleyGlobalInstance {
    private static VolleyGlobalInstance mInstance;
    private RequestQueue mRequestQueue;

    private static Context mCtx;

    private VolleyGlobalInstance(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyGlobalInstance getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyGlobalInstance(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueueWithCompleteListener(Request<T> req,
                                                          RequestQueue.RequestFinishedListener<T> finishListener) {
        getRequestQueue().add(req);
        getRequestQueue().addRequestFinishedListener(finishListener);
    }
}
