package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;

import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_Poolable;
import solstudios.app.utilities.Devices;

/**
 * Marker Axis là Tọa đọ hóa của một Status trên bản đồ
 * Marker Axis được xem như một status view hoàn chỉnh hiển thị trên bản đồ
 * Marker Axis chứa Anchor Object, là đối tượng dùng để xác định tương tác và vị trí giữa
 * những statusview với nhau
 */
public class MarkerAxisView extends AbsMapView implements I_Poolable {

    public float markerX, markerY;
    public STATUS_SIDE markerStatusSide = STATUS_SIDE.UP;
    public STATUS_VISIBLE markerStatusVisible = STATUS_VISIBLE.NORMAL;
    public MarkerOptions mMarkerOption;
    public int maxWidth = 350, maxHeight = 500;
    public int minWidth = 150, minHeight = 300;
    public boolean isEnableDebug = false;
    CircleAxisView circleAxisView;
    AnchorObject markerAnchor; ///marker anchor là nơi xử lí thuật toán hiển thị

    public MarkerAxisView(Context context) {
        super(context);
        init(context);
    }

    public MarkerAxisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MarkerAxisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MarkerAxisView(Context context, MarkerOptions markerOptions, CircleAxisView circleAxisView) {
        super(context);
        this.mMarkerOption = markerOptions;
        this.circleAxisView = circleAxisView;
        init(context);

    }

    public MarkerAxisView(Context context, int resLayoutId, MarkerOptions markerOptions, CircleAxisView circleAxisView) {
        super(context);
        inflate(context, resLayoutId, this);
        this.mMarkerOption = markerOptions;
        this.circleAxisView = circleAxisView;
        init(context);
    }

    int getCurrentHeight() {
        switch (markerStatusVisible) {
            case NORMAL:
                return maxHeight;
            case MINIMIZE:
                return minHeight;
            default:
                return maxHeight;
        }
    }

    int getCurrentWidth() {
        switch (markerStatusVisible) {
            case NORMAL:
                return maxWidth;
            case MINIMIZE:
                return minWidth;
            default:
                return maxWidth;
        }
    }

    private void init(Context context) {
        this.setWillNotDraw(false);
        if (markerAnchor != null) {
            ///marker anchor đã được thêm vào, không cần thêm vào nữa
            Logger.d("init| marker existed");

        } else {
            ///Nếu marker anchor chưa có thì tạo mới
            markerAnchor = new AnchorObject(context, this)/* {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(MarkerAxis.this.getMeasuredWidth(), MarkerAxis.this.getMeasuredHeight());
            }
        }*/;

            markerAnchor.setTag("Marker Anchor");
            markerAnchor.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            setBackgroundColor(getResources().getColor(android.R.color.transparent));

            if (isEnableDebug) {
                this.addView(markerAnchor);
            }

        }

    }

    /**
     * Dựa vào vị trí của Marker để vẽ lại
     *
     * @param mMap
     */
    public void setLocation(GoogleMap mMap) {
        if (mMarkerOption != null) {
            Projection projection = mMap.getProjection();
            ///tính toán lại vị trí của từng marker
            LatLng markerLocation = mMarkerOption.getPosition();
            Point screenPosition = projection.toScreenLocation(markerLocation);
            int screenPositionX = screenPosition.x;
            int screenPositionY = screenPosition.y;
            setLocation(screenPositionX, screenPositionY);

            markerArrangeAlgorithm(screenPositionX, screenPositionY);
        }
    }

    /**
     * @param x
     * @param y
     */
    public void setLocation(float x, float y) {
        //Logger.d("setLocation|x = %f , y = %f", x, y);
        markerX = x;
        markerY = y;
    }

    /**
     * Set vị trí của markerAxis đồng thời di chuyển đến vị trí đó
     *
     * @param x
     * @param y
     */
    public void setLocationWithLayout(float x, float y) {
        setLocation(x, y);
        layoutParams(x, y);

    }

    public void layoutParams(float x, float y) {
        setX(x);
        setY(y);
    }

