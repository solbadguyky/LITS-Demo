package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class AbsMapView extends FrameLayout {

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

}
