package solstudios.app.moduls.mapviews;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;

import java.util.ConcurrentModificationException;
import java.util.Map;

import solstudios.app.R;
import solstudios.app.moduls.anchorpoint.AbsMapView;
import solstudios.app.moduls.anchorpoint.CircleAxisView;
import solstudios.app.moduls.anchorpoint.StatusView;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_MarkerAxis;
import solstudios.app.moduls.mapviews.MapPool.MapMarkerBoundHelper;
import solstudios.app.moduls.mapviews.MapPool.ObjectPool;
import solstudios.app.moduls.mapviews.MapTask.TaskInterface.I_Task;
import solstudios.app.moduls.mapviews.MapTask.TaskSchedule;
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
    Marker myFocusMarker;
    int count = 0;
    private RelativeLayout rootView;
    private GoogleMap mMap;
    ObjectPool markerAxisViewPool;

    MapMarkerBoundHelper markerBoundAdapter;
    TextView debugView;

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

        circleView = new CircleAxisView(this);
        circleView.setPoint(baseAxis[0], baseAxis[1]);
        // rootView.addView(circleView);
        markerAxisViewPool = new ObjectPool(this, 3);
        markerBoundAdapter = new MapMarkerBoundHelper();

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        // mMap.getUiSettings().setScrollGesturesEnabled(false);
        for (int i = 0; i <= 10; i++) {
            createRandomMarker(i);
        }

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                ///Khi camera di chuyển sẽ update lại vị trí của mapview,
                ///Nhưng để đảm bảo hiệu suất hoạt động của ứng dụng, việc tính toán sẽ được thực
                ///hiện sau mỗi 500ms
                if (markerAxisViewPool.getCurrentSize() == markerAxisViewPool.getMaxSize()) {
                    ///Nếu phát hiện đã có marker được gắn statusView, mọi công việc tính toán sẽ được
                    // thực hiện cho đến khi view đó "thả" I_PMarkerAxis ra
                    doingDoingMarkerJobss();
                } else {
                    //Nếu số lượng MarkerAxis chưa đạt đủ số lượng tối đa, sẽ tìm Marker có "nhu cầu"
                    //hiển thị
                    doingDoingMarkerJobss();
                    TaskSchedule.getCurrentTaskSchedule(MapViewsActivity.this)
                            .onStart(new I_Task.MapRunnn(MapViewsActivity.this) {
                                @Override
                                public void run() {
                                    super.run();
                                    doingMarkerJobss();
                                    TaskSchedule.getCurrentTaskSchedule(MapViewsActivity.this).onComplete();
                                }
                            });
                }

                ///Ngay khi bản đồ có tương tác, bộ đếm rebase sẽ bị reset
               /* ReBaseTaskSchedule.getCurrentTaskSchedule(MapViewsActivity.this)
                        .onStart(new I_Task.MapRunnn(MapViewsActivity.this) {
                            @Override
                            public void run() {
                                super.run();

                            }
                        });*/

            }
        });

    }

    void doingMarkerJobss() {
        debugView.setText("Current Visible Marker:\n");
        final Projection projection = mMap.getProjection();
        LatLng latln1 = projection.getVisibleRegion().latLngBounds.southwest;
                /*projection.
                fromScreenLocation(new Point(0,
                        Devices.getDefaultDevice(MapViewsActivity.this)
                                .getSize(false)));*/
        LatLng latln2 = projection.getVisibleRegion().latLngBounds.northeast;
        //projection.fromScreenLocation(
        //  new Point(Devices.getDefaultDevice(MapViewsActivity.this)
        //          .getSize(true), 0));
        markerBoundAdapter.currentMapBound = new LatLngBounds(latln1, latln2);
        for (MarkerOptions markerOptions :
                markerBoundAdapter.sortArrayBaseOnLatLn(
                        latln1, latln2)
                ) {
            /*Logger.d("markerOptions:" +
                    markerOptions.getTitle() + "+Lat: "
                    + markerOptions.getPosition().latitude + "|Long: "
                    + markerOptions.getPosition().longitude);*/
            debugView.append(
                    markerOptions.getTitle() + /*"|Lat: "
                            + markerOptions.getPosition().latitude + "|Long: "
                            + markerOptions.getPosition().longitude +*/ "\n");

            //LatLng markerLocation = markerOptions.getPosition();
            //Point screenPosition = projection.toScreenLocation(markerLocation);
            //int screenPositionX = screenPosition.x;
            //int screenPositionY = screenPosition.y;

            ///Tracking vùng di chuyển của marker object

            //Logger.d("Marker Onscreen|marker: " + mOptions.getTitle());
            ///Kiểm tra đã có statusview gắn vào hay chưa, nếu chưa có thì lấy 1 view từ pool
            if (!markerAxisViewPool.findMarkerOptionHasBeenSet(markerOptions)) {
                StatusView markerAxisView = (StatusView) markerAxisViewPool
                        .adquirePoolObject(AbsMapView.Class.Status);
                if (markerAxisView != null) {
                    try {
                        rootView.addView(markerAxisView);
                    } catch (IllegalStateException e) {
                        ///Nếu view đã được thêm vào thì xóa trước khi tạo
                        rootView.removeView(markerAxisView);
                        rootView.addView(markerAxisView);

                    }

                    markerAxisViewPool.setMarkerObject(markerOptions, markerAxisView);
                    markerAxisView.setMarkerOption(markerOptions);
                    markerAxisView.initializePoolObject();

                }
            } else {
                //StatusView markerAxisView = (StatusView) markerAxisViewPool.findMarerAxis(markerOptions);
                //markerAxisView.setLocationWithLayout(screenPositionX, screenPositionY);
            }

        }
    }

    /**
     * Những I_MarkerAxis nào "có việc làm" rồi thì cứ để cho nó làm thôi
     */
    void doingDoingMarkerJobss() {
        MarkerOptions currentMarkerObject = null;
        if (markerAxisViewPool.getCurrentPoolObjects().size() > 0)
            try {
                ///trong quá trình load markeroption, một số marker có thể bị xóa trước khi
                ///các phép toán được thực hiện xong

                for (Map.Entry<MarkerOptions, I_MarkerAxis> e :
                        markerAxisViewPool.getCurrentPoolObjects().entrySet()) {
                    final Projection projection = mMap.getProjection();
                    LatLng markerLocation = e.getKey().getPosition();
                    Point screenPosition = projection.toScreenLocation(markerLocation);
                    int screenPositionX = screenPosition.x;
                    int screenPositionY = screenPosition.y;


                    ///Kiểm tra MarkerAxis có nằm ngoài mapbounds hay không
                    if (projection.getVisibleRegion().latLngBounds.contains(markerLocation)) {
                        e.getValue().setLocationWithLayout(screenPositionX, screenPositionY);
                    } else {
                        currentMarkerObject = e.getKey();
                        markerAxisViewPool.freePoolObject(e.getKey(), e.getValue());
                    }

                }
            } catch (ConcurrentModificationException ignored) {
                ///Object đã bị xóa trước khi chỉnh sửa
                if (currentMarkerObject != null) {
                    Logger.d("Sh**ttt!! DO 2nd plan now:" + currentMarkerObject);
                    markerAxisViewPool.freePoolObject(currentMarkerObject, null);
                }
            }
    }

    /**
     * Tạo một marker random trên bản đồ để test
     */
    void createRandomMarker(int i) {
        final LatLng marker;
        double radLa = 10.8231 + Math.random() * 20;
        //Math.random() * 100 + Math.random();
        double radLo = 106.6297 + Math.random() * 50;
        //Math.random() * 100 + Math.random();
        marker = new LatLng(radLa, radLo);
        //

        MarkerOptions mMarkerOptions = new MarkerOptions().position(marker)
                .title("Position_" + i).draggable(false);
        Marker mMarker = mMap.addMarker(mMarkerOptions);
        mMarker.setTag("Position_" + i);

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

