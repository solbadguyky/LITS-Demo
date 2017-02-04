package solstudios.app.devices;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;

import org.json.JSONObject;

import solstudios.app.utilities.LogTask;

public class Devices {
    public static final String TAB = "Devices";
    public static Device device;

    /**
     * DEVICE_TYPE: SMARTPHONE_SMALL, SMARTPHONE, SMARTPHONE_LARGE, TABLET,
     * TABLET_LARGE
     *
     * @author SolbadguyKY
     */
    public enum DEVICE_TYPE {
        SMARTPHONE_SMALL, SMARTPHONE, SMARTPHONE_LARGE, TABLET, TABLET_LARGE;
    }

    public enum DEVICE_ORIENTATION {
        PORTRAIT, LANDSCAPE
    }

    private Context context;

    public Devices(Context context) {
        this.context = context;
    }

    public DEVICE_TYPE getDeviceType() {
        if (isTablet()) {
            return DEVICE_TYPE.TABLET;
        } else {
            return DEVICE_TYPE.SMARTPHONE;
        }
    }

    public boolean isTablet() {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public boolean isLandscape() {
        if (context.getResources()
                .getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * get {@link ScreenViewBuilder} pixel
     *
     * @param getWidth
     * @return
     */
    public int getSize(boolean getWidth) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        new LogTask("Getting Screen Demension|w=" + width + ",h=" + height, TAB, LogTask.LOG_D);

        if (getWidth)
            return width;
        else
            return height;
    }

    public float getSizeInDP(boolean getWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        new LogTask("getSizeInDP|w=" + dpWidth + ",h=" + dpHeight, TAB, LogTask.LOG_D);

        if (getWidth)
            return dpWidth;
        else
            return dpHeight;
    }

    public static Device getDefaultDevice(Context context) {
        if (device == null) {
            DEVICE_TYPE deviceType = DEVICE_TYPE.SMARTPHONE;
            if ((context.getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                deviceType = DEVICE_TYPE.TABLET;
            }

            Devices devices = new Devices(context);
            int[] screenSize = {devices.getSize(true), devices.getSize(false)};

            device = new Device(context, deviceType, screenSize);
        }
        return device;
    }

    public static class Device extends Devices {

        private Context context;

        public Device(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        public Device(Context context, DEVICE_TYPE deviceType2, int[] screenSize) {
            super(context);
            // TODO Auto-generated constructor stub
            this.context = context;
        }

        private DEVICE_TYPE deviceType;
        private int[] deviceScreenSize;
        private DEVICE_ORIENTATION deviceOrientation = DEVICE_ORIENTATION.PORTRAIT;
        private DeviceOrientationChangeListener orientationChangingListener;

        @Override
        public boolean isLandscape() {
            // TODO Auto-generated method stub
            return super.isLandscape();
        }

        @Override
        public int getSize(boolean getWidth) {
            // TODO Auto-generated method stub
            return super.getSize(getWidth);
        }

        /**
         * Get Device Type
         *
         * @return {@link phapluat.manager.devices#DEVICE_TYPE}
         * SMARTPHONE_SMALL, SMARTPHONE, SMARTPHONE_LARGE, TABLET,
         * TABLET_LARGE
         */
        public DEVICE_TYPE getDeviceType() {
            if (deviceType == null) {
                deviceType = DEVICE_TYPE.SMARTPHONE;
            }
            return this.deviceType;
        }

        public int[] getScreenSize() throws NullPointerException {
            if (deviceScreenSize == null) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                WindowManager windowManager = (WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE);
                windowManager.getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                int[] screenSize = {width, height};
                this.deviceScreenSize = screenSize;
            }
            return this.deviceScreenSize;
        }

        public boolean isTablet() {
            if (this.deviceType == DEVICE_TYPE.TABLET
                    || this.deviceType == DEVICE_TYPE.TABLET_LARGE) {
                return true;
            } else {
                return false;
            }
        }

        public void setOrientation(DEVICE_ORIENTATION orientation) {
            this.deviceOrientation = orientation;
            if (this.orientationChangingListener != null) {
                this.orientationChangingListener.onChangeRotation(orientation);
            }
        }

        public DEVICE_ORIENTATION getOrientation() {
            return this.deviceOrientation;
        }

        public void addOritationChangingListener(DeviceOrientationChangeListener listener) {
            this.orientationChangingListener = listener;
        }

        public interface DeviceOrientationChangeListener {
            void onChangeRotation(DEVICE_ORIENTATION orientation);
        }

        public String getDeviceModel() {
            return android.os.Build.MODEL;
        }

        public int getSDKVersion() {
            return android.os.Build.VERSION.SDK_INT;
        }

        public String getDeviceName() {
            return android.os.Build.DEVICE;
        }

        public String getProductionName() {
            return android.os.Build.PRODUCT;
        }

        public String getManufacturer() {
            return android.os.Build.MANUFACTURER;
        }

        public String createDeviceInfo() throws Exception {
            // root object
            JSONObject deviceObject = new JSONObject();

            /// device info
            deviceObject.put("device_name", getDeviceName());
            deviceObject.put("device_product_name", getProductionName());
            deviceObject.put("device_model", getDeviceModel());
            deviceObject.put("device_manufacturer", getManufacturer());
            /// device specs
            deviceObject.put("device_type", getDeviceType());
            deviceObject.put("device_screensize", getSize(true) + "x" + getSize(false));

            return deviceObject.toString();
        }

    }
}
