package solstudios.app.anything.customizeViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.orhanobut.logger.Logger;

import solstudios.app.R;

/**
 * TODO: document your custom view class.
 */
public class StatusView extends FrameLayout {

    Paint arrowPaint;
    Path arrowPath;
    Rect defaultRect, newRect;
    Canvas defaultCanvas, newCanvas;

    public StatusView(Context context) {
        super(context);
        init();
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (defaultCanvas == null) {
            defaultCanvas = canvas;
            defaultCanvas.getClipBounds(defaultRect);
        }

        if (newCanvas == null) {
            super.onDraw(defaultCanvas);
            canvas.drawPath(arrowPath, arrowPaint);
        } else {
            newCanvas.clipRect(newRect, Region.Op.REPLACE);
            super.onDraw(newCanvas);
            canvas.drawPath(arrowPath, arrowPaint);
        }

    }

    private void init() {
        inflate(getContext(), R.layout.status_view, this);

        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setStyle(Paint.Style.STROKE);
        arrowPaint.setColor(Color.BLUE);
        arrowPaint.setStrokeWidth(2);

        arrowPath = new Path();

        newRect = new Rect();
        defaultRect = new Rect();
    }

    public void setMarkerLocattionOnScreen(float deltaX, float deltaY, boolean isUp) {
        Logger.d("setMarkerLocattionOnScreen|x = %f ; y = %f ; mX = %f ; mY = %f", deltaX, deltaY, getX(), getY());
        if (newCanvas == null) {
            newCanvas = new Canvas();
        }
        newCanvas = defaultCanvas;

        if (arrowPath != null) {
            arrowPath.reset();
        }

        //arrowPath.moveTo(0, getY());
        assert arrowPath != null;
        if (!isUp) {
            arrowPath.quadTo(0, 0, -deltaX, -deltaY);
        } else {
            // arrowPath.quadTo(0, getHeight(), -deltaX, deltaY - getHeight());
            arrowPath.moveTo(0, getHeight());
            arrowPath.lineTo(-deltaX, deltaY);
            //arrowPath.rQuadTo(0, getHeight(), -deltaX, deltaY - getHeight());
        }

        ///Tạo canvas mới
        newRect = defaultRect;
        newRect.inset(-105, -105);  //make the rect larger
        // newCanvas.clipRect(newRect, Region.Op.REPLACE);

        this.invalidate();
        this.requestLayout();

        //arrowPath.arcTo();
    }
}
