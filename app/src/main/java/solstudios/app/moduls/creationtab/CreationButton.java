package solstudios.app.moduls.creationtab;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import solstudios.app.R;

/**
 * Created by SolbadguyKY on 31-Jan-17.
 */

public class CreationButton extends FrameLayout {

    public enum ButtonState {
        First, Second, Third
    }

    public enum PositionState {
        Top, Bottom, Midle, None
    }

    public View firstView, secondView, thirdView, currentSelectedView;
    public ButtonState currentSelectedButton = ButtonState.Third;
    public PositionState currentPosition = PositionState.Bottom;

    Context mContext;
    public float backViewWidth = 0;
    public float parentHeight = 0, parentMiddle = 0;
    private float oldChildWidth = 0;
    private float currentBaseY = 0;
    private int testingCount = 0;
    private boolean isReset = false;

    private ButtonStateChange buttonStateChangeListener;


    public CreationButton(Context context) {
        super(context);
        init(context);
    }

    public CreationButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CreationButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ///Tính toán chiều rộng dựa vào vị trí X hiện tại
        //float mesureWidth = MeasureSpec.getSize(widthMeasureSpec) - getX();
        //widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) mesureWidth, MeasureSpec.EXACTLY);

        //Logger.d(mesureWidth);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //resetChildViews();
        /*if (currentPosition == PositionState.Bottom) {

        } else {
            setupChildView();
        }*/
        ///resetChildViews();
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        //Logger.d(parentWidthMeasureSpec);
        /*if (currentPosition == PositionState.Bottom) {
            int totalChilds = availableChilds();
            if (totalChilds > 0) {
                ///tính toán chiều rộng mới
                final int measuredWidth = getWidth() / totalChilds;
                parentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
            }
        } else {
            Logger.d("measureChildWithMargins|child.tag = %s , selectedView.tag = %s", child.getTag(), currentSelectedView.getTag());
            if (child.getTag().equals(currentSelectedView.getTag())) {
                parentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY);
                parentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getHeight(), View.MeasureSpec.EXACTLY);
            } else {
                int totalChilds = availableChilds();
                if (totalChilds > 0) {
                    ///tính toán chiều rộng mới
                    final int measuredWidth = getWidth() / totalChilds;
                    parentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredWidth, View.MeasureSpec.EXACTLY);
                }
            }
        }*/
        if (oldChildWidth == 0) {
            int totalChilds = availableChilds();
            if (totalChilds > 0) {
                ///tính toán chiều rộng mới
                oldChildWidth = View.MeasureSpec.getSize(parentWidthMeasureSpec) / totalChilds;
            }
        }
        parentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) oldChildWidth, View.MeasureSpec.EXACTLY);
        super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //super.onLayout(changed, left, top, right, bottom);
        //float mesureWidth = MeasureSpec.getSize(widthMeasureSpec) - getX();
        //widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) mesureWidth, MeasureSpec.EXACTLY);
        // super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

    @Override
    public void setMeasureAllChildren(boolean measureAll) {
        super.setMeasureAllChildren(measureAll);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        resetChildViews();
        /*if (currentPosition == PositionState.Bottom) {
            resetChildViews();
        } else {
            setupChildView();
        }*/

    }

    /**
     * Khi x thay đổi, ta sẽ suy ra vị trí hiện tại của CreationTab thông qua công thức:
     * perX = (x/maxX) * 100 -> maxH * perX
     *
     * @param x khoảng cách di chuyển theo trực X của thanh creation
     */
    @Override
    public void setX(float x) {
        super.setX(x);

        currentBaseY = getCurrentYBaseOnXDistance(x);
        //Logger.d(currentBaseY);
        ///
        if (!isReset || currentBaseY > parentMiddle) {
            //Logger.d(secondView.getX());
            animateSelectedTab(x);
        }

    }

    void init(Context context) {
        mContext = context;
        inflate(context, R.layout.creation_view, this);
        firstView = findViewById(R.id.creationView_1);
        secondView = findViewById(R.id.creationView_2);
        thirdView = findViewById(R.id.creationView_3);

        currentSelectedView = firstView;

        firstView.setOnClickListener(new CreationButtonClickListener());
        secondView.setOnClickListener(new CreationButtonClickListener());
        thirdView.setOnClickListener(new CreationButtonClickListener());
    }

    /**
     * Reset lại trạng thái của các button tab khi thanh creation đang nằm ở Bottom
     */
    void resetChildViews() {
        // Logger.i("resetChildViews");
        if (firstView != null) {
            FrameLayout.LayoutParams firstViewLayoutParams = (LayoutParams) firstView.getLayoutParams();
            firstViewLayoutParams.width = (int) oldChildWidth;
            firstViewLayoutParams.leftMargin = 0;

            firstView.setLayoutParams(firstViewLayoutParams);

            setFirstViewMargin(0, ((LayoutParams) firstView.getLayoutParams()).topMargin,
                    ((LayoutParams) firstView.getLayoutParams()).rightMargin,
                    ((LayoutParams) firstView.getLayoutParams()).bottomMargin);

        } else return;

        if (secondView != null) {
            FrameLayout.LayoutParams secondViewLayoutParams = (LayoutParams) secondView.getLayoutParams();
            secondViewLayoutParams.width = (int) oldChildWidth;

            secondView.setLayoutParams(secondViewLayoutParams);

            setSecondViewMargin((int) oldChildWidth, ((LayoutParams) secondView.getLayoutParams()).topMargin,
                    ((LayoutParams) secondView.getLayoutParams()).rightMargin,
                    ((LayoutParams) secondView.getLayoutParams()).bottomMargin);

        } else return;

        if (thirdView != null) {
            FrameLayout.LayoutParams thirdViewLayoutParams = (LayoutParams) thirdView.getLayoutParams();
            thirdViewLayoutParams.width = (int) oldChildWidth;

            thirdView.setLayoutParams(thirdViewLayoutParams);

            setThirdViewMargin((int) (oldChildWidth * 2), ((LayoutParams) thirdView.getLayoutParams()).topMargin,
                    ((LayoutParams) thirdView.getLayoutParams()).rightMargin,
                    ((LayoutParams) thirdView.getLayoutParams()).bottomMargin);

        } else return;
    }

    void setupChildView() {
        FrameLayout.LayoutParams params = (LayoutParams) currentSelectedView.getLayoutParams();
        params.leftMargin = 0;
        currentSelectedView.setLayoutParams(params);
    }

    int availableChilds() {
        int count = 0;
        if (firstView != null) {
            count++;
        }
        if (secondView != null) {
            count++;
        }
        if (thirdView != null) {
            count++;
        }

        return count;
    }

    /**
     * Khoảng cách di chuyển theo trục X sẽ tính ra được khoảng cách tương đối trên trục Y
     *
     * @param x
     * @return
     */
    float getCurrentYBaseOnXDistance(float x) {
        // Logger.d("getCurrentYBaseOnXDistance| debug: %f %f %f", backViewWidth, parentHeight, x);
        if (backViewWidth != 0 && parentHeight != 0) {
            float percentX = (Math.abs(x) / backViewWidth);
            float y = parentHeight * percentX;
            //Logger.d(y);
            return y;
        } else {
            return 0;
        }

    }

    /**
     * Tab được chọn sẽ lớn ra theo chiều rộng hết màn hình
     * Animation được tạo ra dựa trên thay đổi về Layout margin và layout width
     *
     * @param translatedX khoảng cách di chuyển của creation bar theo trục X
     */
    void animateSelectedTab(float translatedX) {
        //Logger.i("animateSelectedTab");
        getSelectedView();
        ///Tính toánh lại chiều rộng của Selected Tab
        /// perX = (x/maxX) * 100 -> maxH * perX
        ViewGroup.LayoutParams layoutParams = currentSelectedView.getLayoutParams();
        float perXMoved = translatedX / backViewWidth;
        float parentWidth = getWidth();
        float newWidth = perXMoved * parentWidth + oldChildWidth - backViewWidth;

        ///Check nếu chiều rộng > kích thước màn hình hoặc bé hơn kích thước tối thiểu
        if (newWidth < oldChildWidth) {
            newWidth = oldChildWidth;
        } else if (newWidth > parentWidth - backViewWidth) {
            newWidth = parentWidth - backViewWidth;
        }
        layoutParams.width = (int) newWidth;
        currentSelectedView.setLayoutParams(layoutParams);

        ///Set lại margin left cho từng view
        if (currentSelectedView.getTag().equals(firstView.getTag())) {
            ///Nếu status tab được chọn, mission/journal tab chỉ cẩn margin về bên phải
            setSecondViewMargin(firstView.getWidth(), ((LayoutParams) secondView.getLayoutParams()).topMargin,
                    ((LayoutParams) secondView.getLayoutParams()).rightMargin,
                    ((LayoutParams) secondView.getLayoutParams()).bottomMargin);
            setThirdViewMargin(secondView.getWidth() + firstView.getWidth(), ((LayoutParams) thirdView.getLayoutParams()).topMargin,
                    ((LayoutParams) thirdView.getLayoutParams()).rightMargin,
                    ((LayoutParams) thirdView.getLayoutParams()).bottomMargin);
        } else if (currentSelectedView.getTag().equals(secondView.getTag())) {
            ///Nếu mission tab được chọn, margin left sẽ tiến dần tới 0 và kéo dài hết màn hình
            /*setFirstViewMargin((int) -translatedX, ((LayoutParams) firstView.getLayoutParams()).topMargin,
                    ((LayoutParams) firstView.getLayoutParams()).rightMargin,
                    ((LayoutParams) firstView.getLayoutParams()).bottomMargin);
            */
            int leftTranslate = (int) (perXMoved * oldChildWidth);
            setSecondViewMargin((int) (oldChildWidth - leftTranslate),
                    ((LayoutParams) secondView.getLayoutParams()).topMargin,
                    ((LayoutParams) secondView.getLayoutParams()).rightMargin,
                    ((LayoutParams) secondView.getLayoutParams()).bottomMargin);

            ///margin của journal tab sẽ không thay đổi (tức là vẫn nằm ngay bên trái của secondView/missionTab
            setThirdViewMargin((int) (secondView.getWidth() + secondView.getLeft()), ((LayoutParams) thirdView.getLayoutParams()).topMargin,
                    ((LayoutParams) thirdView.getLayoutParams()).rightMargin,
                    ((LayoutParams) thirdView.getLayoutParams()).bottomMargin);
            //Logger.d("animateSelectedTab| debug = %d", (int) (backViewWidth - translatedX));
        } else {
            ///Nếu journal tab được chọn, margin left sẽ tiến dần tới 0 và kéo dài hết màn hình
            ///Tab mission sẽ đi dần về phái bên trái
            int leftTranslate = (int) (perXMoved * oldChildWidth);
            setFirstViewMargin((int) -leftTranslate, ((LayoutParams) firstView.getLayoutParams()).topMargin,
                    ((LayoutParams) firstView.getLayoutParams()).rightMargin,
                    ((LayoutParams) firstView.getLayoutParams()).bottomMargin);

            int leftSecondTranslate = (int) (oldChildWidth * perXMoved);
            setSecondViewMargin((int) (oldChildWidth - leftSecondTranslate),
                    ((LayoutParams) secondView.getLayoutParams()).topMargin,
                    ((LayoutParams) secondView.getLayoutParams()).rightMargin,
                    ((LayoutParams) secondView.getLayoutParams()).bottomMargin);

            ///tính toán khoảng cách di chuyển của journal tab theo %x
            float movingDistanceX = oldChildWidth * 2 * perXMoved;
            //Logger.d(movingDistanceX);
            setThirdViewMargin((int) (oldChildWidth * 2 - movingDistanceX),
                    ((LayoutParams) thirdView.getLayoutParams()).topMargin,
                    ((LayoutParams) thirdView.getLayoutParams()).rightMargin,
                    ((LayoutParams) thirdView.getLayoutParams()).bottomMargin);

        }

        //currentSelectedView.requestLayout();
        //Logger.d("animateSelectedTab| debug = %s", currentSelectedView.getTag());
    }

    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void setFirstViewMargin(int left, int top, int right, int bottom) {
        ///Set params cho view thứ 1
        FrameLayout.LayoutParams paramsFirstView = (LayoutParams) firstView.getLayoutParams();

        if (left < 0 && currentBaseY >= parentMiddle) {
            left = 0;
        }

        paramsFirstView.leftMargin = left;
        paramsFirstView.topMargin = top;
        paramsFirstView.rightMargin = right;
        paramsFirstView.bottomMargin = bottom;

        firstView.setLayoutParams(paramsFirstView);
    }


    /**
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void setSecondViewMargin(int left, int top, int right, int bottom) {
        ///Set params cho view thứ 2
        //Logger.d("setSecondViewMargin| debug: %d %d %d %d", left, top, right, bottom);

        FrameLayout.LayoutParams paramsSecondView = (LayoutParams) secondView.getLayoutParams();

        paramsSecondView.leftMargin = left;
        paramsSecondView.topMargin = top;
        paramsSecondView.rightMargin = right;
        paramsSecondView.bottomMargin = bottom;

        secondView.setLayoutParams(paramsSecondView);
    }

    void setThirdViewMargin(int left, int top, int right, int bottom) {
        //Logger.d(left);
        ///Set params cho view thứ 3
        FrameLayout.LayoutParams paramsThirdView = (LayoutParams) thirdView.getLayoutParams();
        if (left < firstView.getWidth() + secondView.getWidth()
                && currentBaseY <= parentHeight) {
            //left = firstView.getWidth() + secondView.getWidth();
        }
        paramsThirdView.leftMargin = left;
        paramsThirdView.topMargin = top;
        paramsThirdView.rightMargin = right;
        paramsThirdView.bottomMargin = bottom;

        //((LayoutParams) secondView.getLayoutParams()).leftMargin;
        thirdView.setLayoutParams(paramsThirdView);
    }

    void layoutSelectedTab() {
        //Logger.d(currentSelectedView);

        currentSelectedView.layout(0, 0, 0, 0);
    }

    void getSelectedView() {
        switch (currentSelectedButton) {
            case First:
                currentSelectedView = firstView;
                break;
            case Second:
                currentSelectedView = secondView;
                break;
            case Third:
                currentSelectedView = thirdView;
                break;
            default:
                currentSelectedView = firstView;
        }

    }

    public void onReset(boolean onReset) {
        this.isReset = onReset;
    }

    public void setButtonState(ButtonState buttonState) {
        this.currentSelectedButton = buttonState;
        if (buttonStateChangeListener != null) {
            buttonStateChangeListener.onChanged(buttonState);
        }
    }

    public interface ButtonStateChange {
        void onChanged(ButtonState buttonState);

        void onClicked(ButtonState buttonState);
    }


    public void setButtonStateChangeListener(ButtonStateChange buttonStateChangeListener) {
        this.buttonStateChangeListener = buttonStateChangeListener;
    }

    class CreationButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (buttonStateChangeListener != null) {
                if (view.getTag().equals(firstView.getTag())) {
                    buttonStateChangeListener.onClicked(ButtonState.First);
                } else if (view.getTag().equals(secondView.getTag())) {
                    buttonStateChangeListener.onClicked(ButtonState.Second);
                } else if (view.getTag().equals(thirdView.getTag())) {
                    buttonStateChangeListener.onClicked(ButtonState.Third);
                }

            }
        }
    }

    public View getViewAt(int position) {
        switch (position) {
            case 1:
                return this.firstView;
            case 2:
                return this.secondView;
            case 3:
                return this.thirdView;
            default:
                return null;
        }
    }
}
