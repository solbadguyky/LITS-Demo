package solstudios.app.moduls.anchorpoint;

import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
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
    TextView mainStatusTextView;
    ArrayList<MarkerOptions> markerList = new ArrayList<>();
    ArrayList<View> statusViewList = new ArrayList<>();
    final float maxZoom = 17.0f;
    final float minZoom = 2.0f;
    SupportMapFragment mapFragment;
    Display display;
    Point size = new Point();
    int swidth, sheight;

    int[] baseAxis;

    boolean isStatusUp = false;

    //private View selectedView;
    static int SNAP_DISTANCE = 150;
    Marker myMarker;
    MarkerOptions myMarkerOptions;
    MarkerAxis markerAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        baseAxis = new int[]{Devices.getDefaultDevice(this).getSize(true) / 5,
                (int) (Devices.getDefaultDevice(this).getSize(false) * 2 / 3)};

        rootView = (RelativeLayout) findViewById(R.id.mapsStatus_RelativeLayout_Root);
        mainStatusTextView = (TextView) findViewById(R.id.mapsStatus_MainStatus);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        swidth = Devices.getDefaultDevice(this).getSize(true) - baseAxis[0];

        sheight = (int) baseAxis[1];

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        createMapAxis();
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        final LatLng sydney = new LatLng(-34, 151);
        markerList = new ArrayList<>();
        myMarkerOptions = new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true);
        // final MarkerOptions mMarkerOptions6 = new MarkerOptions().position(sydney).title("Marker in Sydney5").draggable(true);
        myMarker = mMap.addMarker(myMarkerOptions);
        myMarker.setTag("Solbadguyky");


        markerList.add(myMarkerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                Projection projection = mMap.getProjection();


                ///tính toán lại vị trí của từng marker

                LatLng markerLocation = myMarkerOptions.getPosition();
                Point screenPosition = projection.toScreenLocation(markerLocation);
                int screenPositionX = screenPosition.x;
                int screenPositionY = screenPosition.y;
                if (markerAxis == null) {
                    markerAxis = new MarkerAxis(LayoutAnchorActivity.this);
                    rootView.addView(markerAxis);
                } else {

                }
                markerAxis.setLocation(screenPositionX, screenPositionY);
                markerAxis.invalidate();
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
                    Logger.d("Marker Offscreen");
                }
                markerArrangeAlgorithm(screenPositionX, screenPositionY);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTag() == myMarker.getTag()) {
                    Projection projection = mMap.getProjection();
                    ///tính toán lại vị trí của từng marker
                    LatLng markerLocation = myMarkerOptions.getPosition();
                    Point screenPosition = projection.toScreenLocation(markerLocation);
                    int screenPositionX = screenPosition.x;
                    int screenPositionY = screenPosition.y;
                    if (markerAxis == null) {
                        markerAxis = new MarkerAxis(LayoutAnchorActivity.this);
                        rootView.addView(markerAxis);
                    } else {

                    }
                    markerAxis.setLocation(screenPositionX, screenPositionY);
                    markerAxis.invalidate();

                    markerArrangeAlgorithm(screenPositionX, screenPositionY);
                    markerAxis.markerStatusVisible = MarkerAxis.STATUS_VISIBLE.NORMAL;
                    return true;
                }
                return false;
            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                float maxZoom = 18f;
                float minZoom = 8f;
                float positonZoom = mMap.getCameraPosition().zoom;
                Logger.d("Debug", "CurrentZoom: " + positonZoom);
                if (positonZoom != maxZoom) {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(maxZoom));
                }/* else if (positonZoom < minZoom) {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(minZoom));
                }*/
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }
        });
    }

    void markerArrangeAlgorithm(int screenPositionX, int screenPositionY) {
        Logger.d("markerArrangeAlgorithm: x =" + screenPositionX + ", y = " + screenPositionY);

        ///tính toán lại điểm neo ảo trên status
        showStatusAt(screenPositionX, screenPositionY);

        if (markerAxis.markerStatusSide == MarkerAxis.STATUS_SIDE.UP) {
            ///Xử lí các trường hợp khi status đang UP

            if (screenPositionX < Devices.getDefaultDevice(LayoutAnchorActivity.this).getSize(true) - swidth
                    ||
                    screenPositionY > sheight
                    ) {
                // Nếu marker đi khỏi vùng ưu tiên, trạng thái sẽ chuyển sang dạng thu nhỏ MINIMAL
                minimizeStatusAt(screenPositionX, screenPositionY);
            }

            if (screenPositionX < -markerAxis.getCurrentWidth()
                    ||
                    screenPositionX > swidth
                    ||
                    screenPositionY < 0
                    ||
                    screenPositionY - markerAxis.getCurrentHeight() >
                            Devices.getDefaultDevice(LayoutAnchorActivity.this).getSize(false) - baseAxis[0]
                    ) {
                //Nếu marker đi khỏi vị trí trên màn hình, marker đi khỏi vị trí bên phải,
                // rìa trên status đi khỏi vị trí bên dưới và rìa phải status đi khỏi vị trí
                // bên trái thì status sẽ bị ẩn đi

                hideStatusAt();
            } else {
                showStatusAt(screenPositionX, screenPositionY);
            }
        } else {
            ///Xử lí các trường hợp khi status đang DOWN
            if (screenPositionX < Devices.getDefaultDevice(LayoutAnchorActivity.this).getSize(true) - swidth
                    ||
                    screenPositionY > sheight
                    ) {
                // Nếu marker đi khỏi vùng ưu tiên, trạng thái sẽ chuyển sang dạng thu nhỏ MINIMAL
                minimizeStatusAt(screenPositionX, screenPositionY);
            }


            if (screenPositionX < 0
                    ||
                    screenPositionX - markerAxis.getCurrentWidth() > swidth
                    ||
                    screenPositionY + markerAxis.getCurrentHeight() < 0
                    ||
                    screenPositionY > Devices.getDefaultDevice(LayoutAnchorActivity.this).getSize(false) - baseAxis[0]
                    ) {
                //Nếu cạnh dưới đi khỏi vị trí trên màn hình, dạnh trái đi khỏi vị trí bên phải,
                // marker đi khỏi vị trí bên dưới và rìa phải status đi khỏi vị trái
                //thì status sẽ bị ẩn đi
                hideStatusAt();
            } else {
                showStatusAt(screenPositionX, screenPositionY);
            }
        }

    }

    //TextView statusAxis;
    private View createView(int x, int y, int id) {

        View mView = new StatusView(this);
        //mView.setBackgroundColor(android.R.color.transparent);

        // statusAxis = (TextView) mView.findViewById(R.id.baseComment_Axis);
        return mView;
    }

    private void showStatusAt(int x, int y) {
        if (markerAxis.markerStatusVisible == MarkerAxis.STATUS_VISIBLE.NORMAL) {
            mainStatusTextView.setText("Showing");
        }
        int centerX = Devices.getDefaultDevice(this).getSize(true) / 2;
        int centerY = Devices.getDefaultDevice(this).getSize(false) / 2;
        double distanceBetweenMarkerAndO = Math.sqrt(Math.abs((x - baseAxis[0]) * x) + Math.abs((y - baseAxis[1]) * y)) / 2;
        Logger.d("distanceBetweenMarkerAndO = " + distanceBetweenMarkerAndO);
        if (distanceBetweenMarkerAndO <= SNAP_DISTANCE) {
            ///Nếu marker ở khu vực ưu tiên xung quanh điểm neo chính thì sẽ tự động bật lên
            markerAxis.markerStatusSide = MarkerAxis.STATUS_SIDE.UP;
            markerAxis.markerStatusVisible = MarkerAxis.STATUS_VISIBLE.NORMAL;
        } else {
            if (markerAxis.markerStatusVisible == MarkerAxis.STATUS_VISIBLE.NORMAL) {
                /// Nếu marker đang được hiển thị thì sẽ không tùy chỉnh gì

            } else if (markerAxis.markerStatusVisible == MarkerAxis.STATUS_VISIBLE.HIDDEN) {
                if (y < centerY) {
                    markerAxis.markerStatusSide = MarkerAxis.STATUS_SIDE.DOWN;

                } else {
                    markerAxis.markerStatusSide = MarkerAxis.STATUS_SIDE.UP;
                }
            }

        }

        markerAxis.invalidate();
    }

    private void hideStatusAt() {
        mainStatusTextView.setText("Hidding");
        markerAxis.markerStatusVisible = MarkerAxis.STATUS_VISIBLE.HIDDEN;
        markerAxis.invalidate();
    }

    private void minimizeStatusAt(int x, int y) {
        mainStatusTextView.setText("Minimizing");
        markerAxis.markerStatusVisible = MarkerAxis.STATUS_VISIBLE.MINIMIZE;
        markerAxis.invalidate();

    }

    private void getCurrentGPS() {
        SmartLocation.with(this).location()
                .config(LocationParams.BEST_EFFORT)
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        Logger.d("onLocationUpdated");

                        LatLng myLat = new LatLng(location.getLatitude(), location.getLongitude());
                        final MarkerOptions mMarkerOptions = new MarkerOptions().position(myLat).title("This is my World").draggable(true);
                        mMap.addMarker(mMarkerOptions);
                        markerList.add(mMarkerOptions);

                        rootView.addView(createView(0, 0, markerList.size()));
                        Logger.d("onLocationUpdated| markerList.size() = %d", markerList.size() - 1);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLat));
                    }
                });
    }

    CircleAxisView circleView;

    private void createMapAxis() {
        View view = new View(this);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        view.setBackgroundColor(Color.BLACK);
        view.setAlpha(0.25f);

        circleView = new CircleAxisView(this);
        circleView.setPoint(baseAxis[0], baseAxis[1]);

        rootView.addView(view);
        rootView.addView(circleView);
    }

    private boolean needExpanding() {

        return false;
    }

}