    /**
     * Thuật toán hiển thị marker dựa vào CircleAxisView
     *
     * @param screenPositionX vị trí x hiện tại
     * @param screenPositionY vị trí y hiện tại
     */
    void markerArrangeAlgorithm(int screenPositionX, int screenPositionY) {
        //Logger.d("markerArrangeAlgorithm: x =" + screenPositionX + ", y = " + screenPositionY);

        ///tính toán lại điểm neo ảo trên status
        showStatusAt(screenPositionX, screenPositionY);

        if (markerStatusSide == MarkerAxisView.STATUS_SIDE.UP) {
            ///Xử lí các trường hợp khi status đang UP

            if (screenPositionX < Devices.getDefaultDevice(getContext()).getSize(true) - LayoutAnchorActivity.swidth
                    ||
                    screenPositionY > LayoutAnchorActivity.sheight
                    ) {
                // Nếu marker đi khỏi vùng ưu tiên, trạng thái sẽ chuyển sang dạng thu nhỏ MINIMAL
                minimizeStatusAt(screenPositionX, screenPositionY);
            }

            if (screenPositionX < -getCurrentWidth()
                    ||
                    screenPositionX > LayoutAnchorActivity.swidth
                    ||
                    screenPositionY < 0
                    ||
                    screenPositionY - getCurrentHeight() >
                            Devices.getDefaultDevice(getContext()).getSize(false) - circleAxisView.pointX
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
            if (screenPositionX < Devices.getDefaultDevice(getContext()).getSize(true) - LayoutAnchorActivity.swidth
                    ||
                    screenPositionY > LayoutAnchorActivity.sheight
                    ) {
                // Nếu marker đi khỏi vùng ưu tiên, trạng thái sẽ chuyển sang dạng thu nhỏ MINIMAL
                minimizeStatusAt(screenPositionX, screenPositionY);
            }


            if (screenPositionX < 0
                    ||
                    screenPositionX - getCurrentWidth() > LayoutAnchorActivity.swidth
                    ||
                    screenPositionY + getCurrentHeight() < 0
                    ||
                    screenPositionY > Devices.getDefaultDevice(getContext()).getSize(false) - circleAxisView.pointX
                    ) {
                //Nếu cạnh dưới đii,
                // marker đi khỏi vị trí bên dưới v khỏi vị trí trên màn hình, dạnh trái đi khỏi vị trí bên phảà rìa phải status đi khỏi vị trái
                //thì status sẽ bị ẩn đi
                hideStatusAt();
            } else {
                showStatusAt(screenPositionX, screenPositionY);
            }
        }

    }

    /**
     * Hiển thị markerview
     *
     * @param x
     * @param y
     */
    private void showStatusAt(int x, int y) {
        if (markerStatusVisible == MarkerAxisView.STATUS_VISIBLE.NORMAL) {
            // mainStatusTextView.setText("Showing");
        }

        int centerX = Devices.getDefaultDevice(getContext()).getSize(true) / 2;
        int centerY = Devices.getDefaultDevice(getContext()).getSize(false) / 2;
        double distanceBetweenMarkerAndO = Math.sqrt(Math.abs((x - circleAxisView.pointX) * x)
                + Math.abs((y - circleAxisView.pointY) * y)) / 2;
        //Logger.d("distanceBetweenMarkerAndO = " + distanceBetweenMarkerAndO);
        if (distanceBetweenMarkerAndO <= LayoutAnchorActivity.SNAP_DISTANCE) {
            ///Nếu marker ở khu vực ưu tiên xung quanh điểm neo chính thì sẽ tự động bật lên
            markerStatusSide = MarkerAxisView.STATUS_SIDE.UP;
            markerStatusVisible = MarkerAxisView.STATUS_VISIBLE.NORMAL;

        } else {
            if (markerStatusVisible == MarkerAxisView.STATUS_VISIBLE.NORMAL) {
                /// Nếu marker đang được hiển thị thì sẽ không tùy chỉnh gì
                ///Nếu không cần redraw thì chỉ set location on screen cua view

            } else if (markerStatusVisible == MarkerAxisView.STATUS_VISIBLE.HIDDEN) {
                if (y < centerY) {
                    markerStatusSide = MarkerAxisView.STATUS_SIDE.DOWN;

                } else {
                    markerStatusSide = MarkerAxisView.STATUS_SIDE.UP;
                }

            }

        }
        reDrawAnchor();
    }

    /**
     * Ẩn markerview
     */
    private void hideStatusAt() {
        if (markerStatusVisible != MarkerAxisView.STATUS_VISIBLE.HIDDEN) {

        }

        markerStatusVisible = MarkerAxisView.STATUS_VISIBLE.HIDDEN;
        reDrawAnchor();
    }

    /**
     * Thu nhỏ marker view
     *
     * @param x
     * @param y
     */
    private void minimizeStatusAt(int x, int y) {
        if (markerStatusVisible != MarkerAxisView.STATUS_VISIBLE.MINIMIZE) {

        } else {
            ///Nếu không cần redraw thì chỉ set location on screen cua view


        }

        markerStatusVisible = MarkerAxisView.STATUS_VISIBLE.MINIMIZE;
        reDrawAnchor();
    }

    void reDrawAnchor() {
        if (isEnableDebug) {
            markerAnchor.needRedraw = true;
            markerAnchor.loadLocation();
        }
    }

    @Override
    public void initializePoolObject() {

    }

    @Override
    public void finalizePoolObject() {

    }

    public enum STATUS_SIDE {
        UP, DOWN
    }

    public enum STATUS_VISIBLE {
        NORMAL, MINIMIZE, HIDDEN
    }

    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }*/
}