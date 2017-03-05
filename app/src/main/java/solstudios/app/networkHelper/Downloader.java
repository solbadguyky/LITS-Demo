package solstudios.app.networkHelper;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import solstudios.app.instances.VolleyGlobalInstance;
import solstudios.app.networkHelper.interfaces.networkInterface.NetworkInterfaces;

/**
 * Downloader phụ trách tất cả những thao tác liên quan đến việc tải dữ liệu xuống thiết bị
 * <p>
 * Created by SolbadguyKY on 16-Jan-17.
 */
public class Downloader {
    public static final String TAG = "DownloadServiceHelper";
    public static final String NOKEY = "nokeyvalue";
    private Context context;
    private NetworkInterfaces.NetworkInstance networkInstance;

    public Downloader(Context context) {
        this.context = context;
    }

    public void setDownloaderInterface(NetworkInterfaces.NetworkInstance networkInstance) {
        this.networkInstance = networkInstance;
    }

    /**
     * Tải JSON từ server
     *
     * @param url
     * @param listener
     * @param errorListener
     */
    public void downloadJSON(String url, NetworkInterfaces.DownloaderListener listener,
                             Response.ErrorListener errorListener) {
        if (networkInstance != null) {
            //Logger.d(i_downloader.getClass().equals(NetworkInterfaces.I_ChannelDownloader.class));
            listener.setNetworkInstance(networkInstance);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener,
                errorListener);
        VolleyGlobalInstance.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Tải JSON từ server nhưng có kiểm tra khi tải xong
     *
     * @param url
     * @param listener
     * @param errorListener
     * @param finishListener
     */
    public void downloadJSON(String url, Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener,
                             RequestQueue.RequestFinishedListener<JSONObject> finishListener) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener,
                errorListener);
        VolleyGlobalInstance.getInstance(context).addToRequestQueueWithCompleteListener(request,
                finishListener);
    }

    /**
     * Những dữ liệu còn thiếu sẽ được xử lí bởi bộ Caching_Downloading của ứng dụng (Dùng trong trường
     * hợp Restful API chưa có dữ liệu đó
     * MissingKey được dùng để kiểm tra dữ liệu đã có hay chưa
     * Các trạng thái của DownloadMissingData bao gồm: NO_DATA , DOWNLOADING
     * <p>
     * Nếu nơi request messingKey vẫn còn hiển thị trên màn hình thì tiến hành cập nhật thông tin tại đó
     *
     * @param context
     * @param missingKey
     * @param missingJsonTag
     * @param url
     * @param finishListener
     */
    public static void downloadMissingData(Context context, String missingKey, final String missingJsonTag, String url,
                                           RequestQueue.RequestFinishedListener<JSONObject> finishListener) {


    }

}
