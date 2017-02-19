package solstudios.app.moduls.creationtab;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import solstudios.app.R;
import solstudios.app.utilities.Devices;
import solstudios.app.utilities.Utils;

import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Bottom;
import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Bottom_Middle_0_1;
import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Bottom_Middle_1_2;
import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Middle;
import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Middle_Top_0_1;
import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Middle_Top_1_2;
import static solstudios.app.moduls.creationtab.DraggableLayout.View_Position.Top;

/**
 * Created by SolbadguyKY on 30-Jan-17.
 * Layout dựa trên Youtube view, nhưng thêm một trạng thái nữa là In-The-Middle
 */
public class DraggableLayout extends ViewGroup {

    public enum View_Position {
        ///Anchor
        Top, Bottom, Middle,
        ///Nằm trên và dưới nửa dưới
        Bottom_Middle_0_1, Bottom_Middle_1_2,
        ///Nằm trên và dưới nửa trên
        Middle_Top_0_1, Middle_Top_1_2;
    }

    private Context mContext;

    private final ViewDragHelper mDragHelper;

    private View mHeaderView;
    private View mDescView;

    private float mInitialMotionX;
    private float mInitialMotionY;
    public static int backViewWidth = 0;
    public static int parentWidth = 0, parentHeight = 0;
    public static int oldChildWidth = 0, oldChildHeight = 0;
    public static float middlePosition = 0;

    private int mDragRange;
    private int mTop;
    private float mDragOffset;
    private OnStateChange onStateChange;

    private static final int INVALID_POINTER = -1;
    private static final float MINIMUM_VELOCIT_X = 200f;
    private static final float MINIMUM_VELOCIT_Y_MIDDLE = 1500f;
    private static final float MINIMUM_VELOCIT_Y_BOTTOM = 2500f;
    private View_Position viewPositionState = Bottom;

    private int activePointerId = INVALID_POINTER;

    public DraggableLayout(Context context) {
        this(context, null);
        mContext = context;
        init(context);
    }

