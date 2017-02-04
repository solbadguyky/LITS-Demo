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

/**
 * TODO: document your custom view class.
 */
public class CreationView extends FrameLayout {


    public CreationView(Context context) {
        super(context);
        init();
    }

    public CreationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CreationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    Paint paint = new Paint();
    private Path mPath;
    Rect defaultRect, newRect;
    Canvas defaultCanvas, newCanvas;

    @Override
    protected void onDraw(Canvas canvas) {
        if (defaultCanvas == null) {
            defaultCanvas = canvas;
            defaultCanvas.getClipBounds(defaultRect);
        }

        if (newCanvas == null) {
            super.onDraw(defaultCanvas);
        } else {
            newCanvas.clipRect(newRect, Region.Op.REPLACE);
            super.onDraw(newCanvas);
        }

        canvas.drawPath(mPath, paint);
    }

    private void init() {
        //inflate(getContext(), R.layout.creation_view, this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);

        mPath = new Path();
        newRect = new Rect();
        defaultRect = new Rect();
    }

   /* public void setLabel(String labelText) {
        label.setText(labelText);
    }*/

    public void setUserLocation(float deltaX, float deltaY) {
        Logger.d("setUserLocation|x = %f ; y = %f", deltaX, deltaY);
        if (newCanvas == null) {
            newCanvas = new Canvas();
        }
        newCanvas = defaultCanvas;

        mPath.reset();

        mPath.moveTo(0, 0);
        mPath.lineTo(100, -250);
        ///Tạo canvas mới
        newRect = defaultRect;
        newRect.inset(0, -300);

        invalidate();
        requestLayout();

    }


}
