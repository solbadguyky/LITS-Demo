package solstudios.app.moduls.mapviews;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import solstudios.app.R;
import solstudios.app.moduls.anchorpoint.CircleAxisView;
import solstudios.app.moduls.anchorpoint.StatusView;
import solstudios.app.moduls.mapviews.MapPool.MapMarkerBoundHelper;
import solstudios.app.moduls.mapviews.MapPool.ObjectPool;
import solstudios.app.moduls.mapviews.MapPool.PoolObjectFactory;
import solstudios.app.utilities.Devices;

/**
 * MapViewsActivity Activity dùng để load các đơn vị hiển thị lên màn hình, bao gồm MarkerView
 * <p>
 * Thuật toán tính toán vị trí / tracking vị tri hiển thị của marker trên màn hình:
 * Khi màn hình/bản đồ di chuyển, sẽ theo dõi vị trí trên bản đồ, đối chiếu với danh sách
 * marker hiện có để tính toán khoảng cách có thuộc vùng hiển thị hay không
 * <p>
 * Khi marker đã đi vào vùng hiển thị, sẽ đối chiếu vị trí marker so với điểm neo để xác định những marker
 * được ưu tiên. Néu đủ số lượng mapview được hiển thi sẽ không theo dõi vị trí của các marker còn lại
 * cho đến khi có một marker nằm ngoài vùng hiển thị
 * <p>
 * <p>
 * Created by SolbadguyKY on 05-Feb-17.
 */
public class MapViewsActivity extends FragmentActivity
        implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    CircleAxisView circleView;
    int[] baseAxis;
    Map<MarkerOptions, Boolean> myMarkerMaps = new HashMap<>();
    Marker myFocusMarker;
    int count = 0;
    private RelativeLayout rootView;
    private GoogleMap mMap;
    PoolObjectFactory poolObjectFactory;
    ObjectPool markerAxisViewPool;

    MapMarkerBoundHelper markerBoundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_views);

        rootView = (RelativeLayout) findViewById(R.id.mapsStatus_RelativeLayout_Root);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        baseAxis = new int[]{Devices.getDefaultDevice(this).getSize(true) / 5,
                Devices.getDefaultDevice(this).getSize(false) * 2 / 3};

        circleView = new CircleAxisView(this);
        circleView.setPoint(baseAxis[0], baseAxis[1]);
        // rootView.addView(circleView);
        markerAxisViewPool = new ObjectPool(this, 2);
        markerBoundAdapter = new MapMarkerBoundHelper();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i <= 20; i++) {
            createRandomMarker(i);
        }


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                ///Khi camera di chuyển sẽ update lại vị trí của mapview
                final Projection projection = mMap.getProjection();

                for (MarkerOptions markerOptions : markerBoundAdapter.sortArrayBaseOnLatLn(projection.
                                fromScreenLocation(new Point(0, Devices.getDefaultDevice(MapViewsActivity.this).getSize(false))),
                        projection.
                                fromScreenLocation(new Point(Devices.getDefaultDevice(MapViewsActivity.this).getSize(true), 0)))
                        ) {
                    //Logger.d("markerOptions:" + markerOptions.getTitle() + "+Lat: " + markerOptions.getPosition().latitude + "|Long: " +
                    //       markerOptions.getPosition().longitude);

                }

            }
        });

    }

    /**
     * Tạo một marker random trên bản đồ để test
     */
    void createRandomMarker(int i) {
        final LatLng marker;
        double radLa = Math.random() * 100 + Math.random();
        double radLo = Math.random() * 100 + Math.random();
        marker = new LatLng(radLa, radLo);
        //

        MarkerOptions mMarkerOptions = new MarkerOptions().position(marker)
                .title("Position_" + i).draggable(false);
        Marker mMarker = mMap.addMarker(mMarkerOptions);
        mMarker.setTag("Position_" + i);
        myMarkerMaps.put(mMarkerOptions, false);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));

        this.markerBoundAdapter.addMarkerOptions(mMarkerOptions);
    }


    void layoutMarkerView(StatusView mMarkerAxisView) {
        mMarkerAxisView.getLayoutParams().width = 1000;//1000;
        mMarkerAxisView.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;//RelativeLayout.LayoutParams.WRAP_CONTENT;

        mMarkerAxisView.setX((float) (Math.random() * 1000));
        mMarkerAxisView.setY((float) (Math.random() * 1000));

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    /**
     * ArrayList<MarkerOptions> markerOptionsArrayList =
     markerBoundAdapter.getMarkerOptionsBaseOnBound(projection,
     true);
     ArrayList<MarkerOptions> inActiveMarkerOptionsArrayList =
     markerBoundAdapter.getMarkerOptionsBaseOnBound(projection, false);

     for (MarkerOptions mOptions : inActiveMarkerOptionsArrayList) {
     ///Thuật toán recycle view chưa được xây dựng,
     // nên mội khi markerView đi ra khỏi màn hình sẽ không render view đó nữa
     //Logger.d("Marker Offscreen|marker: " + mOptions.getTitle());
     if (myMarkerMaps.get(mOptions)) {
     // Logger.d("Marker " + mOptions.getTitle() + " need some help!!!");
     myMarkerMaps.put(mOptions, false);
     ///Đưa Mapview vào trạng thái chờ tái chế
     I_MarkerAxis iMarkerAxis = markerAxisViewPool.freePoolObject(mOptions);
     rootView.removeView((View) iMarkerAxis);
     }
     }

     int selectedViewCount = 0;
     for (MarkerOptions mOptions : markerOptionsArrayList) {
     ///tính toán lại vị trí của từng marker
     if (selectedViewCount == 2) {
     break;
     }
     LatLng markerLocation = mOptions.getPosition();
     Point screenPosition = projection.toScreenLocation(markerLocation);
     int screenPositionX = screenPosition.x;
     int screenPositionY = screenPosition.y;

     ///Tracking vùng di chuyển của marker object

     //Logger.d("Marker Onscreen|marker: " + mOptions.getTitle());
     //Kiểm tra đã có statusview gắn vào hay chưa, nếu chưa có thì lấy 1 view từ pool
     if (!myMarkerMaps.get(mOptions)) {
     StatusView markerAxisView = (StatusView) markerAxisViewPool
     .adquirePoolObject(AbsMapView.Class.Status, mOptions);
     if (markerAxisView != null) {
     try {
     rootView.addView(markerAxisView);
     myMarkerMaps.put(mOptions, true);
     markerAxisView.setMarkerOption(mOptions);

     if (!markerAxisView.isInitialized()) {
     markerAxisView.setLocationWithLayout(screenPositionX, screenPositionY);
     layoutMarkerView(markerAxisView);

     markerAxisView.initializePoolObject();
     } else {
     markerAxisView.setLocationWithLayout(screenPositionX, screenPositionY);
     }

     } catch (IllegalStateException e) {
     ///Nếu view đã được thêm vào thì xóa trước khi tạo
     rootView.removeView(markerAxisView);
     rootView.addView(markerAxisView);
     }
     }
     } else {
     StatusView markerAxisView = (StatusView) markerAxisViewPool.findMarerAxis(mOptions);
     markerAxisView.setLocationWithLayout(screenPositionX, screenPositionY);
     }

     selectedViewCount++;
     }
     */
}

