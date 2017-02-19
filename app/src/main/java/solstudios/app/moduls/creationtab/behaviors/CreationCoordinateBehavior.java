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
 * Theo dõi sự di chuyển của Creation Tab để layout
 * Created by SolbadguyKY on 27-Jan-17.
 */
public class CreationCoordinateBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {
    Context mContext;
    private int mToolbarHeight;
    private int mMaxScrollAppBar;
    private float mImageSizeToolbar;
    private float mImageSizeMax;

    /**
     * Constructor cho code
     *
     * @param context context The {@link Context}.
     */
    public CreationCoordinateBehavior(Context context) {
        mContext = context;
        init();
    }

    /**
     * constructor cho xml
     *
     * @param context context The {@link Context}.
     * @param attrs   The {@link AttributeSet}.
     */
    public CreationCoordinateBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mImageSizeToolbar = mContext.getResources().getDimensionPixelSize(R.dimen.custom_behavior_image_size_toolbar);
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        //Logger.d(dependency);
        return super.layoutDependsOn(parent, child, dependency) || dependency instanceof DraggableLayout;
    }

    @Override
    public boolean blocksInteractionBelow(CoordinatorLayout parent, AppBarLayout child) {
        // Logger.d(child);
        return true;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        //Logger.d(dependency);

        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        if (parent.findViewById(R.id.draggableLayout) != null) {
            DraggableLayout draggableLayout = (DraggableLayout) parent.findViewById(R.id.draggableLayout);
            int distance = draggableLayout.findViewById(R.id.creationTab).getTop();
            // + draggableLayout.findViewById(R.id.creationTab).getHeight();
            int viewHeight = DraggableLayout.parentHeight - draggableLayout.getmHeaderView().getHeight();

            View creationChilds = draggableLayout.findViewById(R.id.creationTab);
            //Logger.d("debug|%d %f", distance, draggableLayout.getMiddlePosition());
            ///Khi thanh creation di chuyển qua điểm neo giữa thì mới di chuyển app bar
            if (distance <= DraggableLayout.middlePosition) {
                float distancePercent = calculateDistancePercentage((int) (viewHeight), distance);
                translateY(parent.findViewById(R.id.appBar), distancePercent);

                translateCreationChild(creationChilds, distancePercent);
            } else {
                ///Nếu không thì sẽ reset state của các creation button
                resetCreationChild(creationChilds);

                ///Nêu nằm nửa dưới màn hình thì điều chỉnh editor/editor holder
                float newViewHeight = viewHeight - DraggableLayout.middlePosition - draggableLayout.getmHeaderView().getHeight();
                float newDistance = distance - DraggableLayout.middlePosition;
                float distancePercent = calculateDistancePercentage((int) newViewHeight, (int) newDistance);
                View editorViewHolder = parent.findViewById(R.id.creationTab_editorViewHolder);
                if (editorViewHolder != null) {
                    transitingEditorViewHolder(editorViewHolder, distancePercent);
                }

                ///Cho hiển thị Editor thật
                View editorView = parent.findViewById(R.id.creationTab_editorView);
                if (editorView != null) {
                    transitingEditorView(editorView, distancePercent);
                }
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
        if (viewHeight == 0) {
            return 0;
        }
        return (distance * 100) / viewHeight;
    }


    private void translateCreationChild(View v, float distancePercent) {
        //get actionview
        View actionView = v.findViewById(R.id.creationTab_actionView);
        View creationViews = v.findViewById(R.id.creationTab_actionFirstView);

        float movingDistance = (-(DraggableLayout.backViewWidth) * (distancePercent)) / 100;
        Logger.d(movingDistance);

        actionView.setX(movingDistance);

        shiftAndResizeCreationTab(creationViews, actionView);
        // v.requestLayout();
    }

    /**
     * Di chuyển thanh creation và Resize theo vị trí của Action View
     *
     * @param view
     * @param dependency
     */
    private void shiftAndResizeCreationTab(View view, View dependency) {
        ///Di chuyển Creation view một khoảng deltaX
        float x = dependency.getX() + dependency.getWidth();
        view.setX(x);
    }

    private void resetCreationChild(View v) {
        View actionView = v.findViewById(R.id.creationTab_actionView);
        actionView.setX(-(actionView.getWidth()));
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

    /**
     * Resize editorView dua vao khoang cach di chuyen cua
     *
     * @param v               EditorView cần resize
     * @param distancePercent Tỉ lệ phần trăm di chuyển để tính toánh kích thước mới của EditorView
     */
    private void transitingEditorViewHolder(View v, float distancePercent) {
        //Logger.d(distancePercent);
        ///Tính toán lại kích thước mới

        float visiblePercent = distancePercent / 100;
        //Logger.d(visiblePercent);
        ///Set View.height
        //v.setPivotX(0);
        //v.setPivotY(0);
        v.setAlpha(visiblePercent);

        if (visiblePercent == 0) {
            v.setVisibility(View.GONE);
        } else {
            // v.setVisibility(View.VISIBLE);
            v.requestFocus();
        }

    }

    private void transitingEditorView(View v, float distancePercent) {
        float visiblePercent = 1 - distancePercent / 100;
        //Logger.d(visiblePercent);
        ///Set View.height
        //v.setPivotX(0);
        //v.setPivotY(0);
        //v.setAlpha(visiblePercent);
        if (visiblePercent == 1.0) {
            // v.setVisibility(View.INVISIBLE);
        } else {
            // v.setVisibility(View.VISIBLE);
            v.requestFocus();
        }

    }
}
