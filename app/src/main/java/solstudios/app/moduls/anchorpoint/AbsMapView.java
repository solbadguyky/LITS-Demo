package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import solstudios.app.moduls.mapviews.MapPool.Interfaces.I_MarkerAxis;

/**
 * Created by SolbadguyKY on 18-Feb-17.
 */

public abstract class AbsMapView extends FrameLayout implements I_MarkerAxis {

    public String id;

    public enum Class {
        Status, Marker
    }

    public AbsMapView(Context context) {
        super(context);
    }

    public AbsMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbsMapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
