package solstudios.app.moduls.mapviews.MapViewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by SolbadguyKY on 16-Feb-17.
 */

public class MyMapAdapterView extends MyAbsAdapterView {

    public MyMapAdapterView(Context context) {
        super(context);
    }

    public MyMapAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMapAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyMapAdapterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public MyAdapter getAdapter() {
        return null;
    }

    @Override
    public void setAdapter(MyAdapter var1) {

    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int var1) {

    }
}
