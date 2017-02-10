package solstudios.app.moduls.creationtab.behaviors;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.orhanobut.logger.Logger;

import solstudios.app.R;
import solstudios.app.moduls.creationtab.DraggableLayout;

/**
 * Di chuyển Appbar dựa vào trạng thái của thanh Creation
 */
public class CreationAppBarBarBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {

    private final Context mContext;
    private int mToolbarHeight;
    private int mMaxScrollAppBar;
    private float mImageSizeToolbar;
    private float mImageSizeMax;

    /**
     * Constructor
     *
     * @param context Context
     * @param attrs   AttributeSet
     */
    public CreationAppBarBarBehavior(Context context, AttributeSet attrs) {
        mContext = context;
        init();
    }

    private void init() {
        mImageSizeToolbar = mContext.getResources().getDimensionPixelSize(R.dimen.custom_behavior_image_size_toolbar);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency) || dependency instanceof DraggableLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {

        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        if (parent.findViewById(R.id.draggableLayout) != null) {
            DraggableLayout draggableLayout = (DraggableLayout) parent.findViewById(R.id.draggableLayout);
            int distance = draggableLayout.findViewById(R.id.creationTab).getTop()
                    + draggableLayout.findViewById(R.id.creationTab).getHeight();
            int viewHeight = draggableLayout.getHeight();
            if (viewHeight == 0) {
                viewHeight = parent.getHeight();
            }

            //Logger.d(distance);

            if (distance <= draggableLayout.getMiddlePosition()) {
                float distancePercent = calculateDistancePercentage((int) (viewHeight - draggableLayout.getMiddlePosition()), distance);
                translateY(child, distancePercent);
            }
        }
        return true;
    }

    /**
     * Tính toán khoảng cách di chuyển của thanh creation trên màn hình ->
     * quy ra tỉ lệ % so với kích thước màn hình / kích thước view
     *
     * @param viewHeight
     * @param distance
     * @return
     */
    private float calculateDistancePercentage(int viewHeight, int distance) {
        return (distance * 100) / viewHeight;
    }

    /**
     * Ẩn thanh Appbar dựa vào khoảng cách di chuyển thanh Creation
     *
     * @param v               appbar view
     * @param distancePercent % di chuyển của CreationTab trên màn hình
     */
    private void translateY(View v, float distancePercent) {
        //Logger.d("translateY| v.y = %f , y = %f", v.getY(), distancePercent);
        float transtaleDistance = v.getHeight() * (100 - distancePercent) / 100;
        v.setY(-transtaleDistance);
    }
}