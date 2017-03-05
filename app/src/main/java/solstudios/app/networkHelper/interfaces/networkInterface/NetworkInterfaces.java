package solstudios.app.networkHelper.interfaces.networkInterface;


import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkInterfaces {

    public enum NetworkInstance {
        Posts, Channels
    }

    public interface I_Downloader {
        /**
         * Dữ liệu trả về
         *
         * @param instanceChannel
         * @param response
         */
        void onResponse(I_ChannelDownloader instanceChannel, JSONObject response);

        /**
         * Dữ liệu trả về
         *
         * @param instancePost
         * @param response
         */
        void onResponse(I_PostDownloader instancePost, JSONObject response);


    }

    public interface I_ChannelDownloader extends I_Downloader {
        void parseChannels(JSONArray rawChannelList);
    }

    public interface I_PostDownloader extends I_Downloader {

        /**
         * Xử lí dữ liệu trả về
         *
         * @param postArr
         * @throws JSONException
         */
        void parsePosts(JSONArray postArr) throws JSONException;


    }


    public static class DownloaderListener implements Response.Listener<JSONObject> {

        private I_Downloader i_downloader;
        private NetworkInstance networkInstance;

        public DownloaderListener(I_Downloader idownloader) {
            this.i_downloader = idownloader;
        }

        protected DownloaderListener() {

        }

        public void setNetworkInstance(NetworkInstance networkIstance) {
            this.networkInstance = networkIstance;
        }

        public I_Downloader getI_downloader() {
            return this.i_downloader;
        }

        public void overrideInterface(I_PostDownloader overridedInterface) {
            this.i_downloader = overridedInterface;
        }

        public void overrideInterface(I_ChannelDownloader overridedInterface) {
            this.i_downloader = overridedInterface;
        }

        @Override
        public void onResponse(JSONObject response) {

            if (i_downloader != null) {
                if (networkInstance != null) {
                    switch (networkInstance) {
                        case Posts:
                            responseToPost(response);
                            break;
                        case Channels:
                            responseToChannel(response);
                            break;
                    }
                } else {
                    if (!responseToPost(response)) {
                        responseToChannel(response);
                    }
                }

            }
        }

        boolean responseToPost(JSONObject response) {
            if (i_downloader instanceof I_PostDownloader) {
                i_downloader.onResponse((I_PostDownloader) this.i_downloader, response);

                return true;
            }

            return false;
        }

        boolean responseToChannel(JSONObject response) {
            if (i_downloader instanceof I_ChannelDownloader) {
                i_downloader.onResponse((I_ChannelDownloader) this.i_downloader, response);
                return true;
            }

            return false;
        }
    }
}
