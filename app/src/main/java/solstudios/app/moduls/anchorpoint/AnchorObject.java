package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import solstudios.app.R;
import solstudios.app.utilities.Devices;

/**
 * Anchor Object la đơn vị hiển thị cơ bản nhất trên bản đồ,được xem là một hệ trục (x,y)
 * tương ứng với một đơn vị hiển thi trong phạm vi quản lí của nó
 * Created by SolbadguyKY on 05-Feb-17.
 */
public class AnchorObject extends View {

    public static final float DEFAULT_ANCHOR_X = 0;
    public static final float DEFAULT_ANCHOR_Y = 0;

    float anchorX, anchorY; ///Mỗi anchorobject có một tọa độ gốc
    Paint axisPaint, statusViewPaint, statusFillPaint;
    Path axisPath, statusViewPath, statusFillPath;
    private Canvas mCanvas;
    private MarkerAxisView markerAxisView;
    Rect dstRect;
    boolean needRedraw = false;

    public AnchorObject(Context context, MarkerAxisView markerAxisView) {
        super(context);
        this.markerAxisView = markerAxisView;
        init();
    }

    public AnchorObject(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialAttributeSet(context, attrs);
        init();
    }

    public AnchorObject(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialAttributeSet(context, attrs);
        init();
    }

    private void init() {
        axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setColor(Color.DKGRAY);
        axisPaint.setStrokeWidth(2);


        statusViewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusViewPaint.setStyle(Paint.Style.STROKE);
        statusViewPaint.setColor(Color.MAGENTA);
        statusViewPaint.setStrokeWidth(1);

        statusFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusFillPaint.setStyle(Paint.Style.FILL);
        statusFillPaint.setColor(Color.GREEN);

        //dstRect = new Rect(markerAxis.getLeft(), markerAxis.getTop(), markerAxis.getRight(), markerAxis.getBottom());

    }

    void initialAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnchorObject);

        this.anchorX = typedArray.getFloat(R.styleable.AnchorObject_anchorX, DEFAULT_ANCHOR_X);
        this.anchorY = typedArray.getFloat(R.styleable.AnchorObject_anchorY, DEFAULT_ANCHOR_Y);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.getClipBounds(dstRect);
        super.onDraw(canvas);

        if (mCanvas == null) {
            canvas.drawPath(axisPath, axisPaint);
            canvas.drawPath(statusViewPath, statusViewPaint);
            mCanvas = canvas;
        } else {
            if (needRedraw) {
                canvas.drawPath(axisPath, axisPaint);
                canvas.drawPath(statusViewPath, statusViewPaint);
                needRedraw = false;
            }

        }

    }

    private void createPath() {
        if (axisPath == null) {
            axisPath = new Path();
        } else {
            //axisPath.c
            axisPath.reset();
        }

        axisPath.moveTo(markerAxisView.markerX, 0);
        axisPath.lineTo(markerAxisView.markerX, Devices.getDefaultDevice(getContext()).getSize(false));

        axisPath.moveTo(0, markerAxisView.markerY);
        axisPath.lineTo(Devices.getDefaultDevice(getContext()).getSize(true), markerAxisView.markerY);
    }

    void createStatusPath() {
        if (statusViewPath == null) {
            statusViewPath = new Path();
        } else {
            //axisPath.c
            statusViewPath.reset();
        }

        switch (markerAxisView.markerStatusSide) {
            case UP:
                ///
                if (markerAxisView.markerStatusVisible == MarkerAxisView.STATUS_VISIBLE.MINIMIZE) {
                    statusViewPath.moveTo(markerAxisView.markerX, markerAxisView.markerY);
                    statusViewPath.lineTo(markerAxisView.markerX + markerAxisView.minWidth, markerAxisView.markerY);
                    statusViewPath.lineTo(markerAxisView.markerX + markerAxisView.minWidth, markerAxisView.markerY - markerAxisView.minHeight);
                    statusViewPath.lineTo(markerAxisView.markerX, markerAxisView.markerY - markerAxisView.minHeight);
                    statusViewPath.close();
                } else {
                    statusViewPath.moveTo(markerAxisView.markerX, markerAxisView.markerY);
                    statusViewPath.lineTo(markerAxisView.markerX + markerAxisView.maxWidth, markerAxisView.markerY);
                    statusViewPath.lineTo(markerAxisView.markerX + markerAxisView.maxWidth, markerAxisView.markerY - markerAxisView.maxHeight);
                    statusViewPath.lineTo(markerAxisView.markerX, markerAxisView.markerY - markerAxisView.maxHeight);
                    statusViewPath.close();
                }
                break;
            case DOWN:
                if (markerAxisView.markerStatusVisible == MarkerAxisView.STATUS_VISIBLE.MINIMIZE) {
                    statusViewPath.moveTo(markerAxisView.markerX, markerAxisView.markerY);
                    statusViewPath.lineTo(markerAxisView.markerX - markerAxisView.minWidth, markerAxisView.markerY);
                    statusViewPath.lineTo(markerAxisView.markerX - markerAxisView.minWidth, markerAxisView.markerY + markerAxisView.minHeight);
                    statusViewPath.lineTo(markerAxisView.markerX, markerAxisView.markerY + markerAxisView.minHeight);
                    statusViewPath.close();
                } else {
                    statusViewPath.moveTo(markerAxisView.markerX, markerAxisView.markerY);
                    statusViewPath.lineTo(markerAxisView.markerX - markerAxisView.maxWidth, markerAxisView.markerY);
                    statusViewPath.lineTo(markerAxisView.markerX - markerAxisView.maxWidth, markerAxisView.markerY + markerAxisView.maxHeight);
                    statusViewPath.lineTo(markerAxisView.markerX, markerAxisView.markerY + markerAxisView.maxHeight);
                    statusViewPath.close();
                }
                break;
            default:
                statusViewPath.moveTo(markerAxisView.markerX, markerAxisView.markerY);
                statusViewPath.lineTo(markerAxisView.markerX + markerAxisView.maxWidth, markerAxisView.markerY);
                statusViewPath.lineTo(markerAxisView.markerX + markerAxisView.maxWidth, markerAxisView.markerY - markerAxisView.maxHeight);
                statusViewPath.lineTo(markerAxisView.markerX, markerAxisView.markerY - markerAxisView.maxHeight);
                statusViewPath.close();
        }


    }

    void createStatusFill() {
        if (statusFillPath == null) {
            statusFillPath = new Path();
        } else {
            //axisPath.c
            statusFillPath.reset();
        }

        switch (markerAxisView.markerStatusVisible) {
            case NORMAL:

                break;
            case MINIMIZE:

                break;
            case HIDDEN:

                break;
            default:
        }

    }

    public void loadLocation() {
        //Logger.d("setLocation|x = %f , y = %f", x, y);
        if (mCanvas == null || needRedraw) {
            createPath();
            createStatusPath();
            createStatusFill();

            invalidate();
            requestLayout();
        }

    }

    interface SideListener {
        void onSizeChange();
    }
}
