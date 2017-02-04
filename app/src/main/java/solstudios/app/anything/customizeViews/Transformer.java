package solstudios.app.anything.customizeViews;

import android.view.View;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * https://github.com/pedrovgs/DraggablePanel
 */
abstract class Transformer {
    private final View view;
    private final View parent;

    private int marginRight;
    private int marginBottom;

    private float xScaleFactor;
    private float yScaleFactor;

    private int originalHeight;
    private int originalWidth;

    public Transformer(View view, View parent) {
        this.view = view;
        this.parent = parent;
    }

    public float getXScaleFactor() {
        return xScaleFactor;
    }

    public void setXScaleFactor(float xScaleFactor) {
        this.xScaleFactor = xScaleFactor;
    }

    public float getYScaleFactor() {
        return yScaleFactor;
    }

    public void setYScaleFactor(float yScaleFactor) {
        this.yScaleFactor = yScaleFactor;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = Math.round(marginRight);
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = Math.round(marginBottom);
    }

    /**
     * Change view height using the LayoutParams of the view.
     *
     * @param newHeight to change..
     */
    public void setViewHeight(int newHeight) {
        if (newHeight > 0) {
            originalHeight = newHeight;
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = newHeight;
            view.setLayoutParams(layoutParams);
        }
    }

    protected View getView() {
        return view;
    }

    protected View getParentView() {
        return parent;
    }

    public abstract void updatePosition(float verticalDragOffset);

    public abstract void updateScale(float verticalDragOffset);

    /**
     * @return height of the view before it has change the size.
     */
    public int getOriginalHeight() {
        if (originalHeight == 0) {
            originalHeight = view.getMeasuredHeight();
        }
        return originalHeight;
    }

    /**
     * @return width of the view before it has change the size.
     */
    public int getOriginalWidth() {
        if (originalWidth == 0) {
            originalWidth = view.getMeasuredWidth();
        }
        return originalWidth;
    }

    public boolean isViewAtTop() {
        return view.getTop() == 0;
    }

    public boolean isAboveTheMiddle() {
        int parentHeight = parent.getHeight();
        float viewYPosition = ViewHelper.getY(view) + (view.getHeight() * 0.5f);
        return viewYPosition < (parentHeight * 0.5);
    }

    public abstract boolean isViewAtRight();

    public abstract boolean isViewAtBottom();

    public abstract boolean isNextToRightBound();

    public abstract boolean isNextToLeftBound();

    /**
     * @return min possible height, after apply the transformation, plus the margin right.
     */
    public abstract int getMinHeightPlusMargin();

    /**
     * @return min possible width, after apply the transformation.
     */
    public abstract int getMinWidthPlusMarginRight();
}