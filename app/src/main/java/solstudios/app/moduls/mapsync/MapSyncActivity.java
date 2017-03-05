package solstudios.app.moduls.mapsync;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import solstudios.app.R;
import solstudios.app.utilities.Devices;

/**
 * MaySync là modul định vị user realtime trên bản đồ
 * Khi vị trí hiện tại (current location) trên thiết bị được kích hoạt, nó sẽ gửi thông báo đồng bộ
 * mỗi 15s
 * <p>
 * Nếu có một thiết bị khác log-in vào cùng một tài khoản, việc đồng bộ sẽ bị ngưng lại (đồng thời
 * chuyển cho thiết bị kia) cho đến khi có tương tác đến từ thiết bị này
 * <p>
 * Một popup sẽ hiện lên và thông báo việc bị ngưng đồng bộ
 * <p>
 * Created by SolbadguyKY on 05-Feb-17.
 */
public class MapSyncActivity extends FragmentActivity
        implements OnMapReadyCallback {

    int[] baseAxis;
    private RelativeLayout rootView;
    private GoogleMap mMap;
    TextView debugView;
    MarkerOptions mMarkerOptions;
    Marker myMarker, myNullLocation;
    boolean isMyDeviceActive = true; ///biến isMydeviceactive dùng để xác định thiết bị hiện có
    /// được kích hoạt hay không

    Handler handler = new Handler();
    private RepeatRunnable periodicUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_views);

        rootView = (RelativeLayout) findViewById(R.id.mapsStatus_RelativeLayout_Root);
        debugView = (TextView) findViewById(R.id.debugView);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        baseAxis = new int[]{Devices.getDefaultDevice(this).getSize(true) / 5,
                Devices.getDefaultDevice(this).getSize(false) * 2 / 3};

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.getUiSettings().setScrollGesturesEnabled(false);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                generateNullLocation();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                manualUpdateLocation();
                return true;
            }
        });

        ///Ghi nhận khi location có sự thay đổi và đang active, sẽ logging ngay tọa độ lên server
        SmartLocation.with(this).location()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(final Location location) {
                        if (isMyDeviceActive) {
                            loggingToSever(location);
                        } else {
                            alertReActiveDialog();
                            if (periodicUpdate != null) {
                                handler.removeCallbacks(periodicUpdate);
                                periodicUpdate = null;
                            }
                        }
                    }
                });

        getCurrentRunnable().run();
    }

    /**
     * Thao tác cập nhật thủ công vị trí của thiết bị sẽ đồng thời kích hoạt chức năng logging thời
     * gian thực
     */
    public void manualUpdateLocation() {
        debugView.append("\nmanualUpdateLocation");

        SmartLocation.with(this).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {

                    @Override
                    public void onLocationUpdated(Location location) {
                        updateMarker(location);
                        loggingToSever(location);
                        isMyDeviceActive = true;
                    }
                });
    }

    public void updateMarker(Location location) {
        debugView.append("\nupdateMarker|location:"
                + location.getLatitude() + "," + location.getLongitude());
        if (mMarkerOptions == null) {
            mMarkerOptions = new MarkerOptions();
        }

        if (myMarker == null) {

            mMarkerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
            Date date = new Date();
            date.setTime(location.getTime());

            mMarkerOptions.title(date.toString());
            mMarkerOptions.draggable(true);

            myMarker = mMap.addMarker(mMarkerOptions);

        } else {
            myMarker.setPosition(new LatLng(location.getLatitude(),
                    location.getLongitude()));
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng
                (new LatLng(location.getLatitude(), location.getLongitude())
                ));
    }

    /**
     * (Giả lập việc đăng nhập từ một thiết bị khác
     * <p>
     * Khi có một thiết bị đăng nhập, quyền active sẽ ngay lập tức chuyển sang thiết bị đó
     */
    public void generateNullLocation() {
        double radLa = 10.8231 + Math.random() * 20;
        double radLo = 106.6297 + Math.random() * 50;
        debugView.append("\ngenerateNullLocation|location:"
                + radLa + "," + radLo);
        if (myNullLocation == null) {
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(new LatLng(radLa, radLo));

            myNullLocation = mMap.addMarker(markerOptions);
        } else {
            myNullLocation.setPosition(new LatLng(radLa, radLo));
        }

        ///Giả lập delay trong  setting active device
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isMyDeviceActive = false;
            }
        }, 2000);

    }

    /**
     * (Giả lập thao tác loggin đến server)
     * <p>
     * Mỗi khi nhận ra có sự thay đổi về vị trí của user, thiết bị sẽ gửi thông số LatLn lên server
     */
    public void loggingToSever(Location location) {
        debugView.append("\nloggingToSever|location:" + location.getLatitude() + "," + location.getLongitude());
    }

    public void loggingToSever(LatLng latln) {
        debugView.append("\nloggingToSever|location:" + latln.latitude + "," + latln.longitude);
    }

    /**
     * (Giả lập việc gửi request active device lên server)
     */
    public void sendRequestActiveToServer() {
        debugView.append("\nsendRequestActiveToServer");
    }

    public void alertReActiveDialog() {
        debugView.append("\nDeative Device");
        AlertDialog.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this, R.style.MaterialBaseTheme_AlertDialog);
        }
        builder.setTitle("Alert|Need attention");
        builder.setMessage("What's upp??????");
        builder.setPositiveButton("Active", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isMyDeviceActive = true;
                getCurrentRunnable().run();
            }
        });

        builder.show();
    }

    public RepeatRunnable getCurrentRunnable() {
        if (periodicUpdate == null) {
            periodicUpdate = new RepeatRunnable();
        }

        return periodicUpdate;
    }

    private class RepeatRunnable implements Runnable {

        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, 5 * 1000);
            ///Sau mỗi (x) giây kể từ lần loggin hoặc thao tác cuối cùng trong trạng thái active dược
            ///kích hoạt, việc cập nhật sẽ được thực hiện
            if (isMyDeviceActive) {
                if (myMarker != null) {
                    loggingToSever(myMarker.getPosition());
                }

            } else {
                alertReActiveDialog();
                if (periodicUpdate != null) {
                    handler.removeCallbacks(periodicUpdate);
                    periodicUpdate = null;
                }
            }
        }
    }
}

