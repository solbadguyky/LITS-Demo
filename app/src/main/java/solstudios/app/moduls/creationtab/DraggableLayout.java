package solstudios.app.moduls.creationtab;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import solstudios.app.R;
import solstudios.app.utilities.Devices;

import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Bottom;
import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Middle;
import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Top;

/**
 * Created by SolbadguyKY on 30-Jan-17.
 * Layout dựa trên Youtube view, nhưng thêm một trạng thái nữa là In-The-Middle
 */
public class DraggableLayout extends ViewGroup {

    public enum View_Position {
        Top, Bottom, Middle
    }

    private Context mContext;

    private final ViewDragHelper mDragHelper;

    private View mHeaderView;
    private View mDescView;

    private float mInitialMotionX;
    private float mInitialMotionY;

    private int mDragRange;
    private int mTop;
    private float mDragOffset;
    private OnStateChange onStateChange;
    private float middlePosition;
    private static final int INVALID_POINTER = -1;
    private static final float MINIMUM_VELOCIT_X = 200f;
    private static final float MINIMUM_VELOCIT_Y_MIDDLE = 1500f;
    private static final float MINIMUM_VELOCIT_Y_BOTTOM = 2500f;
    private View_Position viewPositionState = Bottom;

    private int activePointerId = INVALID_POINTER;

    public DraggableLayout(Context context) {
        this(context, null);
        mContext = context;
    }

