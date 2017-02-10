package solstudios.app.moduls.anchorpoint;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import solstudios.app.R;

/**
 * Anchor Object la đơn vị hiển thị cơ bản nhất trên bản đồ,được xem là một hệ trục (x,y)
 * tương ứng với một đơn vị hiển thi trong phạm vi quản lí của nó
 * Created by SolbadguyKY on 05-Feb-17.
 */
public class AnchorObject extends View {

    public static final float DEFAULT_ANCHOR_X = 0;
    public static final float DEFAULT_ANCHOR_Y = 0;

    float anchorX, anchorY; ///Mỗi anchorobject có một tọa độ gốc

    public AnchorObject(Context context) {
        super(context);
    }

    public AnchorObject(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialAttributeSet(context, attrs);
    }

    public AnchorObject(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialAttributeSet(context, attrs);
    }

    void initialAttributeSet(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnchorObject);

        this.anchorX = typedArray.getFloat(R.styleable.AnchorObject_anchorX, DEFAULT_ANCHOR_X);
        this.anchorY = typedArray.getFloat(R.styleable.AnchorObject_anchorY, DEFAULT_ANCHOR_Y);

        typedArray.recycle();
    }
}
