package solstudios.app.moduls.creationtab;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import solstudios.app.R;

/**
 * Created by SolbadguyKY on 31-Jan-17.
 */

class CreationTabChildBehavior extends CoordinatorLayout.Behavior<CreationButton> {
    Context mContext;
    private int mInitialOffset;

    public CreationTabChildBehavior() {
    }

    public CreationTabChildBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, CreationButton child,
                                  int parentWidthMeasureSpec, int widthUsed,
                                  int parentHeightMeasureSpec, int heightUsed) {

        // final int offset = getChildMeasureOffset(parent, child);
       /* if (numberOfTab(parent) > 0) {
            final int measuredWidth = View.MeasureSpec.getSize(parentWidthMeasureSpec) / parent.getChildCount();
            int childMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
            child.measure(childMeasureSpec, parentHeightMeasureSpec);
        } else {
            return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }*/

        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CreationButton child, View dependency) {
        // Logger.d(dependency);
        if (dependency.getTag().equals("actionView")) {
            //View previous = getPreviousChild(parent, child);
            //Logger.d(dependency.getX());
            shiftAndResizeCreationTab(parent, child, dependency);
        }
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, CreationButton child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CreationButton child, View dependency) {
        //Logger.d(dependency);
        //View previous = getPreviousChild(parent, child);
        // return true;
        return (dependency != child && dependency instanceof CreationButton) ||
                dependency.getTag().equals("actionView");
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent,
                                 CreationButton child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        setCreationBackSize(parent, child);
        View previous = getPreviousChild(parent, child);
        if (previous != null) {
            //Logger.d(previous.getLeft());
            if (previous.getTag().equals("actionView")) {
                //child.setX(previous.getX() + previous.getWidth());
            }
        } else {
            // child.offsetLeftAndRight(0);
        }

        mInitialOffset = child.getLeft();
        return true;
    }

    private int getChildMeasureOffset(CoordinatorLayout parent, View child) {
        //Offset is the sum of all header heights except the target
        int offset = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view != child && (view instanceof CreationButton)) {
                offset += view.getWidth();
            }
        }

        return offset;
    }

    /**
     * Di chuyển thanh creation và Resize theo vị trí của Action View
     *
     * @param parent
     * @param view
     * @param dependency
     */
    private void shiftAndResizeCreationTab(CoordinatorLayout parent, View view, View dependency) {
        ///Di chuyển Creation view một khoảng deltaX
        float x = dependency.getX() + dependency.getWidth();
        view.setX(x);

        ///tính toán lại chiều rộng còn lại trên màn hình
        //CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        //float availableWidth = Devices.getDefaultDevice(mContext).getSize(true) - x;
        //params.width = (int) availableWidth;
        //view.setLayoutParams(params);

        //view.requestLayout();
        //view.invalidate();
        //Logger.d(availableWidth);
    }


    int numberOfTab(CoordinatorLayout parent) {
        int count = 0;
        for (int i = 0; i >= 0; i--) {
            View v = parent.getChildAt(i);
            if (v instanceof CreationButton) {
                count++;
            }
        }
        return count;
    }

    private View getPreviousChild(CoordinatorLayout parent, View child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex - 1; i >= 0; i--) {
            View v = parent.getChildAt(i);
            if (v instanceof CreationButton || v.getTag().equals("actionView")) {
                return v;
            }
        }

        return null;
    }

    private CreationButton getNextChild(CoordinatorLayout parent, View child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex + 1; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            if (v instanceof CreationButton) {
                return (CreationButton) v;
            }
        }

        return null;
    }

    private boolean setCreationBackSize(CoordinatorLayout parent, View view) {
        if (view instanceof CreationButton) {
            if (parent.findViewWithTag("actionView") != null) {
                ((CreationButton) view).backViewWidth = parent.findViewWithTag("actionView").getWidth();
                if (parent.getRootView().findViewById(R.id.activity_modul_creation_tab) instanceof CoordinatorLayout) {
                    ((CreationButton) view).parentHeight = parent.getRootView().findViewById(R.id.activity_modul_creation_tab).getHeight();
                }

                return true;
            }
        }
        return false;
    }

}
