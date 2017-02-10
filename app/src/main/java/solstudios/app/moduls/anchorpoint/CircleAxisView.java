package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import solstudios.app.utilities.Devices;

import static solstudios.app.moduls.anchorpoint.LayoutAnchorActivity.SNAP_DISTANCE;

/**
 * Created by SolbadguyKY on 05-Feb-17.
 */

public class CircleAxisView extends View {
    Paint circlePaint = new Paint();
    Paint roundPaint = new Paint();
    Paint linePain = new Paint();
    Paint fakeScreenPaint = new Paint();

    Path mPath, mCirlePath, mLinePath, mScreenPath;
    boolean isTriggered = false;
    int pointX;
    int pointY;

    public CircleAxisView(Context context) {
        super(context);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.RED);

        roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        roundPaint.setStyle(Paint.Style.STROKE);
        roundPaint.setColor(Color.MAGENTA);

        linePain = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePain.setStyle(Paint.Style.STROKE);
        linePain.setColor(Color.RED);
        linePain.setStrokeWidth(2);

        fakeScreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fakeScreenPaint.setStyle(Paint.Style.STROKE);
        fakeScreenPaint.setColor(Color.BLUE);
        fakeScreenPaint.setStrokeWidth(2);
    }

    public void setPoint(int x, int y) {
        pointX = x;
        pointY = y;
        mPath = new Path();
        mPath.addCircle(x, y, 10, Path.Direction.CW);
        mPath.addCircle(Devices.getDefaultDevice(getContext()).getSize(true) / 2,
                Devices.getDefaultDevice(getContext()).getSize(false) / 2, 10, Path.Direction.CW);
        mPath.close();

        mCirlePath = new Path();
        mCirlePath.addCircle(x, y, SNAP_DISTANCE, Path.Direction.CW);
        mCirlePath.addCircle(Devices.getDefaultDevice(getContext()).getSize(true) / 2,
                Devices.getDefaultDevice(getContext()).getSize(false) / 2, 10, Path.Direction.CW);
        mCirlePath.close();

        mLinePath = new Path();
        mLinePath.moveTo(x, 0);
        mLinePath.lineTo(x, Devices.getDefaultDevice(getContext()).getSize(false));

        mLinePath.moveTo(0, y);
        mLinePath.lineTo(Devices.getDefaultDevice(getContext()).getSize(true), y);

        mScreenPath = new Path();
        mScreenPath.moveTo(Devices.getDefaultDevice(getContext()).getSize(true) - x, Devices.getDefaultDevice(getContext()).getSize(true) - y);
        mScreenPath.lineTo(Devices.getDefaultDevice(getContext()).getSize(true) - x, Devices.getDefaultDevice(getContext()).getSize(false));

        mScreenPath.moveTo(0, Devices.getDefaultDevice(getContext()).getSize(false) - x);
        mScreenPath.lineTo(Devices.getDefaultDevice(getContext()).getSize(true),
                Devices.getDefaultDevice(getContext()).getSize(false) - x);

        this.isTriggered = true;
        this.invalidate();

    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawLine(10, 20, 30, 40, paint);
        //canvas.drawLine(20, 10, 50, 20, paint);

        if (this.isTriggered) {
            canvas.drawPath(mPath, circlePaint);
            canvas.drawPath(mCirlePath, roundPaint);
            canvas.drawPath(mPath, circlePaint);
            canvas.drawPath(mLinePath, linePain);
            canvas.drawPath(mScreenPath, fakeScreenPaint);
        }
    }
}