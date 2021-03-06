package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import solstudios.app.R;
import solstudios.app.moduls.mapviews.DefaultMapAsymmetricGridViewAdapter;
import solstudios.app.moduls.mapviews.MapData.MapItem;
import solstudios.app.moduls.mapviews.MapImageAsymmetricGridViewItem;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_MarkerAxis;

/**
 * TODO: document your custom view class.
 */
public class StatusView extends AbsMapView implements I_MarkerAxis {
    public float markerX, markerY;
    public MarkerOptions mMarkerOption;
    private boolean isInitialized = false;
    private int count = 0;
    private static final String TEST_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    public StatusView(Context context) {
        super(context);
        init();
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        inflate(getContext(), R.layout.status_view_full, this);
        JCVideoPlayer.ACTION_BAR_EXIST = false;
        JCVideoPlayer.TOOL_BAR_EXIST = false;
    }

    @Override
    public void setMarkerOption(MarkerOptions markerOption) {
        this.mMarkerOption = markerOption;
    }

    @Override
    public void setMarkerLocation(LatLng latLng) {

    }

    @Override
    public void setLocationWithLayout(float x, float y) {
        setLocation(x, y);
        layoutParams(x, y);
    }

    @Override
    public void setLocation(float x, float y) {
        markerX = x;
        markerY = y;
    }

    @Override
    public void setMapItem(MapItem mapItem) {

    }


    public void setLocation(GoogleMap mMap) {
        if (mMarkerOption != null) {
            Projection projection = mMap.getProjection();
            ///tính toán lại vị trí của từng marker
            LatLng markerLocation = mMarkerOption.getPosition();
            Point screenPosition = projection.toScreenLocation(markerLocation);
            int screenPositionX = screenPosition.x;
            int screenPositionY = screenPosition.y;
            setLocation(screenPositionX, screenPositionY);
        }
    }


    public boolean isInitialized() {
        return this.isInitialized;
    }


    public void clearMarkerOption() {

    }

    @Override
    public void initializePoolObject() {
        setVisibility(VISIBLE);


        AsymmetricGridView asymmetricGridView = (AsymmetricGridView) findViewById(R.id.statusViewContent_ImageGirdView);
        if (asymmetricGridView != null) {
            asymmetricGridView.setRequestedColumnWidth(Utils.dpToPx(getContext(), 120));

            // initialize your items array
            DefaultMapAsymmetricGridViewAdapter adapter = new DefaultMapAsymmetricGridViewAdapter(getContext());

            for (int i = 0; i <= 4; i++) {
                adapter.add(createItemImage(i));
            }

            AsymmetricGridViewAdapter asymmetricAdapter =
                    new AsymmetricGridViewAdapter<>(getContext(), asymmetricGridView, adapter);

            asymmetricGridView.setAdapter(asymmetricAdapter);
        }

        TextView tv = (TextView) findViewById(R.id.statusViewContent_TextView);
        if (this.mMarkerOption != null && tv != null) {
            tv.setText(this.mMarkerOption.getTitle());
        }

        FrameLayout view = (FrameLayout) findViewById(R.id.videoHolder);
        view.setVisibility(GONE);

        VideoView videoView = (VideoView) findViewById(R.id.video);
        if (videoView != null) {
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
// Set video link (mp4 format )
            Uri video = Uri.parse(TEST_URL);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(video);
            videoView.start();
        }


        /*if (view.findViewWithTag(1111) == null) {
            final EasyVideoPlayer easyVideoPlayer = new EasyVideoPlayer(getContext());
            easyVideoPlayer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));

            easyVideoPlayer.setTag(1111);

            view.addView(easyVideoPlayer);
            easyVideoPlayer.setAutoPlay(true);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        easyVideoPlayer.setSource(Uri.parse(TEST_URL));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 500);

        }*/

        isInitialized = true;
    }

    @Override
    public void finalizePoolObject() {
        setVisibility(GONE);
        FrameLayout view = (FrameLayout) findViewById(R.id.videoHolder);
        if (view.findViewWithTag(1111) != null) {
            view.removeView(view.findViewWithTag(1111));
        }

        VideoView videoView = (VideoView) findViewById(R.id.video);
        if (videoView != null) {
            videoView.pause();
        }
    }

    public void layoutParams(float x, float y) {
        setX(x);
        setY(y);
    }

    MapImageAsymmetricGridViewItem createItemImage(int currentOffset) {
        int colSpan = Math.random() < 0.2f ? 2 : 1;
        // Swap the next 2 lines to have items with variable
        // column/row span.
        int rowSpan = Math.random() < 0.2f ? 2 : 1;
        return new MapImageAsymmetricGridViewItem(1, 1, currentOffset);
    }

}