    public DraggableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        init(context);
    }

    void init(Context context) {
        oldChildWidth = Devices.getDefaultDevice(context).getSize(true) / CreationGroupView.availableChilds();
        oldChildHeight = Utils.getActionBarSize(context);
        middlePosition = Devices.getDefaultDevice(context).getSize(false) / 2;
    }


    @Override
    protected void onFinishInflate() {
        mHeaderView = findViewById(R.id.creationTab);
        mDescView = findViewById(R.id.creationScreen);
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
        //Logger.i("minimize");
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
                slideOffset = middlePosition;
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
                    onStateChange.onMiddlemize();
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
        //Handler countDownHanlder;
        //Runnable countDownRunnable;

        DragHelperCallback() {
            init();
        }

        void init() {
            /*countDownHanlder = new Handler();
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
            };*/
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
            //  mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
            ///Nếu view_position đang ở vị trí In-Middle thì chỉ cho phép settle khi đạt đủ velocity.y
            switch (getViewPositionState()) {
                case Top:
                    //mDragHelper.settleCapturedViewAt(0, top);
                    break;
                case Middle_Top_0_1:
                    //mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                    smoothSlideTo(Top);
                    break;
                case Middle_Top_1_2:
                    smoothSlideTo(Middle);
                    break;
                //mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                case Bottom_Middle_0_1:
                    smoothSlideTo(Middle);
                    break;
                case Middle:
                    //mDragHelper.settleCapturedViewAt(0, top);
                case Bottom_Middle_1_2:
                    smoothSlideTo(Bottom);
                    break;
                case Bottom:
                    //mDragHelper.settleCapturedViewAt(0, top);
                    break;
                default:
                    //Logger.i("Fall to default settling state");
                    //mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), top);
                    //mDragHelper.settleCapturedViewAt(0, top);
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
            //Logger.i(String.valueOf(state));
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
               /* if (countDownHanlder.hasMessages(COUNT_DOWN_MESSAGE)) {
                    countDownHanlder.removeCallbacks(countDownRunnable);
                    countDownHanlder.removeMessages(COUNT_DOWN_MESSAGE);
                }

                countDownHanlder.postDelayed(countDownRunnable, COUNT_DOWN);
                countDownHanlder.sendEmptyMessage(COUNT_DOWN_MESSAGE);*/
            }

            mDraggingState = state;
            super.onViewDragStateChanged(state);
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
                if (getViewPositionState() == Middle
                        || getViewPositionState() == Bottom_Middle_0_1
                        || getViewPositionState() == Middle_Top_1_2) {
                    if (mHeaderView.findViewById(R.id.creationTab_editorView) != null) {
                        float exactlyPositionY = getCreationBasePostionOnscreen(x, y)[1];
                        if (exactlyPositionY > 0) {
                            interceptTap = !mDragHelper.isViewUnder(mHeaderView.findViewById(R.id.creationTab_editorView), (int) x, (int) exactlyPositionY);
                        }

                        //Logger.d(mDragHelper.isViewUnder(mHeaderView.findViewById(R.id.creationTab_editorView), (int) x, (int) y));
                        //Logger.d(mDragHelper.isViewUnder(mHeaderView.findViewById(R.id.creationTab_editorViewHolder), (int) x, (int) y));
                    }

                }

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
                    checkSelectedView(x, y);
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        ///Nếu chưa có parent.size thì set tại đây
        if (parentHeight == 0) {
            parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        }

        if (parentWidth == 0) {
            parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        }

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        super.setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));

    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight() - mHeaderView.getHeight();

        mHeaderView.layout(0, mTop, r, mTop + mHeaderView.getMeasuredHeight());
        mDescView.layout(0, mTop + mHeaderView.getMeasuredHeight(), r, mTop + b);

        ///Set actionView size tại đây
        if (findViewById(R.id.creationTab_actionView) != null) {
            //Logger.d(findViewById(R.id.creationTab_actionView));
            if (backViewWidth == 0) {
                backViewWidth = MeasureSpec.getSize(findViewById(R.id.creationTab_actionView).getMeasuredWidth());
            }
        }

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

    /**
     * Dựa vào vị trí click trên thanh creation để xác định view được click vào
     *
     * @param x vị trí click.X trên màn hình
     * @param y vị trí click.Y trên màn hình
     * @return
     */
    View checkSelectedView(float x, float y) {
        //Logger.d("CheckSeletedView|debug: %f %f", x, y);
        CreationGroupView creationGroupView = castToCreationButtonView();
        if (creationGroupView != null) {
            ///exactlyPosY là vị trí click trên hệ trục gốc lấy từ CreationButton (0,0)

            float[] exactlyLocation = getCreationBasePostionOnscreen(x, y);
            float exactlyPosY = 0;
            if (exactlyLocation[1] > 0) {
                exactlyPosY = exactlyLocation[1];
            } else {
                exactlyPosY = y;
            }

            //exactlyPosY = y - (creationGroupView.parentHeight - mHeaderView.getTop());


            View firstView = creationGroupView.getViewAt(1);
            View secondView = creationGroupView.getViewAt(2);
            View thirdView = creationGroupView.getViewAt(3);
            View editorView = findViewById(R.id.creationTab_editorView);
            View editorViewHolder = findViewById(R.id.creationTab_editorViewHolder);
            View actionView = findViewById(R.id.creationTab_actionView);
            //Logger.d("checkSelectedView|debug:" + mDragHelper.isViewUnder(actionView, (int) x, (int) exactlyPosY));

            ///Nếu creation tab đang ở Top thì sẽ kểm tra ActionView
            if (getViewPositionState() == Top) {
                if (mDragHelper.isViewUnder(actionView, (int) x, (int) exactlyPosY)) {
                    minimize();
                    return actionView;
                } else {
                    ///Không process thao tac khác
                    return null;
                }

            }
            if (mDragHelper.isViewUnder(firstView, (int) x, (int) exactlyPosY)) {
                //Logger.i("A");

                firstView.performClick();
            } else if (mDragHelper.isViewUnder(secondView, (int) x, (int) exactlyPosY)) {
                //Logger.i("B");

                secondView.performClick();
            } else if (mDragHelper.isViewUnder(thirdView, (int) x, (int) exactlyPosY)) {
                //Logger.i("C");

                thirdView.performClick();
            } else if (mDragHelper.isViewUnder(editorView, (int) x, (int) exactlyPosY)
                    || mDragHelper.isViewUnder(editorViewHolder, (int) x, (int) exactlyPosY)) {
                //Logger.d("checkSelectedView|debug: " + getViewPositionState());
                if (getViewPositionState() == Middle || getViewPositionState() == Middle_Top_1_2) {
                    ///Có thể bật bàn phím ảo tại đây
                } else {
                    middlemize();
                }

            }

        }
        //Logger.d(creationButton);
        return null;
    }

    CreationGroupView castToCreationButtonView() {
        if (mHeaderView.findViewById(R.id.creationTab_actionFirstView) != null) {
            return (CreationGroupView) mHeaderView.findViewById(R.id.creationTab_actionFirstView);
        } else return null;
    }

    public void setState(View_Position state) {
        this.viewPositionState = state;
    }

    /**
     * Trả về vị trí hiện tại của thanh creation trên màn hình
     *
     * @return View_Position
     */
    public View_Position getViewPositionState() {
        doubleCheckForSure();
        //Logger.d("getViewPositionState|viewPositionState" + viewPositionState);
        //Logger.d(getMiddlePosition() + mHeaderView.getHeight() / 2);
        return this.viewPositionState;
    }

    /**
     * Kiểm tra vị trí của view trước khi trả về kết quả
     */
    void doubleCheckForSure() {
        CreationGroupView creationGroupView = castToCreationButtonView();

        if (creationGroupView != null) {
            /// Khoảng cách nữa dưới (dưới điểm In-Middle) sẽ trừ
            ///đi chiều cao của CreationButton
            float deltaMiddleBottom = parentHeight - middlePosition - mHeaderView.getHeight();
            if (mHeaderView.getTop() == 0) {
                this.viewPositionState = Top;
            } else if (mHeaderView.getTop() > 0 && mHeaderView.getTop() <= middlePosition / 2) {
                this.viewPositionState = Middle_Top_0_1;
            } else if (mHeaderView.getTop() > middlePosition / 2 && mHeaderView.getTop() <= middlePosition) {
                this.viewPositionState = Middle_Top_1_2;
            } else if (mHeaderView.getTop() > middlePosition
                    && mHeaderView.getTop() <= middlePosition + deltaMiddleBottom / 2)
            //&& mHeaderView.getTop() == creationButton.parentHeight - mHeaderView.getHeight()) {
            {
                this.viewPositionState = Bottom_Middle_0_1;

            } else if (mHeaderView.getTop() > middlePosition + deltaMiddleBottom / 2 && mHeaderView.getTop() < middlePosition + deltaMiddleBottom) {
                this.viewPositionState = Bottom_Middle_1_2;
            } else if (mHeaderView.getTop() == middlePosition + deltaMiddleBottom) {
                this.viewPositionState = Bottom;
            } else if (mHeaderView.getTop() == middlePosition) {
                this.viewPositionState = Middle;
            }
        }
    }

    View_Position getNearestPosition() {
        CreationGroupView creationGroupView = castToCreationButtonView();
        if (creationGroupView != null) {
            if (mHeaderView.getTop() < middlePosition) {
                return Top;
            } else if (mHeaderView.getTop() >= middlePosition
                    && mHeaderView.getTop() < parentHeight - mHeaderView.getHeight()) {
                return Middle;
            } else {
                return Bottom;
            }
        } else {
            return null;
        }
    }

    float[] getCreationBasePostionOnscreen(float x, float y) {
        float exactlyPosX = 0, exactlyPosY = 0;
        CreationGroupView creationGroupView = castToCreationButtonView();

        if (creationGroupView != null) {
            if (getViewPositionState() == Middle
                    || getViewPositionState() == Middle_Top_1_2
                    || getViewPositionState() == Bottom_Middle_0_1
                    ) {
                exactlyPosY = y - (parentHeight - mHeaderView.getTop());
            } else if (getViewPositionState() == Bottom || getViewPositionState() == Bottom_Middle_1_2) {
                exactlyPosY = y - (mHeaderView.getTop());
            }
        }
        return new float[]{exactlyPosX, exactlyPosY};
    }

}