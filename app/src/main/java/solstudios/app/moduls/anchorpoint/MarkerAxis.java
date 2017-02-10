package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.orhanobut.logger.Logger;

import solstudios.app.utilities.Devices;

/**
 * Created by SolbadguyKY on 05-Feb-17.
 */

public class MarkerAxis extends View {
    public enum STATUS_SIDE {
        UP, DOWN
    }

    public enum STATUS_VISIBLE {
        NORMAL, MINIMIZE, HIDDEN
    }

    float markerX, markerY;
    Paint axisPaint, statusViewPaint, statusFillPaint;
    Path axisPath, statusViewPath, statusFillPath;
    private Canvas mCanvas;
    public STATUS_SIDE markerStatusSide = STATUS_SIDE.UP;
    public STATUS_VISIBLE markerStatusVisible = STATUS_VISIBLE.NORMAL;

    int maxWidth = 350, maxHeight = 500;
    int minWidth = 150, minHeight = 300;

    public MarkerAxis(Context context) {
        super(context);
        init();
    }

    public void setLocation(float x, float y) {
        //Logger.d("setLocation|x = %f , y = %f", x, y);
        this.markerX = x;
        this.markerY = y;
        createPath();
        createStatusPath();
        createStatusFill();
        //invalidate();
    }

    private void createPath() {
        if (axisPath == null) {
            axisPath = new Path();
        } else {
            //axisPath.c
            axisPath.reset();

        }

        axisPath.moveTo(markerX, 0);
        axisPath.lineTo(markerX, Devices.getDefaultDevice(getContext()).getSize(false));

        axisPath.moveTo(0, markerY);
        axisPath.lineTo(Devices.getDefaultDevice(getContext()).getSize(true), markerY);


        this.invalidate();
        this.requestLayout();

    }

    void createStatusPath() {
        if (statusViewPath == null) {
            statusViewPath = new Path();
        } else {
            //axisPath.c
            statusViewPath.reset();
        }

        switch (markerStatusSide) {
            case UP:
                if (markerStatusVisible == STATUS_VISIBLE.MINIMIZE) {
                    statusViewPath.moveTo(markerX, markerY);
                    statusViewPath.lineTo(markerX + minWidth, markerY);
                    statusViewPath.lineTo(markerX + minWidth, markerY - minHeight);
                    statusViewPath.lineTo(markerX, markerY - minHeight);
                    statusViewPath.close();
                } else {
                    statusViewPath.moveTo(markerX, markerY);
                    statusViewPath.lineTo(markerX + maxWidth, markerY);
                    statusViewPath.lineTo(markerX + maxWidth, markerY - maxHeight);
                    statusViewPath.lineTo(markerX, markerY - maxHeight);
                    statusViewPath.close();
                }
                break;
            case DOWN:
                if (markerStatusVisible == STATUS_VISIBLE.MINIMIZE) {
                    statusViewPath.moveTo(markerX, markerY);
                    statusViewPath.lineTo(markerX - minWidth, markerY);
                    statusViewPath.lineTo(markerX - minWidth, markerY + minHeight);
                    statusViewPath.lineTo(markerX, markerY + minHeight);
                    statusViewPath.close();
                } else {
                    statusViewPath.moveTo(markerX, markerY);
                    statusViewPath.lineTo(markerX - maxWidth, markerY);
                    statusViewPath.lineTo(markerX - maxWidth, markerY + maxHeight);
                    statusViewPath.lineTo(markerX, markerY + maxHeight);
                    statusViewPath.close();
                }
                break;
            default:
                statusViewPath.moveTo(markerX, markerY);
                statusViewPath.lineTo(markerX + maxWidth, markerY);
                statusViewPath.lineTo(markerX + maxWidth, markerY - maxHeight);
                statusViewPath.lineTo(markerX, markerY - maxHeight);
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

        switch (markerStatusVisible) {
            case NORMAL:

                break;
            case MINIMIZE:

                break;
            case HIDDEN:
                statusViewPath.reset();
                break;
            default:

        }


    }

    int getCurrentHeight() {
        switch (markerStatusVisible) {
            case NORMAL:
                return maxHeight;
            case MINIMIZE:
                return minHeight;
            default:
                return maxHeight;
        }
    }

    int getCurrentWidth() {
        switch (markerStatusVisible) {
            case NORMAL:
                return maxWidth;
            case MINIMIZE:
                return minWidth;
            default:
                return maxWidth;
        }
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logger.d("On Draw");

        mCanvas = canvas;
        canvas.drawPath(axisPath, axisPaint);
        canvas.drawPath(statusViewPath, statusViewPaint);
    }
}