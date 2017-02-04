package solstudios.app.moduls.creationtab;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import solstudios.app.R;

/**
 * Created by SolbadguyKY on 27-Jan-17.
 */

public class CreationCoordinateBehavior extends CoordinatorLayout.Behavior<View> {
    Context mContext;

    public CreationCoordinateBehavior() {
    }

    public CreationCoordinateBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {

        return /*super.onTouchEvent(parent, child, ev)*/ true;
    }

    @Override
    public boolean blocksInteractionBelow(CoordinatorLayout parent, View child) {
        return /*super.blocksInteractionBelow(parent, child)*/ true;
    }

    @Override
    public float getScrimOpacity(CoordinatorLayout parent, View child) {
        // Logger.d(super.getScrimOpacity(parent, child));
        return super.getScrimOpacity(parent, child);
    }

    @Override
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout parent, View child, Rect rectangle, boolean immediate) {
        return super.onRequestChildRectangleOnScreen(parent, child, rectangle, immediate);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        // Logger.d(child);

        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        // Logger.d("onDependentViewChanged");
        if (dependency.getId() == R.id.appBar) {
            //View topAnchorView = parent.findViewById(R.id.scrollView_Anchor);
            //View creationTab = parent.findViewById(R.id.creationTab);

            if (child instanceof DraggableLayout) {
                int anchorPos = dependency.getTop();
                int distance = (int) ((DraggableLayout) child).findViewById(R.id.creationTab).getTop();
                //translateAppBar(dependency, distance);
                // Logger.d("onDependentViewChanged|height = %d , distances = %d", anchorPos, distance);
            }
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        if (parent.findViewById(R.id.draggableLayout) != null) {
            DraggableLayout draggableLayout = (DraggableLayout) parent.findViewById(R.id.draggableLayout);
            int distance = draggableLayout.findViewById(R.id.creationTab).getTop();
            int viewHeight;
            viewHeight = (int) (parent.getHeight() - draggableLayout.getmHeaderView().getHeight() - draggableLayout.getMiddlePosition());
            //Devices.getDefaultDevice(mContext).getSize(false);
            float distancePercent = calculateDistancePercentage(viewHeight, distance);
            //Logger.d("onLayoutChild|debug: %d %d", viewHeight, distance);
            View creationChilds = draggableLayout.findViewById(R.id.creationTab);

            ///Nếu khoảng cách di chuyển của Dragglayout đạt đủ độ cao sẽ dịch chuyển actionView
            if (distancePercent <= 100) {
                translateCreationChild(creationChilds, distancePercent);
            } else {
                ///Nếu không thì sẽ reset state của các creation button
                resetCreationChild(creationChilds);


            }
        }
        return super.onLayoutChild(parent, child, layoutDirection);
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
        if (viewHeight == 0) {
            return 0;
        }
        return (distance * 100) / viewHeight;
    }


    private void translateCreationChild(View v, float distancePercent) {
        //get actionview
        View actionView = v.findViewById(R.id.creationTab_actionView);

        float movingDistance = (-(actionView.getWidth()) * (distancePercent)) / 100;
        // Logger.d(distancePercent);

        actionView.setX(movingDistance);

        // v.requestLayout();
    }

    private void resetCreationChild(View v) {
        View actionView = v.findViewById(R.id.creationTab_actionView);
        actionView.setX(-(actionView.getWidth()));

    }
}
