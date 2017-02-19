package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import solstudios.app.R;
import solstudios.app.moduls.mapviews.DefaultMapAsymmetricGridViewAdapter;
import solstudios.app.moduls.mapviews.MapImageAsymmetricGridViewItem;
import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_MarkerAxis;

/**
 * TODO: document your custom view class.
 */
public class StatusView extends AbsMapView implements I_MarkerAxis {
    public float markerX, markerY;
    public MarkerOptions mMarkerOption;
    private boolean isInitialized = false;

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
    }

    @Override
    public void setMarkerOption(MarkerOptions markerOption) {
        this.mMarkerOption = markerOption;
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

    @Override
    public boolean isInitialized() {
        return this.isInitialized;
    }

    @Override
    public void initializePoolObject() {
        isInitialized = true;

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
    }

    @Override
    public void finalizePoolObject() {

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
