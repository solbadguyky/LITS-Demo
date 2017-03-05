package solstudios.app.networkHelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import solstudios.app.instances.VolleyGlobalInstance;

/**
 * Request Helper thực hiện công việc gửi bài viết đến các site
 * <p>
 * Created by SolbadguyKY on 09-Feb-17.
 *
 * $2y$10$1qc/aJ3DLLeDihuaBnmuh.iX.leJ2hoh6jMXItqWX1PkZosQSEspe
 */
public class Requester {
    public static final String TAB = "RequestServiceHelper";

    public enum RequesterKey {
        Base_Url("baseUrl"),
        Title("title"), Url("url"), Excerpt("excerpt"), Content("content"), Message("message"),
        Node_Id("nodeId"), Categories("categories");

        private String value;

        RequesterKey(String key) {
            this.value = key;
        }

        public String getValue() {
            return value;
        }
    }

    private Context context;

    public Requester(Context context) {
        this.context = context;
    }

    public void sendPost(String postContent, String url, @Nullable final Map<String, String> params,
                         @Nullable JSONObject jsonBody, Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }
        };
        VolleyGlobalInstance.getInstance(context).addToRequestQueue(request);
    }

    /**
     * Gửi bài viết đến xenforo
     *
     * @param siteParams
     * @throws UnsupportedEncodingException
     * @throws JSONException
     * @throws NullPointerException
     */
    public void createUser(HashMap<String, String> siteParams, Callback<JSONObject> callback)
            throws UnsupportedEncodingException, JSONException, NullPointerException {

        Map<String, String> params = new HashMap<>();
        params.put("action", "createThread");
        params.put("node_id", siteParams.get(RequesterKey.Node_Id.getValue()));
        params.put("grab_as", "Admin");
        String title =
                siteParams.get(RequesterKey.Title.getValue()).length() <= 100 ? siteParams.get(RequesterKey.Title.getValue())
                        : (siteParams.get(RequesterKey.Title.getValue()).substring(0, 90) + "...");
        String message = siteParams.get(RequesterKey.Message.getValue());

        Gson gson = new GsonBuilder()
                .setLenient()
                .disableHtmlEscaping()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(siteParams.get(RequesterKey.Base_Url.getValue()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Logger.d(siteParams.get(RequesterKey.Base_Url.getValue()));

        RetrofitHelper retrofitHelper = retrofit.create(RetrofitHelper.class);
        Call<JSONObject> call = retrofitHelper.postThreadXf(params, title, message);
        call.enqueue(callback);
    }

    public interface I_Requester {
        void sendAll(int site) throws UnsupportedEncodingException, JSONException;

        void sendOne(int site, int index) throws UnsupportedEncodingException, JSONException;
    }
}