    public DraggableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        mHeaderView = findViewById(R.id.creationTab);
        mDescView = findViewById(R.id.nestedScrollView);
    }

    public DraggableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
    }

    /**
     * Phóng to
     */
    public void maximize() {
        smoothSlideTo(Top);
    }

    /**
     * Thu nhỏ
     */
    public void minimize() {
        smoothSlideTo(Bottom);
    }

    /**
     * Chuyển sang dạng Middle
     */
    public void middlemize() {
        smoothSlideTo(Middle);
    }

    public void setMiddlePosition(float middlePosition) {
        this.middlePosition = middlePosition;
    }

    boolean smoothSlideTo(View_Position view_position) {
        final int topBound = getPaddingTop();
        float slideOffset = 0;
        switch (view_position) {
            case Top:
                slideOffset = 0f * mDragRange;
                break;
            case Bottom:
                slideOffset = 1f * mDragRange;
                break;
            case Middle:
                ///Nếu creation tab ở trạng thái Quick Editor, khoảng cách di chuyển sẽ là vị trí In-Middle
                slideOffset = getMiddlePosition();
                break;

        }
        int y = (int) (topBound + slideOffset);

        if (mDragHelper.smoothSlideViewTo(mHeaderView, /*mHeaderView.getLeft()*/ 0, y)) {
            ViewCompat.postInvalidateOnAnimation(this);
            if (view_position == Top) {
                if (onStateChange != null) {
                    onStateChange.onMinimize();
                }

                setState(Bottom);
            } else if (view_position == Middle) {
                if (onStateChange != null) {
                    onStateChange.onMaximize();
                }

                setState(Middle);
            } else if (view_position == Bottom) {
                if (onStateChange != null) {
                    onStateChange.onMaximize();
                }

                setState(Top);
            }
            return true;
        }
        return false;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {
        static final int COUNT_DOWN = 2000;
        static final int COUNT_DOWN_MESSAGE = 69;
        private int mDraggingState;
        private boolean countDownCallBack = false;
        Handler countDownHanlder;
        Runnable countDownRunnable;

        DragHelperCallback() {
            init();
        }

        void init() {
            countDownHanlder = new Handler();
            countDownRunnable = new Runnable() {
                @Override
                public void run() {
                    if (countDownCallBack) {
                        Logger.i("View need to re-position");
                        ///Kiểm tra vị trí ban đầu của view, nếu là Minimize hoặc In-Middle
                        // thì sẽ thực hiện re-position
                        if (getNearestPosition() != null) {
                            smoothSlideTo(getNearestPosition());
                        } else {
                            smoothSlideTo(getViewPositionState());
                        }
                    }
                }
            };
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mHeaderView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTop = top;

            mDragOffset = (float) top / mDragRange;

            mHeaderView.setPivotX(mHeaderView.getWidth());
            mHeaderView.setPivotY(mHeaderView.getHeight());
            // mHeaderView.setScaleX(1 - mDragOffset / 2);
            // mHeaderView.setScaleY(1 - mDragOffset / 2);

            //mDescView.setAlpha(1 - mDragOffset);

            requestLayout();

            if (onStateChange != null) {
                onStateChange.onStartDragging();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int top = getPaddingTop();
            if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                top += mDragRange;
            }
            //Logger.d("onViewReleased|debug: %f %f", xvel, yvel);
            ///Nếu view_position đang ở vị trí In-Middle thì chỉ cho phép settle khi đạt đủ velocity.y
            switch (getViewPositionState()) {
                case Middle:
                    //Logger.i("In-Middle settling state");
                    if (yvel >= MINIMUM_VELOCIT_Y_MIDDLE) {
                        mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                    } else {
                        middlemize();
                    }
                    break;
                case Bottom:
                    //Logger.i("Bottom settling state");
                    if (yvel <= -MINIMUM_VELOCIT_Y_BOTTOM) {
                        mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                    } else {
                        minimize();
                    }
                    break;
                default:
                    //Logger.i("Fall to default settling state");
                    mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                    break;
            }
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mHeaderView.getHeight() - mHeaderView.getPaddingBottom();

            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            Logger.i(String.valueOf(state));
            if (state == mDraggingState) { // no change
                //Logger.i("This view no change");
                countDownCallBack = false;
                return;
            }

            if ((mDraggingState == ViewDragHelper.STATE_DRAGGING || mDraggingState == ViewDragHelper.STATE_SETTLING) &&
                    state == ViewDragHelper.STATE_IDLE) {
                /// the view stopped from moving.
                //Logger.i("This view stopped from moving");
                countDownCallBack = false;
            }

            if (state == ViewDragHelper.STATE_DRAGGING) {
                /// the view is dragged.
                //Logger.i("This view is dragged");
                countDownCallBack = true;
                ///Nếu như view đang ở trạng thái dragging, thi sẽ trigger một countdown timer,
                // quá thời gian active sẽ trả view về vị trí cũ
                if (countDownHanlder.hasMessages(COUNT_DOWN_MESSAGE)) {
                    countDownHanlder.removeCallbacks(countDownRunnable);
                    countDownHanlder.removeMessages(COUNT_DOWN_MESSAGE);
                }

                countDownHanlder.postDelayed(countDownRunnable, COUNT_DOWN);
                countDownHanlder.sendEmptyMessage(COUNT_DOWN_MESSAGE);
            }

            mDraggingState = state;
            super.

                    onViewDragStateChanged(state);
        }

    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            //Logger.i("computeScroll");
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if ((action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }

        final float x = ev.getX();
        final float y = ev.getY();
        boolean interceptTap = false;

        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mDragHelper.cancel();
                return false;
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                interceptTap = mDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = mDragHelper.getTouchSlop();
                if (ady > slop && adx > ady) {
                    mDragHelper.cancel();
                    return false;
                }
            }
        }

        return mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);

        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();

        boolean isHeaderViewUnder = mDragHelper.isViewUnder(mHeaderView, (int) x, (int) y);
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = mDragHelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
                    if (mDragOffset == 0) {
                        //smoothSlideTo(1f);
                    } else {
                        //smoothSlideTo(0f);
                        ///Kiểm tra vị trí click có thuộc view nào không
                        checkSelectedView(x, y);
                    }
                }
                break;
            }
        }


        return isHeaderViewUnder && isViewHit(mHeaderView, (int) x, (int) y) || isViewHit(mDescView, (int) x, (int) y);
    }


    /**
     * Kiểm tra vị trí click/drag có nằm trong Dragglayout hay không
     *
     * @param view view kiểm tra
     * @param x    vị trí click.x
     * @param y    vị trí click.y
     * @return true nếu như click vào view
     */
    private boolean isViewHit(View view, int x, int y) {
        //Logger.d("isViewHit|debug: %d %d", x, y);
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight() - mHeaderView.getHeight();

        mHeaderView.layout(0, mTop, r, mTop + mHeaderView.getMeasuredHeight());
        mDescView.layout(0, mTop + mHeaderView.getMeasuredHeight(), r, mTop + b);
    }

    public interface OnStateChange {
        void onMinimize();

        void onMaximize();

        void onMiddlemize();

        void onStartDragging();

    }

    public void setOnStateChangeListener(OnStateChange onStateChangeListener) {
        this.onStateChange = onStateChangeListener;
    }

    public View getBottomView() {
        return this.mDescView;
    }

    public View getmHeaderView() {
        return this.mHeaderView;
    }

    public float getMiddlePosition() {
        if (this.middlePosition == 0) {
            return Devices.getDefaultDevice(mContext).getSize(false) / 2;
        } else {
            return this.middlePosition;
        }
    }

    /**
     * Dựa vào vị trí click trên thanh creation để xác định view được click vào
     *
     * @param x vị trí click.X trên màn hình
     * @param y vị trí click.Y trên màn hình
     * @return
     */
    View checkSelectedView(float x, float y) {
        CreationButton creationButton = castToCreationButtonView();
        if (creationButton != null) {
            ///exactlyPosY là vị trí click trên hệ trục gốc lấy từ CreationButton
            float exactlyPosY = 0;
            if (getViewPositionState() == Bottom) {
                ///Nếu creation đang ở Bottom, thì Y của CreationButton sẽ bằng
                // (parent.height - creationBar.height)
                exactlyPosY = y - (creationButton.parentHeight - mHeaderView.getHeight());
            } else if (getViewPositionState() == Middle) {
                exactlyPosY = y - (creationButton.parentHeight - getMiddlePosition());
            } else {
                exactlyPosY = -1;
                return null;
            }

            //Logger.d("checkSelectedView|debug: %f %f", x, exactlyPosY);
            //Logger.d("checkSelectedView|debug: " + getViewPositionState());

            View firstView = creationButton.getViewAt(1);
            View secondView = creationButton.getViewAt(2);
            View thirdView = creationButton.getViewAt(3);

            //Logger.d(firstView.getWidth() + secondView.getWidth());

            if (exactlyPosY < creationButton.getHeight()) {
                //Logger.d("checkSelectedView|debug: %f %f", x, exactlyPosY);
                ///Khi click vào creation button thì sẽ process chức năng filter
                if (x < firstView.getWidth()) {
                    firstView.performClick();
                } else if (x < firstView.getWidth() + secondView.getWidth()) {
                    secondView.performClick();
                } else {
                    thirdView.performClick();
                }
            } else {
                ///Click vào khu vực nhập liệu sẽ chuyển sang dạng In-Middle
                middlemize();
            }
        }
        //Logger.d(creationButton);
        return null;
    }

    CreationButton castToCreationButtonView() {
        if (mHeaderView.findViewById(R.id.creationTab_actionFirstView) != null) {
            return (CreationButton) mHeaderView.findViewById(R.id.creationTab_actionFirstView);
        } else return null;
    }

    public void setState(View_Position state) {
        this.viewPositionState = state;
    }

    public View_Position getViewPositionState() {
        doubleCheckForSure();
        return this.viewPositionState;
    }

    /**
     * Kiểm tra vị trí của view trước khi trả về kết quả
     */
    void doubleCheckForSure() {
        CreationButton creationButton = castToCreationButtonView();
        if (creationButton != null) {
            if (mHeaderView.getTop() == 0) {
                this.viewPositionState = Top;
            } else if (mHeaderView.getTop() == creationButton.parentHeight - mHeaderView.getHeight()) {
                this.viewPositionState = Bottom;
            } else if (mHeaderView.getTop() == getMiddlePosition()) {
                this.viewPositionState = Middle;
            }
        }
    }

    View_Position getNearestPosition() {
        CreationButton creationButton = castToCreationButtonView();
        if (creationButton != null) {
            if (mHeaderView.getTop() < getMiddlePosition()) {
                return Top;
            } else if (mHeaderView.getTop() >= getMiddlePosition()
                    && mHeaderView.getTop() < creationButton.parentHeight - mHeaderView.getHeight()) {
                return Middle;
            } else {
                return Bottom;
            }
        } else {
            return null;
        }
    }
}