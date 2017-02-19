package solstudios.app.moduls.anchorpoint;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import solstudios.app.R;
import solstudios.app.utilities.Devices;

/**
 * LayoutAnchor Activity là hệ thống tọa độ hóa các đơn vị hiển thị trên màn hình
 * Anchor Object là các điểm neo, là các tọa độ gốc nhằm mục đích định hướng các đơn vị hiển thị
 * Created by SolbadguyKY on 05-Feb-17.
 */
public class LayoutAnchorActivity extends FragmentActivity implements OnMapReadyCallback {

    private RelativeLayout rootView;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    public static int swidth, sheight;
    int[] baseAxis;
    static int SNAP_DISTANCE = 150;
    Marker myFocusMarker;
    ArrayList<MarkerAxisView> markerAxisViewList;
    Map<MarkerAxisView, MarkerOptions> myMarkerMaps = new HashMap<>();
    CircleAxisView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        baseAxis = new int[]{Devices.getDefaultDevice(this).getSize(true) / 5,
                Devices.getDefaultDevice(this).getSize(false) * 2 / 3};

        rootView = (RelativeLayout) findViewById(R.id.mapsStatus_RelativeLayout_Root);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        swidth = Devices.getDefaultDevice(this).getSize(true) - baseAxis[0];

        sheight = baseAxis[1];

        myMarkerMaps = new HashMap<>();
        markerAxisViewList = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        createMapAxis();
        mMap = googleMap;

        final LatLng marker = new LatLng(10.8231, 106.6297);
        MarkerOptions mMarkerOptions = new MarkerOptions().position(marker)
                .title("0").draggable(false);
        Marker mMarker = mMap.addMarker(mMarkerOptions);
        mMarker.setTag("0");
        MarkerAxisView mMarkerAxisView = new MarkerAxisView(this, mMarkerOptions, circleView);
        mMarkerAxisView.setTag("0");
        myMarkerMaps.put(mMarkerAxisView, mMarkerOptions);
        myFocusMarker = mMarker;

        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                ///Khi camera di chuyển sẽ update lại vị trí của statusview
                for (MarkerAxisView mAxis : myMarkerMaps.keySet()) {
                    MarkerOptions mOptions = myMarkerMaps.get(mAxis);
                    Projection projection = mMap.getProjection();
                    ///tính toán lại vị trí của từng marker
                    LatLng markerLocation = mOptions.getPosition();
                    Point screenPosition = projection.toScreenLocation(markerLocation);
                    int screenPositionX = screenPosition.x;
                    int screenPositionY = screenPosition.y;
                    ///Nếu marker axis chưa được thêm vào thì sẽ addView
                    if (rootView.findViewWithTag(mAxis.getTag()) == null) {
                        rootView.addView(mAxis);
                    }

                    mAxis.setLocation(mMap);
                    // mAxis.invalidate();

                    ///Tracking vùng di chuyển của marker object
                    if (
                            (screenPositionX < (Devices.getDefaultDevice(LayoutAnchorActivity.this).getSize(true))
                                    &&
                                    screenPositionX > 0)
                                    &&
                                    screenPositionY < Devices.getDefaultDevice(LayoutAnchorActivity.this).getSize(false)
                                    &&
                                    screenPositionY > 0
                            ) {
                        Logger.d("Marker Onscreen");


                    } else {
                        ///Thuật toán recycle view chưa được xây dựng, nên mội khi markerView đi ra khỏi màn hình sẽ không render view đó nữa
                        Logger.d("Marker Offscreen");
                    }
                }

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ///Marker được click vào sẽ có mức độ ưu tiên cao nhất
                myFocusMarker = marker;
                for (MarkerAxisView mAxis : myMarkerMaps.keySet()) {
                    if (mAxis.getTag().equals(marker.getTag())) {
                        printMarkerLog(mAxis);
                    }
                }
                return false;
            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                ///Set max/min zoom
                float maxZoom = 18f;

                float positonZoom = mMap.getCameraPosition().zoom;
                //Logger.d("Debug", "CurrentZoom: " + positonZoom);
                if (positonZoom != maxZoom) {
                    // mMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
                }/* else if (positonZoom < minZoom) {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
                }*/   float minZoom = 8f;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ///Click vào bản đồ sẽ focus vào marker chính
                if (myFocusMarker != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myFocusMarker.getPosition()));

                }


            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                ///Tạo marker tại vị trí được chọn
                createRandomMarker(latLng);
            }
        });

        Button resetButton = (Button) findViewById(R.id.button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((SeekBar) findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ///Thay dổi
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Tạo marker ngẫu nhiên tại vị trí được chọn
     *
     * @param latLng tọa độ latlong được thêm vào để test
     */
    private void createRandomMarker(LatLng latLng) {
        LatLng randomMarker = new LatLng(latLng.latitude, latLng.longitude);
        MarkerOptions mMarkerOptions = new MarkerOptions().position(randomMarker).title("" + myMarkerMaps.size());
        /*Marker marker =*/
        mMap.addMarker(mMarkerOptions).setTag("" + myMarkerMaps.size());

        MarkerAxisView mMarkerAxisView = new MarkerAxisView(this, mMarkerOptions, circleView);
        mMarkerAxisView.setTag("" + myMarkerMaps.size());
        myMarkerMaps.put(mMarkerAxisView, mMarkerOptions);

    }

    /**
     * MapAxis hay còn gọi là điểm neo chính, là nơi quản lí sự tương tác giữa chúng
     * với MarkerAxis/Anchor Object
     */
    private void createMapAxis() {
        circleView = new CircleAxisView(this);
        circleView.setPoint(baseAxis[0], baseAxis[1]);
        rootView.addView(circleView);
    }

    void printMarkerLog(MarkerAxisView markerAxisView) {
        Logger.d("MarkerAxis|Bounds: w = " + markerAxisView.getWidth() + ",h = " + markerAxisView.getHeight());
    }
}