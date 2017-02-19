package solstudios.app.moduls.mapviews.MapViewgroup;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by SolbadguyKY on 16-Feb-17.
 */

public class MapView extends MyAbsMapView {
    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public MyListAdapter getAdapter() {
        return null;
    }

    @Override
    public void setAdapter(MyListAdapter var1) {

    }

    @Override
    public void setSelection(int var1) {

    }
}
