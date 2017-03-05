package solstudios.app.networkHelper;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface RetrofitHelper {

    @Headers("Content-Type : application/json; charset=UTF-8")
    @FormUrlEncoded
    @POST("api.php")
    Call<JSONObject> postThreadXf(
            @FieldMap Map<String, String> params, @Field("title") String title,
            @Field("message") String content);

    @Headers("Content-Type : application/json; charset=UTF-8")
    @FormUrlEncoded
    @POST("posts/")
    Call<JSONObject> postThreadWp(
            @FieldMap Map<String, String> params,
            @Field("content") String content,
            @Header("Authorization") String basickey);
}
